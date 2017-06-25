package com.markert.bittrex.common;

import java.awt.*;
import java.net.URI;

/**
 * Created by admin on 6/25/17.
 */
public class Util {
    public static void showUrlByBrowser(String marketName) {
        try {
            URI url = new URI("https://bittrex.com/Market/Index?MarketName=" + marketName);
            Desktop.getDesktop().browse(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
