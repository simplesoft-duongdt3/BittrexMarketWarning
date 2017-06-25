package com.markert.bittrex.func.main;

import com.sun.istack.internal.NotNull;
import javax.swing.*;

/**
 * Created by admin on 6/25/17.
 */
public class WarningSettingView {

    @NotNull
    private final WarningSettingViewPresenter warningSettingViewPresenter;
    private JTextArea tInputSettingWarning;
    private JCheckBox cbNotification;
    private JCheckBox cbSound;

    public WarningSettingView(@NotNull WarningSettingViewPresenter warningSettingViewPresenter, JButton btSaveListWarning,
                              JTextArea tInputSettingWarning, JCheckBox cbNotification, JCheckBox cbSound) {
        this.warningSettingViewPresenter = warningSettingViewPresenter;
        this.tInputSettingWarning = tInputSettingWarning;
        this.cbNotification = cbNotification;
        this.cbSound = cbSound;

        btSaveListWarning.addActionListener(e -> {
            warningSettingViewPresenter.saveSetting(cbNotification.isSelected(), cbSound.isSelected(), tInputSettingWarning.getText());
        });
    }

    public void render(WarningSettingViewModel warningSettingViewModel) {
        if (warningSettingViewModel != null) {
            tInputSettingWarning.setText(warningSettingViewModel.getWarningSettingText());
            cbNotification.setSelected(warningSettingViewModel.isNotificationEnable());
            cbSound.setSelected(warningSettingViewModel.isSoundEnable());
        }

    }
}
