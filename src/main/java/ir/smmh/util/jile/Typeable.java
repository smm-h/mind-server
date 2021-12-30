package ir.smmh.util.jile;

public interface Typeable {

    void type(char character);

    void pressBackspace();

    int getCaret();

    void setCaret(int position);

    default void pressDelete() {
        moveCaret(+1);
        pressBackspace();
    }

    default void pressEnter() {
        type('\n');
    }

    default void pressSpace() {
        type(' ');
    }

    default void pressEscape() {
        clearCaret();
    }

    default void moveCaret(int relativePosition) {
        setCaret(getCaret() + relativePosition);
    }

    default void clearCaret() {
        setCaret(-1);
    }
}