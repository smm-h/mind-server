package ir.smmh.net.client.impl;

import ir.smmh.net.client.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientImpl implements Client {

    public static void main(String[] args) {
        Client client = new ClientImpl();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type in your requests as JSON to process them; or type in '.' to stop.");
        while (true) {
            try {
                System.out.print(">>> ");
                String request = input.readLine();
                if ("." .equals(request))
                    break;
                String response = client.connectToLocalHost(request);
                System.out.print("=== ");
                System.out.println(response);
            } catch (IOException e) {
                System.err.println("Something went wrong; try again please");
            }
        }
        try {
            input.close();
        } catch (IOException e) {
            System.err.println("Failed to close input");
        }
    }

    @Override
    public final int getDefaultPort() {
        return 7000;
    }

    @Nullable
    @Override
    public final String connectToHost(@NotNull String address, int port, @NotNull String request) {
        String response = null;
        try {
            Socket socket = new Socket(address, port);
            DataInputStream res = new DataInputStream(socket.getInputStream());
            DataOutputStream req = new DataOutputStream(socket.getOutputStream());
            try {
                req.writeUTF(request);
                try {
                    response = res.readUTF();
                } catch (IOException e) {
                    System.err.println("Could not read response");
                }
            } catch (IOException e) {
                System.err.println("Could not write request");
            }
            try {
                res.close();
                req.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("Could not close resources");
            }
        } catch (UnknownHostException u) {
            System.err.println("Could not findIdeaByName the host");
        } catch (IOException e) {
            System.err.println("Connection failed");
        }
        return response;
    }
}
