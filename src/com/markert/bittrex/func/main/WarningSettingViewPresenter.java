package com.markert.bittrex.func.main;

import com.google.gson.Gson;
import com.markert.bittrex.common.InitData;
import com.markert.bittrex.pojo.WarningSettingModel;
import com.sun.istack.internal.NotNull;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by admin on 6/25/17.
 */
public class WarningSettingViewPresenter implements InitData {
    private WarningSettingView warningSettingView;

    public void setWarningSettingView(WarningSettingView warningSettingView) {
        this.warningSettingView = warningSettingView;
    }

    @Override
    public void initData() {
        warningSettingView.render(loadSetting());
    }

    public void saveSetting(boolean notificationEnable, boolean soundEnable, String warningSettingText) {
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

    public WarningSettingViewModel loadSetting() {
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
