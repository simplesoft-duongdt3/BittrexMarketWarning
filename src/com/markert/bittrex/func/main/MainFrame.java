package com.markert.bittrex.func.main;

import com.markert.bittrex.common.InitData;
import com.markert.bittrex.MainForm;
import com.markert.bittrex.common.WarningNotificationPresenter;

import javax.swing.*;

/**
 * Created by admin on 6/25/17.
 */
public class MainFrame extends JFrame implements InitData {
    private WarningListViewPresenter warningListPresenter;
    private WarningSettingViewPresenter warningSettingViewPresenter;
    @Override
    protected void frameInit() {
        super.frameInit();
        customInit();
    }

    private void customInit() {
        warningListPresenter = new WarningListViewPresenter();
        warningSettingViewPresenter = new WarningSettingViewPresenter();

        MainForm mainForm = new MainForm();
        WarningListView warningListView = new WarningListView(mainForm.tableData, warningListPresenter);
        warningListPresenter.setWarningNotificationPresenter(new WarningNotificationPresenter());
        warningListPresenter.setWarningListView(warningListView);

        WarningSettingView warningSettingView = new WarningSettingView(warningSettingViewPresenter, mainForm.btSaveListWarning,
                mainForm.tInputSettingWarning, mainForm.cbNotification, mainForm.cbSound);
        warningSettingViewPresenter.setWarningSettingView(warningSettingView);
        this.setContentPane(mainForm.mainPanel);
        initData();
    }

    @Override
    public void initData() {
        warningSettingViewPresenter.initData();
        warningListPresenter.initData();
    }
}
