package ir.smmh.apps.plotbot.impl;

import ir.smmh.apps.plotbot.Identifier;
import ir.smmh.apps.plotbot.MarkupWriter;
import ir.smmh.apps.plotbot.Operator;
import ir.smmh.apps.plotbot.UserData;
import ir.smmh.lingu.Maker.MakingException;
import ir.smmh.tgbot.impl.UserManagingTelegramBotImpl;
import ir.smmh.util.Map;
import ir.smmh.util.impl.MapImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ir.smmh.util.FunctionalUtil.with;

public class UserDataImpl extends UserManagingTelegramBotImpl.UserData implements UserData {

    private static final MarkupWriter markup = MarkupWriter.getInstance();
    private final Map.SingleValue.Mutable<String, Operator> userDefinedOps = new MapImpl.SingleValue.Mutable<>();
    private final Map.SingleValue.Mutable<String, Identifier> freeVariables = new MapImpl.SingleValue.Mutable<>();
    private boolean darkMode = false;

    public UserDataImpl(long chatId) {
        super(chatId);
    }

    @Override
    public @NotNull Iterable<String> getUserDefinedOperators() {
        return userDefinedOps.overKeys();
    }

    @Override
    public @Nullable Operator getUserDefinedOperator(String name) {
        return userDefinedOps.getAtPlace(name);
    }

    @Override
    public void defineOperator(String name, Operator operator) {
        userDefinedOps.setAtPlace(name, operator);
    }

    @Override
    public boolean forgetUserDefined(String name) {
        boolean existed = userDefinedOps.containsPlace(name) || freeVariables.containsPlace(name);
        userDefinedOps.removeAtPlace(name);
        freeVariables.removeAtPlace(name);
        return existed;
    }

    @Override
    public int forgetAllUserDefined() {
        int count = userDefinedOps.getSize() + freeVariables.getSize();
        userDefinedOps.removeAllPlaces();
        freeVariables.removeAllPlaces();
        return count;
    }

    @Override
    public @Nullable Identifier getVariableIdentifier(String name) {
        return freeVariables.getAtPlace(name);
    }

    @Override
    public @NotNull Identifier newIdentifier(String name) {
        return new IdentifierImpl(name);
    }

    @Override
    public boolean toggleDarkMode() {
        return darkMode = !darkMode;
    }

    @Override
    public boolean isInDarkMode() {
        return darkMode;
    }

    @Override
    public @NotNull Iterable<String> getVariables() {
        return freeVariables.overKeys();
    }

    @Override
    public @Nullable Double getVariableValue(String name) {
        return with(freeVariables.getAtPlace("name"), Identifier::getValue, null);
    }

    private class IdentifierImpl implements Identifier {
        final String name;
        double value = Double.NaN;

        private IdentifierImpl(String name) {
            freeVariables.setAtPlace(name, this);
            this.name = name;
        }

        @Override
        public double evaluate(double x) {
            if (Double.isNaN(value))
                throw new NullPointerException("Free variable is unassigned: " + markup.code(name));
            return value;
        }

        @Override
        public @NotNull String getName() {
            return name;
        }

        @Override
        public double getValue() {
            return value;
        }

        @Override
        public void setValue(double value) {
            this.value = value;
        }

        @Override
        public void bind() throws MakingException {
            if (Double.isNaN(value)) {
                freeVariables.removeAtPlace(name);
            } else {
                throw new MakingException("Cannot bind " + markup.code(name) + " as it has already been used as a free variable");
            }
        }
    }
}
