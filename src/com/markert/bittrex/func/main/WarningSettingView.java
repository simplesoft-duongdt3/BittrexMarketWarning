package com.markert.bittrex.func.main;

import com.sun.istack.internal.NotNull;
import javax.swing.*;

/**
 * Created by admin on 6/25/17.
 */
public class WarningSettingView {

    @NotNull
    private final WarningSettingViewPresenter warningSettingViewPresenter;
    private JButton btSaveListWarning;
    private JTextArea tInputSettingWarning;
    private JCheckBox cbNotification;
    private JCheckBox cbSound;

    public WarningSettingView(@NotNull WarningSettingViewPresenter warningSettingViewPresenter, JButton btSaveListWarning, JTextArea tInputSettingWarning, JCheckBox cbNotification, JCheckBox cbSound) {
        this.warningSettingViewPresenter = warningSettingViewPresenter;
        this.btSaveListWarning = btSaveListWarning;
        this.tInputSettingWarning = tInputSettingWarning;
        this.cbNotification = cbNotification;
        this.cbSound = cbSound;

        btSaveListWarning.addActionListener(e -> {
            WarningSettingViewModel warningSettingViewModel = new WarningSettingViewModel(cbSound.isSelected(), cbNotification.isSelected(), tInputSettingWarning.getText());
            warningSettingViewPresenter.saveSetting(warningSettingViewModel);
        });
    }

    public void render(WarningSettingViewModel warningSettingViewModel) {
        tInputSettingWarning.setText(warningSettingViewModel.getWarningSettingText());
        cbNotification.setSelected(warningSettingViewModel.isNotificationEnable());
        cbSound.setSelected(warningSettingViewModel.isSoundEnable());
    }
}
