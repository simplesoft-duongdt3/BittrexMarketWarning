package com.markert.bittrex.common;

import com.markert.bittrex.pojo.MarketSummaryModel;
import com.notification.NotificationFactory;
import com.notification.NotificationManager;
import com.notification.manager.SimpleManager;
import com.notification.types.TextNotification;
import com.sun.istack.internal.NotNull;
import com.theme.ThemePackagePresets;
import com.utils.Time;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by admin on 6/25/17.
 */
public class WarningNotificationPresenter {
    @NotNull
    private final NotificationFactory notificationFactory = new NotificationFactory(ThemePackagePresets.cleanLight());
    @NotNull
    private final NotificationManager notificationManager = new SimpleManager(NotificationFactory.Location.NORTHEAST);

    public void showNotification(@NotNull List<MarketSummaryModel.Result> results) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        String time = simpleDateFormat.format(new Date());
        String titleWarning = "Warning " + time;
        StringBuilder warningListResult = new StringBuilder();
        for (MarketSummaryModel.Result result : results) {
            if (warningListResult.length() != 0) {
                warningListResult.append(",");
            }
            warningListResult.append(result.getMarketName());
        }

        TextNotification notification = notificationFactory.buildTextNotification(titleWarning, warningListResult.toString());
        notification.setCloseOnClick(true);
        notification.addNotificationListener((notification1, action) -> {
            if ("clicked".equals(action)) {
                if (notification1 instanceof TextNotification) {
                    String content1 = ((TextNotification) notification1).getSubtitle();

                    String[] strings = content1.split(",");
                    for (String marketName : strings) {
                        Util.showUrlByBrowser(marketName);
                    }
                }
            }
        });
        //slideManager.setSlideDirection(SlideManager.SlideDirection.WEST);
        notificationManager.addNotification(notification, Time.seconds(3));
    }

}
