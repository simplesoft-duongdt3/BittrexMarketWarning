package com.markert.bittrex;

import com.google.gson.Gson;
import com.markert.bittrex.pojo.MarketSummary;
import com.markert.bittrex.pojo.SettingWarning;
import com.notification.Notification;
import com.notification.NotificationFactory;
import com.notification.NotificationListener;
import com.notification.NotificationManager;
import com.notification.manager.QueueManager;
import com.notification.manager.SimpleManager;
import com.notification.manager.SlideManager;
import com.notification.types.TextNotification;
import com.theme.ThemePackagePresets;
import com.utils.Time;
import okhttp3.*;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by admin on 6/25/17.
 */
public class MainFrame extends JFrame {
    static final Vector<String> columns = new Vector<>();
    private DefaultTableModel tableModel;

    public static class BittrexTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus, int row, int col) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            boolean status1 = (boolean) table.getModel().getValueAt(row, 2);
            boolean status2 = (boolean) table.getModel().getValueAt(row, 3);
            if (status1 || status2) {
                setForeground(Color.RED);
            } else {
                setForeground(table.getForeground());
            }
            return this;
        }
    }

    public static class BittrexRightTableCellRenderer extends BittrexTableCellRenderer {
        protected  BittrexRightTableCellRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }
    }

    //headers for the table
    static {
        columns.add("Market");
        columns.add("Last");
        columns.add("W Up");
        columns.add("W Down");
    }

    static final Class[] columnClass = new Class[]{
            String.class, String.class, Boolean.class, Boolean.class
    };

    private static final OkHttpClient client = new OkHttpClient();
    private static final NotificationFactory notificationFactory = new NotificationFactory(ThemePackagePresets.cleanLight());
    private static final NotificationManager notificationManager = new SimpleManager(NotificationFactory.Location.NORTHEAST);
    private static final QueueManager queueManager = new QueueManager(NotificationFactory.Location.NORTHEAST);
    private static final SlideManager slideManager = new SlideManager(NotificationFactory.Location.SOUTHEAST);

    @Override
    protected void frameInit() {
        super.frameInit();
        customInit();
    }

    private void customInit() {
        MainForm mainForm = new MainForm();
        mainForm.btSaveListWarning.addActionListener(e -> writeWarningSettingIntoFile(mainForm));

        Vector<Vector<Object>> vectors = new Vector<>();
        //create table model with data
        tableModel = new DefaultTableModel(vectors, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnClass[columnIndex];
            }
        };
        mainForm.tableData.setModel(tableModel);
        mainForm.tableData.setDefaultRenderer(Object.class, new BittrexTableCellRenderer());
        mainForm.tableData.getColumnModel().getColumn(1).setMinWidth(100);
        mainForm.tableData.getColumnModel().getColumn(1).setCellRenderer(new BittrexRightTableCellRenderer());
        this.setContentPane(mainForm.mainPanel);

        loadWarningSettingFromFile(mainForm.tInputSettingWarning);
        loadDataBittrexAndUpdateUI(mainForm.tableData);
    }

    void showNotification(String title, String content) {
        TextNotification notification = notificationFactory.buildTextNotification(title, content);
        notification.setCloseOnClick(true);
        notification.addNotificationListener(new NotificationListener() {
            @Override
            public void actionCompleted(Notification notification, String action) {
                if ("clicked".equals(action)) {
                    if (notification instanceof TextNotification) {
                        String content = ((TextNotification) notification).getSubtitle();

                        String[] strings = content.split(",");
                        for (String marketName : strings) {
                            try {
                                URI url = new URI("https://bittrex.com/Market/Index?MarketName=" + marketName);
                                Desktop.getDesktop().browse(url);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        //slideManager.setSlideDirection(SlideManager.SlideDirection.WEST);
        notificationManager.addNotification(notification, Time.seconds(3));
    }

    private void writeWarningSettingIntoFile(MainForm mainForm) {
        try {
            FileUtils.writeStringToFile(new File("warning_setting.st"), mainForm.tInputSettingWarning.getText(), "UTF-8");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private SettingWarning getWarningSettingFromFile() {
        SettingWarning settingWarning = null;
        try {
            String string = FileUtils.readFileToString(new File("warning_setting.st"), "UTF-8");
            settingWarning = new Gson().fromJson(string, SettingWarning.class);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return settingWarning;
    }

    private void loadWarningSettingFromFile(JTextArea jTextArea) {
        try {
            String string = FileUtils.readFileToString(new File("warning_setting.st"), "UTF-8");
            jTextArea.setText(string);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void loadDataBittrexAndUpdateUI(JTable tableData) {
        System.out.printf("loadDataBittrexAndUpdateUI");
        Request request = new Request.Builder()
                .url("https://bittrex.com/api/v1.1/public/getmarketsummaries")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                delayRequestNewData(tableData);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MarketSummary marketSummary = new Gson().fromJson(response.body().charStream(), MarketSummary.class);
                if (marketSummary.isSuccess()) {
                    renderNewData(marketSummary, tableData);
                }

                delayRequestNewData(tableData);
            }
        });
    }

    private void delayRequestNewData(JTable tableData) {
        Timer timer = new Timer(3000, e -> loadDataBittrexAndUpdateUI(tableData));
        timer.setRepeats(false);
        timer.start();
    }

    private void renderNewData(MarketSummary marketSummary, JTable tableData) {
        HashMap<String, SettingWarning.SettingWarningItem> mapSetting = new HashMap<>();
        SettingWarning warningSettingFromFile = getWarningSettingFromFile();
        if (warningSettingFromFile != null) {
            for (SettingWarning.SettingWarningItem settingWarningItem : warningSettingFromFile.getLstSettingWarning()) {
                mapSetting.put(settingWarningItem.getMarketName(), settingWarningItem);
            }
        }

        boolean needCheckSettingWarning = mapSetting.size() > 0;

        boolean needNotification = false;
        StringBuilder warningListMarket = new StringBuilder();
        Vector<Vector<Object>> vectors = new Vector<>();
        for (MarketSummary.Result result : marketSummary.getResult()) {
            Vector<Object> objects = new Vector<>();
            objects.add(result.getMarketName());
            objects.add(result.getLast());

            if (needCheckSettingWarning) {
                SettingWarning.SettingWarningItem settingWarningItem = mapSetting.get(result.getMarketName());
                if (settingWarningItem != null) {
                    vectors.add(objects);
                    boolean isUpWarning = Double.valueOf(result.getLast()) >= Double.valueOf(settingWarningItem.getMax());
                    objects.add(isUpWarning);
                    boolean isDownWarning = Double.valueOf(result.getLast()) <= Double.valueOf(settingWarningItem.getMin());
                    objects.add(isDownWarning);

                    if (isUpWarning || isDownWarning) {
                        if (warningListMarket.length() != 0) {
                            warningListMarket.append(",");
                        }
                        warningListMarket.append(result.getMarketName());
                        needNotification = true;
                    }
                }
            } else {
                objects.add(false);
                objects.add(false);
                vectors.add(objects);
            }
        }
        if (needNotification) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            String time = simpleDateFormat.format(new Date());
            showNotification("Warning " + time, warningListMarket.toString());
        }
        tableModel.setDataVector(vectors, columns);
        tableData.getColumnModel().getColumn(1).setMinWidth(140);
        tableData.getColumnModel().getColumn(1).setCellRenderer(new BittrexRightTableCellRenderer());
    }
}
