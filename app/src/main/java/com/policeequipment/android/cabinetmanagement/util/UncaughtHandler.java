package com.policeequipment.android.cabinetmanagement.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.policeequipment.android.cabinetmanagement.AndroidApp;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

public class UncaughtHandler implements Thread.UncaughtExceptionHandler {

    /**
     * 是否开启Log输出,在Debug状态下开启,
     * 在Release状态下关闭以提示程序性能
     */
    public static final boolean DEBUG = true;
    /*** Debug Log Tag */
    public static final String TAG = "CrashCatchHandler";
    private static final String VERSION_NAME = "VERSION_NAME";
    private static final String VERSION_CODE = "VERSION_CODE";
    private static final String EXCEPTION = "EXCEPTION";
    /*** 错误报告文件的扩展名 */
    private static final String CRASH_REPORTER_PREFIX = "crash";
    private static final String CRASH_REPORTER_EXTENSION = ".log";
    /*** 关闭崩溃程序倒计时 */
    private static final int TIME = 2000;
    /*** 存放日志文件的文件夹名称 */
    private static final String PATH = "/log";
    /*** 日志名称时间模式 */
    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /*** 全局上下文 */
    private Context ctx;
    /*** 单例饿汉模式 */
    private static final UncaughtHandler INSTANCE = new UncaughtHandler();
    /*** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefHandler;
    /*** 使用Properties来保存设备的信息和错误堆栈信息 */
    private java.util.Properties mProperties = new java.util.Properties();
    /*** 存放路径 */
    private String rootPath;

    private UncaughtHandler() {
    }

    public static UncaughtHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param cxt
     */
    public void init(Context cxt) {
        this.ctx = cxt;
        // 获取系统默认的UncaughtException处理器
        mDefHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置当前CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        // 日志存放路径
        rootPath = getRootPath() + PATH;
    }

    private String getRootPath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            // 外部内存可用
            return ctx.getExternalCacheDir().getAbsolutePath();
        } else {
            // 外部内存不可用(需要Root)
            return ctx.getCacheDir().getPath();
        }
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(TIME);
            } catch (InterruptedException e) {
                if (DEBUG) {
                    Log.e(TAG, "Error :", e);
                }
            }
            // 杀死进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param e
     * @return 如果处理了该异常信息, 返回true;否则返回false.
     */
    private boolean handleException(Throwable e) {
        if (e == null) {
            if (DEBUG) {
                Log.w(TAG, "handleException-ex == null");
            }
            return false;
        }
        final String msg = e.getLocalizedMessage();
        if (msg == null) {
            return false;
        }
        // toast显示提醒
        toastShowMsg("程序异常，即将退出:\r\n", msg);
        // 收集设备参数信息
        collectPhoneInfo();
        // 保存日志文件
        String s = dumpExceptionToSDCard(e);
        Log.e(TAG, "handleException: " + s);
        // 发送错误报告到服务器
        //        uploadCrashReportsToServer();
        return true;
    }

    /**
     * 把错误报告发送给服务器,包含新产生的和以前没发送的.
     */
    private void uploadCrashReportsToServer() {
        File dir = new File(rootPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                for (File file : files) {
                    if (file.exists() && file.isFile()) {
                        postReport(file);
                    }
                }
            }
        }
    }

    /**
     * 异步发送异常报告到服务器
     *
     * @param file
     */
    private void postReport(File file) {
        // TODO 异步发送异常报告到服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                file.delete();// 发送之后删除
            }
        }).start();
    }

    /**
     * 收集设备信息
     */
    public void collectPhoneInfo() {
        PackageManager pm = ctx.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                mProperties.put(VERSION_NAME,
                        pi.versionName == null ? "null" : pi.versionName);
                mProperties.put(VERSION_CODE, Integer.toString(pi.versionCode));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // 获得某个类的所有申明的字段，即包括public、private和proteced，
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            // 反射 ,获取私有的信息;类中的成员变量为private,故必须进行此操作
            field.setAccessible(true);
            try {
                mProperties.put(field.getName(), String.valueOf(field.get(null)));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导出异常信息到SD卡中
     *
     * @param e
     * @return 返回文件名称
     */
    @SuppressLint("CommitPrefEdits")
    private String dumpExceptionToSDCard(Throwable e) {
        /* 获得错误信息字符串 */
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String result = sw.toString();
//        mProperties.put(EXCEPTION, result);
//        try {
//            sw.close();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        pw.close();
        /* 写入文件 */
        AndroidApp.writeFile(result);

//        // 日志生成时间
//        String time = new java.text.SimpleDateFormat(TIME_PATTERN, Locale.US)
//                .format(new java.util.Date());
//        // 文件名称
//        StringBuilder crashFileName = new StringBuilder();
//        crashFileName.append("/").append(CRASH_REPORTER_PREFIX)
//                .append(time).append(CRASH_REPORTER_EXTENSION);
//        String cfn = crashFileName.toString();
//
//        File dir = new File(rootPath);
//        if (!dir.exists()) {
//            dir.mkdirs();
//            if (dir.canWrite()) {
//                return writtenToSDCard(cfn);
//            } else {
//                toastShowMsg("unable to write to log file", null);
//            }
//        } else if (dir.canWrite()) {
//            return writtenToSDCard(cfn);
//        } else {
//            toastShowMsg("unable to write to log file", null);
//        }
//        return null;
        return "";
    }

    /**
     * 写入SD卡中
     *
     * @param fileName
     * @return 写入成功，返回文件名
     */
    public String writtenToSDCard(String fileName) {
        File file = new File(rootPath + fileName);
        try {
            file.createNewFile();
            if (file.exists() && file.canWrite()) {
                FileOutputStream fos = new FileOutputStream(file, false);
                mProperties.store(fos, "");
                fos.flush();
                fos.close();

                // TODO 可以将文件名写入sharedPreferences中，方便下一次打开程序时操作日志
                    /*SharedPreferences.Editor editor = mContext
                            .getSharedPreferences("log").edit();
                    editor.putString("lastCrashFileName", crashFileName);
                    editor.commit();*/
                return fileName;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用Toast显示异常信息
     *
     * @param describe
     * @param msg
     */
    private void toastShowMsg(String describe, String msg) {
        new Thread() {
            public void run() {
                StringBuffer content = new StringBuffer();
                content.append(describe);
                content.append("\r\n");
                if (msg != null)
                    content.append(msg);
                Looper.prepare();
                Toast toast = Toast.makeText(ctx, content.toString(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Looper.loop();
            }
        }.start();
    }
}