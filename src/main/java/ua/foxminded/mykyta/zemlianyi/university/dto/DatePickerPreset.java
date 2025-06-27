package ua.foxminded.mykyta.zemlianyi.university.dto;

public enum DatePickerPreset {
    TODAY("Today"), THIS_WEEK("ThisWeek"), NEXT_WEEK("NextWeek"), THIS_MONTH("ThisMonth"), CUSTOM("Custom");

    private final String preset;

    DatePickerPreset(String preset) {
        this.preset = preset;
    }

    public String getPreset() {
        return preset;
    }
}
