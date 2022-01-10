package ir.smmh.lingu.settings.impl;

import ir.smmh.lingu.Token;
import ir.smmh.lingu.settings.FormalizationBlueprint;
import ir.smmh.lingu.settings.Length;

import java.util.HashMap;

public abstract class FormalizationBlueprintImpl implements FormalizationBlueprint {

    private final SettingsFormalizerImpl<?> settingsFormalizer;
    private final String name;
    private final boolean doesNameMatter;
    private final HashMap<String, Integer> keyToIndex = new HashMap<>();
    private final HashMap<Integer, String> indexToKey = new HashMap<>();
    private final HashMap<Integer, Token.Individual[]> defaultValues = new HashMap<>();
    private final HashMap<Integer, Length> validLengths = new HashMap<>();
    private int size = 0;

    public FormalizationBlueprintImpl(SettingsFormalizerImpl<?> settingsFormalizer, String name, boolean doesNameMatter) {
        this.settingsFormalizer = settingsFormalizer;
        this.name = name;
        this.doesNameMatter = doesNameMatter;
        defineBlueprint();
    }

    @Override
    public boolean doesNameMatter() {
        return doesNameMatter;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void add(String key, Token.Individual... defaultValue) {
        add(key, Length.ONE, defaultValue);
    }

    @Override
    public void addRange(String key) {
        add(key, Length.ONE_OR_TWO, settingsFormalizer.DEFAULT_VALUE_NUMERIC_ZERO);
        // int | [int, int]
    }

    @Override
    public void addBoolean(String key, boolean defaultValue) {
        if (defaultValue)
            add(key, settingsFormalizer.DEFAULT_VALUE_BOOLEAN_TRUE);
        else
            add(key, settingsFormalizer.DEFAULT_VALUE_BOOLEAN_FALSE);
    }

    @Override
    public void add(String key, Length length, Token.Individual... defaultValue) {
        key = key.toLowerCase().replaceAll("([ _])", "-");
        indexToKey.put(size, key);
        keyToIndex.put(key, size);
        defaultValues.put(size, defaultValue);
        validLengths.put(size, length);
        size++;
    }

    @Override
    public Token.Individual[] getDefaultValue(int index) {
        return defaultValues.get(index);
    }

    @Override
    public Length getValidLength(int index) {
        return validLengths.get(index);
    }

    @Override
    public String getKey(int index) {
        return indexToKey.get(index);
    }

    @Override
    public int getIndex(String key) {
        return keyToIndex.get(key);
    }

    @Override
    public int size() {
        return size;
    }
}
