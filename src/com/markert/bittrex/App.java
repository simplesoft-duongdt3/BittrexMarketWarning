package com.markert.bittrex;

import com.google.gson.Gson;
import com.markert.bittrex.pojo.MarketSummary;
import okhttp3.*;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by admin on 6/24/17.
 */
public class App {


    public static void main(String[] args) {
        JFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setAlwaysOnTop(true);
        frame.setResizable(true);
        frame.setVisible(true);
    }

    private static void showFrame(MainForm mainForm) {

        /*frame.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {

            }

            public void windowLostFocus(WindowEvent e) {
                frame.setOpacity(0.5f);
            }
        });*/
    }
}
