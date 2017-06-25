package com.markert.bittrex.func.main;

import com.google.gson.Gson;
import com.markert.bittrex.common.*;
import com.markert.bittrex.pojo.MarketSummaryModel;
import com.markert.bittrex.pojo.WarningSettingModel;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import okhttp3.*;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by admin on 6/25/17.
 */
public class WarningListViewPresenter implements InitData {
    private AudioPlayer audioPlayer = new AudioPlayer();
    private WarningSettingViewModel warningSettingViewModel;
    @NotNull
    private final OkHttpClient client = new OkHttpClient();
    private WarningListView warningListView;

    private WarningNotificationPresenter warningNotificationPresenter;

    public void setWarningListView(WarningListView warningListView) {
        this.warningListView = warningListView;
    }

    public void setWarningNotificationPresenter(WarningNotificationPresenter warningNotificationPresenter) {
        this.warningNotificationPresenter = warningNotificationPresenter;
    }

    private void delayRequestNewData() {
        Timer timer = new Timer(3000, e -> loadDataBittrexAndUpdateUI());
        timer.setRepeats(false);
        timer.start();
    }

    private void loadDataBittrexAndUpdateUI() {
        Request request = new Request.Builder()
                .url("https://bittrex.com/api/v1.1/public/getmarketsummaries")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                delayRequestNewData();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MarketSummaryModel marketSummaryModel = new Gson().fromJson(response.body().charStream(), MarketSummaryModel.class);
                if (marketSummaryModel.isSuccess()) {
                    warningSettingViewModel = ConfigUtil.loadSetting();
                    warningListView.render(marketSummaryModel);
                }
                delayRequestNewData();
            }
        });
    }

    private WarningSettingViewModel getWarningSettingViewModel() {
        if (warningSettingViewModel == null) {
            warningSettingViewModel = ConfigUtil.loadSetting();
        }
        return warningSettingViewModel;
    }

    public void showNotification(List<MarketSummaryModel.Result> lstResultNotification) {
        WarningSettingViewModel warningSetting = getWarningSettingViewModel();
        if (warningSetting != null) {
            if (warningSetting.isSoundEnable()) {
                if (!audioPlayer.isPlaying()) {
                    audioPlayer.play("warning.wav");
                }
            }
            if (warningSetting.isNotificationEnable()) {
                warningNotificationPresenter.showNotification(lstResultNotification);
            }
        }
    }

    @Override
    public void initData() {
        loadDataBittrexAndUpdateUI();
    }

    public void showBrowserMarket(String marketName) {
        Util.showUrlByBrowser(marketName);
    }

    @Nullable
    public WarningSettingModel getWarningSettingModel() {
        WarningSettingViewModel warningSetting = getWarningSettingViewModel();
        WarningSettingModel warningSettingModel = null;
        if (warningSetting != null) {
            warningSettingModel = warningSetting.getWarningSettingModel();
        }
        return warningSettingModel;
    }
}
