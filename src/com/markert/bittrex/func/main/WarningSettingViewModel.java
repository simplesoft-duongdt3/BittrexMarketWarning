package com.markert.bittrex.func.main;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.markert.bittrex.pojo.WarningSettingModel;

/**
 * Created by admin on 6/25/17.
 */
public class WarningSettingViewModel {
    @SerializedName("notificationEnable")
    @Expose
    private boolean notificationEnable;
    @SerializedName("soundEnable")
    @Expose
    private boolean soundEnable;
    @SerializedName("warningSettingModel")
    @Expose
    private WarningSettingModel warningSettingModel;

    public WarningSettingViewModel(boolean notificationEnable, boolean soundEnable, WarningSettingModel warningSettingModel) {
        this.notificationEnable = notificationEnable;
        this.soundEnable = soundEnable;
        this.warningSettingModel = warningSettingModel;
    }

    public boolean isNotificationEnable() {
        return notificationEnable;
    }

    public boolean isSoundEnable() {
        return soundEnable;
    }

    public WarningSettingModel getWarningSettingModel() {
        return warningSettingModel;
    }

    public String getWarningSettingText() {
        return new Gson().toJson(warningSettingModel);
    }
}
