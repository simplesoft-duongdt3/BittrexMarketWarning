package com.markert.bittrex.common;

import com.google.gson.Gson;
import com.markert.bittrex.pojo.WarningSettingModel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by admin on 6/25/17.
 */
public class SettingPresenter {

    private static final String WARNING_SETTING_FILE = "warning_setting.st";
    public static final String ENCODING = "UTF-8";

    public void writeWarningSettingIntoFile(String warningText) {
        try {
            FileUtils.writeStringToFile(new File(WARNING_SETTING_FILE), warningText, ENCODING);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public WarningSettingModel getWarningSettingFromFile() {
        String warningText = getTextWarningSettingFromFile();
        WarningSettingModel warningSettingModel = null;
        if (warningText != null) {
            warningSettingModel = new Gson().fromJson(warningText, WarningSettingModel.class);
        }
        return warningSettingModel;
    }

    public String getTextWarningSettingFromFile() {
        String warningText = null;
        try {
            warningText = FileUtils.readFileToString(new File(WARNING_SETTING_FILE), ENCODING);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return warningText;
    }

    public void setNotificationStatus(boolean enable) {

    }

    public void setSoundStatus(boolean enable) {

    }

    public boolean getNotificationStatus() {
        return true;
    }

    public boolean getSoundStatus() {
        return true;
    }
}
