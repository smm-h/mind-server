package ir.smmh.mpg.ttt;

import ir.smmh.net.client.NotOkException;
import ir.smmh.net.client.impl.StandardClientImpl;
import ir.smmh.util.JSONUtil;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import static ir.smmh.util.JSONUtil.map;

public final class DesktopClient extends StandardClientImpl {

    private static final Stroke STROKE_GRID = new BasicStroke(1);
    private static final Stroke STROKE_XO = new BasicStroke(8);
    private static final int K = 160; // cell size
    private static final int P = K / 8; // padding
    private static final int R = K - P * 2; // radius
    private static final int SIZE = K * 3;
    private static final int[][] WINS = {
            {0, 1, 2}, {3, 4, 5},
            {6, 7, 8}, {0, 3, 6},
            {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };
    private final Cell[] cells = new Cell[9];
    private final Runnable refreshScreen;
    private final Cell ourSymbol, theirSymbol;
    private final Socket connection;
    private GameState gameState;
    private boolean itIsOurTurn;

    public DesktopClient(int port, String hostAddress, JSONObject authentication) {
        super(port, hostAddress, authentication);
        JFrame frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g0) {
                String title;
                if (gameState == null) {
                    title = "Preparing...";
                } else {
                    title = gameState.message;
                    if (gameState != GameState.UNACCEPTED) {
                        Graphics2D g = (Graphics2D) g0;
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g.setColor(Color.WHITE);
                        g.fillRect(0, 0, SIZE, SIZE);
                        g.setStroke(STROKE_XO);
                        for (int i = 0; i < cells.length; i++) {
                            Cell cell = cells[i];
                            if (cell != null) {
                                int x = K * (i % 3) + P;
                                int y = K * (i / 3) + P;
                                switch (cell) {
                                    case X:
                                        g.setColor(Color.RED);
                                        g.drawLine(x, y, x + R, y + R);
                                        g.drawLine(x + R, y, x, y + R);
                                        break;
                                    case O:
                                        g.setColor(Color.BLUE);
                                        g.drawOval(x, y, R, R);
                                        break;
                                }
                            }
                        }
                        g.setStroke(STROKE_GRID);
                        g.setColor(Color.GRAY);
                        g.drawLine(K, 0, K, SIZE);
                        g.drawLine(0, K, SIZE, K);
                        g.drawLine(K * 2, 0, K * 2, SIZE);
                        g.drawLine(0, K * 2, SIZE, K * 2);
                        if (gameState == GameState.PLAYING) {
                            title = itIsOurTurn
                                    ? "It is your turn."
                                    : "It is not your turn.";
                        }
                    }
                }
                frame.setTitle(title);
            }
        };
        panel.setPreferredSize(new Dimension(SIZE, SIZE));
        frame.setContentPane(panel);
        frame.pack();
        panel.setFocusable(true);
        panel.requestFocus();
        panel.setBackground(Color.WHITE);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent m) {
                if (gameState == GameState.PLAYING && itIsOurTurn) {
                    int position = (m.getX() / K) + (m.getY() / K) * 3;
                    if (cells[position] == null) {
                        println(System.out, "Sending: " + position);
                        try {
                            send("play", map("position", position));
                            println(System.out, "Delivered: " + position);
                            cells[position] = ourSymbol;
                            if (checkIfWon(ourSymbol)) {
                                gameState = GameState.WON;
                            } else if (checkIfTied()) {
                                gameState = GameState.TIED;
                            } else {
                                gameState = GameState.PLAYING;
                                itIsOurTurn = false;
                            }
                            refreshScreen.run();
                        } catch (NotOkException e) {
                            System.err.println("Failed to send request");
                        }
                    }
                }
            }
        });
        refreshScreen = () -> {
            panel.repaint();
            Toolkit.getDefaultToolkit().sync();
        };
        gameState = GameState.PLAYING;

        // center the window in the screen
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - SIZE) / 2;
        int y = (d.height - SIZE) / 2;
        frame.setLocation(x, y);

        try {
            itIsOurTurn = send("is_it_my_turn").getBoolean("is_it_your_turn");
            ourSymbol = itIsOurTurn ? Cell.X : Cell.O;
            theirSymbol = itIsOurTurn ? Cell.O : Cell.X;
            int k = d.width / 2;
            frame.setLocation((k - SIZE) / 2 + (itIsOurTurn ? 0 : k), y);
            connection = reconnect();
            var i = new DataInputStream(connection.getInputStream());
            new Thread(() -> {
                while (true) {
                    try {
                        JSONObject object = JSONUtil.parse(i.readUTF());
                        switch (object.getString("update")) {
                            case "player_played": {
                                String playerId = object.getString("player_id");
                                int position = object.getInt("played");
                                System.out.println(playerId + " played " + position);
                                println(System.out, "Received: " + position);
                                cells[position] = theirSymbol;
                                if (checkIfWon(theirSymbol)) {
                                    gameState = GameState.LOST;
                                } else if (checkIfTied()) {
                                    gameState = GameState.TIED;
                                } else {
                                    gameState = GameState.PLAYING;
                                    itIsOurTurn = true;
                                }
                                refreshScreen.run();
                                break;
                            }
                            case "player_left": {
                                String playerId = object.getString("player_id");
                                System.out.println(playerId + " left");
                                break;
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("Could not read from connection");
                    }
                }
            }).start();
            new Thread(() -> frame.setVisible(true)).start();
        } catch (NotOkException | IOException e) {
            throw new RuntimeException("Could not communicate with the server");
        }
    }

    private void println(PrintStream ps, String s) {
        ps.println(s);
    }

    private boolean checkIfWon(Cell s) {
        for (int[] win : WINS)
            if (cells[win[0]] == s && cells[win[1]] == s && cells[win[2]] == s)
                return true;
        return false;
    }

    private boolean checkIfTied() {
        for (Cell cell : cells)
            if (cell == null)
                return false;
        return true;
    }

    private enum Cell {
        X, O
    }

    private enum GameState {
        UNACCEPTED("Waiting for opponent..."),
        PLAYING(""),
        WON("You won!"),
        LOST("Opponent won!"),
        TIED("Game ended in a tie."),
        ;

        private final String message;

        GameState(String message) {
            this.message = message;
        }
    }
}
