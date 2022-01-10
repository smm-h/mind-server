package ir.smmh.lingu.settings.impl;

import ir.smmh.lingu.Token;
import ir.smmh.lingu.settings.InformalSettings;

import java.util.HashMap;
import java.util.Map;

public class InformalSettingsImpl implements InformalSettings {

    private final Token.Individual type, name;
    private final Map<String, Token.Individual[]> data = new HashMap<>();
    // private final Set<String> referenceKeys = new HashSet<>();


    public InformalSettingsImpl(Token.Individual type, Token.Individual name) {
        this.type = type;
        this.name = name;
    }

    public Token.Individual getName() {
        return name;
    }

    public Token.Individual getType() {
        return type;
    }

    @Override
    public Token.Individual[] get(String key) {
        return data.get(key);
    }

    @Override
    public String getIdentity() {
        String identity = "";

        if (type == null)
            identity += "<untyped>";
        else
            identity += type.getData();

        if (name == null)
            identity += "<unnamed>";
        else
            identity += " '" + name.getData() + "'";

        return identity;
    }

    @Override
    public String toString() {
        return getIdentity();
    }

    // TODO someone should call this
    // beware of null names and types
    // public String getRepresentation() {
    // String string = name + " as " + type + " (";
    // boolean firstTime = true;
    // for (String key : data.keySet()) {
    // if (firstTime) {
    // firstTime = false;
    // } else {
    // string += ", ";
    // }
    // string += key + ": " + data.get(key);
    // }
    // string += ")";
    // return string;
    // }

    @Override
    public boolean sets(String key) {
        return data.containsKey(key);
    }

    @Override
    public void set(String key, Token.Individual... value) {
        data.put(key, value);
    }

    // public void addReferenceKey(String key) {
    // referenceKeys.add(key);
    // }

    // public boolean isReferenceKey(String key) {
    // return referenceKeys.contains(key);
    // }

}
