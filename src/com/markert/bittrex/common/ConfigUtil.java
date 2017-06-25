package com.markert.bittrex.common;

import com.google.gson.Gson;
import com.markert.bittrex.func.main.WarningSettingViewModel;
import com.markert.bittrex.pojo.WarningSettingModel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by admin on 6/25/17.
 */
public class ConfigUtil {
    public static void saveSetting(boolean notificationEnable, boolean soundEnable, String warningSettingText) {
        Gson gson = new Gson();
        try {
            WarningSettingModel warningSettingModel = gson.fromJson(warningSettingText, WarningSettingModel.class);
            WarningSettingViewModel warningSettingViewModel = new WarningSettingViewModel(notificationEnable, soundEnable, warningSettingModel);
            String json = gson.toJson(warningSettingViewModel);
            FileUtils.write(new File("warning_config"),json, "UTF-8");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static WarningSettingViewModel loadSetting() {
        WarningSettingViewModel warningSettingViewModel = null;
        try {
            String warningText = FileUtils.readFileToString(new File("warning_config"), "UTF-8");
            if (warningText != null) {
                warningSettingViewModel = new Gson().fromJson(warningText, WarningSettingViewModel.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return warningSettingViewModel;
    }
}
