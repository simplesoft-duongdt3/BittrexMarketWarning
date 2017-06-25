package com.markert.bittrex.func.main;

import com.markert.bittrex.common.ConfigUtil;
import com.markert.bittrex.pojo.MarketSummaryModel;
import com.markert.bittrex.pojo.WarningSettingModel;
import com.sun.istack.internal.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by admin on 6/25/17.
 */
public class WarningListView {
    @NotNull
    private JTable table;
    private DefaultTableModel tableModel;

    @NotNull
    private final Vector<String> columns = new Vector<>();
    @NotNull
    private final Class[] columnClass = new Class[] {
            String.class, String.class, String.class, Boolean.class, Boolean.class
    };

    @NotNull
    private final WarningListViewPresenter warningListPresenter;

    public WarningListView(@NotNull JTable table, @NotNull WarningListViewPresenter warningListPresenter) {
        this.table = table;
        this.warningListPresenter = warningListPresenter;

        initHeaderList();
        initTable();
    }

    private void initTable() {
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
        table.setModel(tableModel);
        table.setDefaultRenderer(Object.class, new BittrexTableCellRenderer());
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 0) {
                    String marketName = (String) tableModel.getValueAt(row, col);
                    warningListPresenter.showBrowserMarket(marketName);
                }
            }
        });
        Dimension dim = new Dimension(4,1);
        table.setIntercellSpacing(new Dimension(dim));
        int height = table.getRowHeight();
        table.setRowHeight(height + 10);
    }

    private void initHeaderList() {
        columns.clear();
        columns.add("Market");
        columns.add("Last");
        columns.add("%");
        columns.add("Up");
        columns.add("Down");
    }

    public void render(MarketSummaryModel marketSummaryModel) {
        HashMap<String, WarningSettingModel.SettingWarningItem> mapSetting = new HashMap<>();
        WarningSettingModel warningSettingModel = warningListPresenter.getWarningSettingModel();
        if (warningSettingModel != null) {
            List<WarningSettingModel.SettingWarningItem> lstSettingWarning = warningSettingModel.getLstSettingWarning();
            for (WarningSettingModel.SettingWarningItem settingWarningItem : lstSettingWarning) {
                mapSetting.put(settingWarningItem.getMarketName(), settingWarningItem);
            }
        }
        boolean needCheckSettingWarning = mapSetting.size() > 0;
        boolean needNotification = false;
        Vector<Vector<Object>> vectors = new Vector<>();
        List<MarketSummaryModel.Result> lstResultNotification = new ArrayList<>();
        for (MarketSummaryModel.Result result : marketSummaryModel.getResult()) {
            Vector<Object> objects = new Vector<>();
            objects.add(result.getMarketName());
            objects.add(result.getLast());

            Double preDayAmount = Double.valueOf(result.getPrevDay());
            double amountChangePreDay = Double.valueOf(result.getLast()) - preDayAmount;
            double percentChangePreDay = (amountChangePreDay / preDayAmount) * 100;
            String textAmountChangePreDay = ((int) percentChangePreDay) + "";
            objects.add(textAmountChangePreDay);

            if (needCheckSettingWarning) {
                WarningSettingModel.SettingWarningItem settingWarningItem = mapSetting.get(result.getMarketName());
                if (settingWarningItem != null) {
                    vectors.add(objects);
                    boolean isUpWarning = Double.valueOf(result.getLast()) >= Double.valueOf(settingWarningItem.getMax());
                    objects.add(isUpWarning);
                    boolean isDownWarning = Double.valueOf(result.getLast()) <= Double.valueOf(settingWarningItem.getMin());
                    objects.add(isDownWarning);

                    if (isUpWarning || isDownWarning) {
                        lstResultNotification.add(result);
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
            warningListPresenter.showNotification(lstResultNotification);
        }
        tableModel.setDataVector(vectors, columns);
        table.getColumnModel().getColumn(0).setMinWidth(80);
        table.getColumnModel().getColumn(1).setMinWidth(100);
        table.getColumnModel().getColumn(2).setMinWidth(30);
        table.getColumnModel().getColumn(1).setCellRenderer(new BittrexRightTableCellRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new BittrexRightTableCellRenderer());
    }

    private static class BittrexTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus, int row, int col) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            String change = (String) table.getModel().getValueAt(row, 2);
            if (Integer.valueOf(change) <= 0) {
                setForeground(Color.RED);
            } else {
                setForeground(Color.BLUE);
            }
            /*boolean status1 = (boolean) table.getModel().getValueAt(row, 3);
            boolean status2 = (boolean) table.getModel().getValueAt(row, 4);
            if (status1 || status2) {
                setForeground(Color.RED);
            } else {
                setForeground(table.getForeground());
            }*/
            return this;
        }
    }

    private static class BittrexRightTableCellRenderer extends BittrexTableCellRenderer {
        protected  BittrexRightTableCellRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }
    }
}
