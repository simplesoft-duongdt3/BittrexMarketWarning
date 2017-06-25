package com.markert.bittrex.pojo;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 6/25/17.
 */
public class WarningSettingModel {
    @SerializedName("lstSettingWarning")
    @Expose
    private List<SettingWarningItem> lstSettingWarning = null;

    public List<SettingWarningItem> getLstSettingWarning() {
        return lstSettingWarning;
    }

    public void setLstSettingWarning(List<SettingWarningItem> lstSettingWarning) {
        this.lstSettingWarning = lstSettingWarning;
    }

    public static class SettingWarningItem {

        @SerializedName("marketName")
        @Expose
        private String marketName;
        @SerializedName("min")
        @Expose
        private String min;
        @SerializedName("max")
        @Expose
        private String max;

        public String getMarketName() {
            return marketName;
        }

        public void setMarketName(String marketName) {
            this.marketName = marketName;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

    }
}
