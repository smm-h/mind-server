package ir.smmh.apps.plotbot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface UserData extends ir.smmh.tgbot.UserData {
    @NotNull Iterable<String> getUserDefinedOperators();

    @Nullable Operator getUserDefinedOperator(String name);

    @NotNull Iterable<String> getVariables();

    @Nullable Double getVariableValue(String name);

    void defineOperator(String name, Operator operator);

    boolean forgetUserDefined(String name);

    int forgetAllUserDefined();

    @Nullable Identifier getVariableIdentifier(String name);

    @NotNull Identifier newIdentifier(String name);

    boolean toggleDarkMode();

    boolean isInDarkMode();
}
