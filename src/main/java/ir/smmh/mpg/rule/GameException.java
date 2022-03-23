package ir.smmh.mpg.rule;

@SuppressWarnings("PublicConstructor")
public final class GameException extends Exception {

    public GameException() {
        super();
    }

    public GameException(String message) {
        super(message);
    }

    public GameException(Throwable cause) {
        super(cause);
    }
}
