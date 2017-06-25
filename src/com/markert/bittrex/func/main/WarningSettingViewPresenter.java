package com.markert.bittrex.func.main;

import com.markert.bittrex.common.InitData;
import com.markert.bittrex.common.SettingPresenter;
import com.sun.istack.internal.NotNull;

/**
 * Created by admin on 6/25/17.
 */
public class WarningSettingViewPresenter implements InitData {
    @NotNull
    private final SettingPresenter settingPresenter = new SettingPresenter();
    private WarningSettingView warningSettingView;

    public void setWarningSettingView(WarningSettingView warningSettingView) {
        this.warningSettingView = warningSettingView;
    }

    @Override
    public void initData() {
        String warningSettingText = settingPresenter.getTextWarningSettingFromFile();
        boolean soundStatus = settingPresenter.getSoundStatus();
        boolean notificationStatus = settingPresenter.getNotificationStatus();

        WarningSettingViewModel warningSettingViewModel = new WarningSettingViewModel(notificationStatus, soundStatus, warningSettingText);
        warningSettingView.render(warningSettingViewModel);
    }

    public void saveSetting(WarningSettingViewModel warningSettingViewModel) {
        settingPresenter.writeWarningSettingIntoFile(warningSettingViewModel.getWarningSettingText());
        settingPresenter.setNotificationStatus(warningSettingViewModel.isNotificationEnable());
        settingPresenter.setSoundStatus(warningSettingViewModel.isSoundEnable());
    }
}
