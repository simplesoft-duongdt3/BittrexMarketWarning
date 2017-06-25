package com.markert.bittrex;

import com.markert.bittrex.func.main.MainFrame;

import javax.swing.*;

/**
 * Created by admin on 6/24/17.
 */
public class App {
    public static void main(String[] args) {
        JFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(320, 220);
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
