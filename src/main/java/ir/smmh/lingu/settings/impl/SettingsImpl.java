package ir.smmh.lingu.settings.impl;

import ir.smmh.lingu.settings.Settings;

public class SettingsImpl implements Settings {

    private final String absoluteName;

    public SettingsImpl(String absoluteName) {
        this.absoluteName = absoluteName;
    }

    @Override
    public String getAbsoluteName() {
        return absoluteName;
    }
}
