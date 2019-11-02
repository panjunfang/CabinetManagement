package com.policeequipment.android.cabinetmanagement;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.Utils;

import org.litepal.LitePal;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

public class AndroidApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
        LitePal.initialize(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SdCardPath")
    public static void writeFile(String sb) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        String datetime = "";
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "
                    + "hh:mm:ss:SSS");
            datetime = tempDate.format(new java.util.Date()).toString();
            fw = new FileWriter("/sdcard/日志.log", true);//

            // 创建FileWriter对象，用来写入字符流
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("/sdcard/日志.log"), StandardCharsets.UTF_8);


            bw = new BufferedWriter(out); // 将缓冲对文件的输出


            String myreadline = datetime + "[]" + sb;


            bw.write(myreadline + "\n"); // 写入文件
            bw.newLine();
            bw.flush(); // 刷新该流的缓冲
            bw.close();
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
