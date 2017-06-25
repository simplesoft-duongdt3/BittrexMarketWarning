package com.markert.bittrex.func.main;

import com.google.gson.Gson;
import com.markert.bittrex.common.*;
import com.markert.bittrex.pojo.MarketSummaryModel;
import com.sun.istack.internal.NotNull;
import okhttp3.*;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by admin on 6/25/17.
 */
public class WarningListViewPresenter implements InitData {
    private AudioPlayer audioPlayer = new AudioPlayer();

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
                    warningListView.render(marketSummaryModel);
                }
                delayRequestNewData();
            }
        });
    }

    public WarningSettingViewModel getWarningSetting() {
        return ConfigUtil.loadSetting();
    }

    public void showNotification(List<MarketSummaryModel.Result> lstResultNotification) {
        if (!audioPlayer.isPlaying()) {
            audioPlayer.play("warning.wav");
        }
        warningNotificationPresenter.showNotification(lstResultNotification);
    }

    @Override
    public void initData() {
        loadDataBittrexAndUpdateUI();
    }

    public void showBrowserMarket(String marketName) {
        Util.showUrlByBrowser(marketName);
    }
}
