package com.highstreet.wallet.network.res;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.highstreet.wallet.model.type.Fee;
import com.highstreet.wallet.model.type.Msg;
import com.highstreet.wallet.model.type.Result;

import java.util.ArrayList;
import java.util.List;

public class ResApiTxList {

    @SerializedName("data")
    public ArrayList<Data> data;

    public class Data {
        @SerializedName("id")
        public int id;

        @SerializedName("height")
        public int height;

        @SerializedName("tx_hash")
        public String tx_hash;

        @SerializedName(value="messages", alternate={"msg"})
        public ArrayList<Msg> messages;

        @SerializedName("fee")
        public Fee fee;

        @SerializedName("memo")
        public String memo;

        @SerializedName(value="time", alternate={"timestamp"})
        public String time;

        @SerializedName("logs")
        @Expose
        public Object logs;

        @SerializedName("result")
        public Result result;

        public boolean isSuccess() {
            boolean result = true;
            try {
                Log temp = new Gson().fromJson(new Gson().toJson(logs), Log.class);
                result = temp.success;

            } catch (Exception e) { }

            try {
                ArrayList<Log> temp = new Gson().fromJson(new Gson().toJson(logs), new TypeToken<List<Log>>(){}.getType());
                for (Log log:temp) {
                    if (!TextUtils.isEmpty(log.log)) {
                        result = false;
                        break;
                    }
                }

            } catch (Exception e) { }
            return result;
        }

        public String failMsg() {
            String result = "";
            try {
                Log temp = new Gson().fromJson(new Gson().toJson(logs), Log.class);
                result = temp.log;

            } catch (Exception e) { }

            try {
                ArrayList<Log> temp = new Gson().fromJson(new Gson().toJson(logs), new TypeToken<List<Log>>(){}.getType());
                for (Log log:temp) {
                    if(!log.success) {
                        result = log.log;
                        break;
                    }
                }

            } catch (Exception e) { }
            return result.replace(" ", "\u00A0");
        }

    }


    public class Log {
        @SerializedName("msg_index")
        public String msg_index;

        @SerializedName("success")
        public boolean success;

        @SerializedName("log")
        public String log;
    }
}
