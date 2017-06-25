package com.markert.bittrex.func.main;

/**
 * Created by admin on 6/25/17.
 */
public class WarningSettingViewModel {
    private boolean notificationEnable;
    private boolean soundEnable;
    private String warningSettingText;

    public WarningSettingViewModel(boolean notificationEnable, boolean soundEnable, String warningSettingModel) {
        this.notificationEnable = notificationEnable;
        this.soundEnable = soundEnable;
        this.warningSettingText = warningSettingModel;
    }

    public boolean isNotificationEnable() {
        return notificationEnable;
    }

    public boolean isSoundEnable() {
        return soundEnable;
    }

    public String getWarningSettingText() {
        return warningSettingText;
    }
}
