package com.policeequipment.android.cabinetmanagement.service;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.deemons.serialportlib.ByteUtils;
import com.deemons.serialportlib.SerialPort;
import com.policeequipment.android.cabinetmanagement.util.SPKey;
import com.policeequipment.android.cabinetmanagement.bean.BoxStatus;
import com.policeequipment.android.cabinetmanagement.bean.DoorNumberInFo;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
import static com.policeequipment.android.cabinetmanagement.util.SPKey.QueryLock1;
import static com.policeequipment.android.cabinetmanagement.util.SPKey.QueryLock2;
import static com.policeequipment.android.cabinetmanagement.util.StringUtil.getBoxStatusValue;
import static com.policeequipment.android.cabinetmanagement.util.StringUtil.initListener;

public class SerialPortPresenter implements SerialPortContract.IPresenter {
    private SerialPortContract.IView view;
    private int boardNumber1 = 1;
    private int boardNumber2 = 2;
    private List<String> keyList1_8;
    private List<String> keyList9_16;
    private List<BoxStatus> boxStatusList1_8;
    private List<BoxStatus> boxStatusList9_16;
    private List<String> key = new ArrayList<>();
    private boolean isAllOpen;

    /**
     * 门箱
     */
    private String mPath;
    private int    mBaudRate;
    private int    mCheckDigit;
    private int    mDataBits;
    private int    mStopBit;
    private SerialPort mSerialPort_box;
    private boolean isInterrupted_box;
    private ObservableEmitter<byte[]> mEmitter;
    private Disposable subscribe_box;
    private Disposable mReceiveDisposable;


    /**
     * ic
     */
    private String mPath_IC;
    private int    mBaudRate_IC;
    private int    mCheckDigit_IC;
    private int    mDataBits_IC;
    private int    mStopBit_IC;
    private SerialPort mSerialPort_ic;
    private boolean isInterrupted_ic;
    private boolean isRegistered_ic = false;
    private ObservableEmitter<byte[]> mEmitter_ic;
    private Disposable subscribe_ic;
    private boolean isICstaue = false;
    private Disposable receiveDisposable_ic;


    private String mPath_x86;
    private int    mBaudRate_x86;
    private int    mCheckDigit_x86;
    private int    mDataBits_x86;
    private int    mStopBit_x86;
    private SerialPort mSerialPort_x86;
    private boolean isInterrupted_x86;
    private Disposable subscribe_x86;
    private Disposable receivesubscribe_x86;
    private ObservableEmitter<byte[]> mEmitter_x86;


    public SerialPortPresenter(SerialPortContract.IView view) {
        this.view = view;
        boxStatusList1_8 = new ArrayList<>();
        boxStatusList9_16 = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            boxStatusList1_8.add(new BoxStatus(i, false, false));
        }
        for (int i = 8; i < 16; i++) {
            boxStatusList9_16.add(new BoxStatus(i, false, false));
        }
        if (view != null) {
            view.getData(boxStatusList1_8, boxStatusList9_16);
        }
        setData();
        refreshValueFormSp();
    }

    @Override
    public void setData() {
        List<String> strings = Arrays.asList(SPKey.InstructionSet);
        key.clear();
        boardNumber1 = SPUtils.getInstance().getInt(SPKey.BoardNumber1, 1);
        boardNumber2 = SPUtils.getInstance().getInt(SPKey.BoardNumber2, 2);
        LogUtils.i(boardNumber1);
        LogUtils.i(boardNumber2);

        keyList1_8 = new ArrayList<>();
        keyList9_16 = new ArrayList<>();
        if (boardNumber1 == 1) {
            for (int i = 0; i < 8; i++) {
                keyList1_8.add(strings.get(i));

            }
        } else if (boardNumber1 == 2) {
            for (int i = 8; i < 16; i++) {
                keyList1_8.add(strings.get(i));

            }

        }

        if (boardNumber2 == 1) {
            for (int i = 0; i < 8; i++) {
                keyList9_16.add(strings.get(i));

            }
        } else if (boardNumber2 == 2) {
            for (int i = 8; i < 16; i++) {
                keyList9_16.add(strings.get(i));

            }
        }
//        if (boardNumber1 == 1) {
//            key.addAll(keyList1_8);
//            key.addAll(keyList9_16);
//        } else if (boardNumber2 == 1) {
//            key.addAll(keyList9_16);
//            key.addAll(keyList1_8);
//        }
        key.addAll(keyList1_8);
        key.addAll(keyList9_16);

        int i = 1;
        for (String s : key) {
            Log.e(TAG, "setData: "+i+"::" + s + "\n");
            i++;
        }

    }


    @Override
    public void refreshValueFormSp() {
        mPath = SPUtils.getInstance().getString(SPKey.SERIAL_PORT, "");
        mBaudRate = Integer.parseInt(SPUtils.getInstance().getString(SPKey.BAUD_RATE, "115200"));
        mCheckDigit = Integer.parseInt(SPUtils.getInstance().getString(SPKey.CHECK_DIGIT, "0"));
        mDataBits = Integer.parseInt(SPUtils.getInstance().getString(SPKey.DATA_BITS, "8"));
        mStopBit = Integer.parseInt(SPUtils.getInstance().getString(SPKey.STOP_BIT, "1"));

        mPath_IC = SPUtils.getInstance().getString(SPKey.SERIAL_PORT_IC, "");
        mBaudRate_IC = Integer.parseInt(SPUtils.getInstance().getString(SPKey.BAUD_RATE_IC, "9600"));
        mCheckDigit_IC = Integer.parseInt(SPUtils.getInstance().getString(SPKey.CHECK_DIGIT_IC, "0"));
        mDataBits_IC = Integer.parseInt(SPUtils.getInstance().getString(SPKey.DATA_BITS_IC, "8"));
        mStopBit_IC = Integer.parseInt(SPUtils.getInstance().getString(SPKey.STOP_BIT_IC, "1"));

        mPath_x86 = SPUtils.getInstance().getString(SPKey.SERIAL_PORT_X86, "");
        mBaudRate_x86 = Integer.parseInt(SPUtils.getInstance().getString(SPKey.BAUD_RATE_X86, "4800"));
        mCheckDigit_x86 = Integer.parseInt(SPUtils.getInstance().getString(SPKey.CHECK_DIGIT_X86, "0"));
        mDataBits_x86 = Integer.parseInt(SPUtils.getInstance().getString(SPKey.DATA_BITS_X86, "8"));
        mStopBit_x86 = Integer.parseInt(SPUtils.getInstance().getString(SPKey.STOP_BIT_X86, "1"));
        boardNumber1 = SPUtils.getInstance().getInt(SPKey.BoardNumber1, 1);
        boardNumber2 = SPUtils.getInstance().getInt(SPKey.BoardNumber2, 2);


        isInterrupted_box = true;
        isInterrupted_ic = true;
        isInterrupted_x86 = true;


        if (TextUtils.isEmpty(mPath)) {
            ToastUtils.showShort("请设置串口！");
            return;
        }
        OpenDoorSerialPort();
        openICSerialPort();
        OpenDoorSerialPort_x86();

    }




    private void OpenDoorSerialPort() {
        //门控制
        try {
            mSerialPort_box = new SerialPort(new File(mPath), mBaudRate, mCheckDigit, mDataBits, mStopBit, 0);
        } catch (IOException e) {
            e.printStackTrace();

            ToastUtils.showShort("门箱串口开启失败");
        }
        if (mSerialPort_box != null) {
            isInterrupted_box = false;
            onSendSubscribe();
            onReceiveSubscribe();
            ToastUtils.showShort("门箱串口开启成功");
        }

    }

    @SuppressLint("CheckResult")
    private void onReceiveSubscribe() {
        mReceiveDisposable = Flowable.create(new FlowableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(FlowableEmitter<byte[]> emitter) throws Exception {
                InputStream is = mSerialPort_box.getInputStream();
                int available;
                int first;
                while (!isInterrupted_box && mSerialPort_box != null && is != null && (first = is.read()) != -1) {
                    do {
                        available = is.available();
                        SystemClock.sleep(1);
                    } while (available != is.available());
                    available = is.available();
                    byte[] bytes = new byte[available + 1];
                    is.read(bytes, 1, available);
                    bytes[0] = (byte) (first & 0xFF);
                    emitter.onNext(bytes);
                }

                close();
            }
        }, BackpressureStrategy.MISSING)
                .subscribeOn(Schedulers.io())
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<byte[]>() {
                    @Override
                    public void accept(byte[] bytes) throws Exception {

                        if (mEmitter_x86 != null) {
                            mEmitter_x86.onNext(bytes);
                        }
                        if (bytes.length == 11) {
                            //修改
                            byte s = bytes[5];
                            String s1 = Integer.toHexString(0xFF & s);
                            if (s1.equals("1")) {
                                String boxStatusValue1_8 = getBoxStatusValue(bytes);
                                setDataDoor(boxStatusValue1_8, boardNumber1);
                            }
                            if (s1.equals("2")) {
                                String boxStatusValue9_16 = getBoxStatusValue(bytes);
                                setDataDoor(boxStatusValue9_16, boardNumber2);

                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });



    }

    private void setDataDoor(String box_1_8, int type) {

        if (type == 1) {
            boxStatusList1_8.clear();

            for (int i = 0; i < 8; i++) {
                String box_n = String.valueOf(box_1_8.charAt(i));
                BoxStatus boxStatus = new BoxStatus(i, box_n.equals("0"), false);
                setdb(boxStatus);
                boxStatusList1_8.add(boxStatus);
            }


        } else if (type == 2) {

            boxStatusList9_16.clear();
            for (int i = 8; i < 16; i++) {

                String box_n = String.valueOf(box_1_8.charAt(i - 8));


                BoxStatus boxStatus = new BoxStatus(i, box_n.equals("0"), false);
                setdb(boxStatus);
                boxStatusList9_16.add(boxStatus);
            }
//
        }
        if (view != null) {


            view.getData(boxStatusList1_8, boxStatusList9_16);

        }
    }

    private void onSendSubscribe() {


        subscribe_box= Observable.create((ObservableOnSubscribe<byte[]>) emitter -> {
            mEmitter = emitter;
        }).doOnNext(bytes ->

                mSerialPort_box.getOutputStream().write(bytes)

        ).observeOn(AndroidSchedulers.mainThread())
                .subscribe(bytes -> {

                }, throwable -> {

                });
    }
    private void close() {
        mSerialPort_box = null;
        isInterrupted_box = true;
        disposable(mReceiveDisposable);
        disposable(subscribe_box);
    }

    private void disposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

    }



    private void setdb(BoxStatus boxStatus) {
        List<DoorNumberInFo> doorNumberInFos = LitePal.where("doorNumber ==?", boxStatus.getSerialNumber() + "").find(DoorNumberInFo.class);
        if (doorNumberInFos.size() > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("isOpenState", boxStatus.isOpenState());
            LitePal.updateAll(DoorNumberInFo.class, contentValues, "doorNumber ==?",  boxStatus.getSerialNumber() + "");

        } else {
            DoorNumberInFo doorNumberInFo = new DoorNumberInFo(boxStatus.getSerialNumber(), "", "", "", true);
            boolean save = doorNumberInFo.save();
        }


    }


    private void openICSerialPort() {
        //IC卡监听
        try {
            mSerialPort_ic = new SerialPort(new File(mPath_IC), mBaudRate_IC, mCheckDigit_IC, mDataBits_IC, mStopBit_IC, 0);
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtils.showShort("IC串口开启失败");
        }
        if (mSerialPort_ic != null) {
            isInterrupted_ic = false;
            onSendSubscribeIC();
            onReceiveSubscribeIC();
            ToastUtils.showShort("IC串口开启成功");


        }

    }

    /**
     * 写IC卡信息
     */
    private void onSendSubscribeIC() {
        subscribe_ic= Observable.create((ObservableOnSubscribe<byte[]>) emitter -> {
            mEmitter_ic = emitter;
        }).doOnNext(bytes -> mSerialPort_box.getOutputStream().write(bytes)).observeOn(AndroidSchedulers.mainThread())
                .subscribe(bytes -> {
                    Log.e(TAG, "ic卡--------------发送的指令 " + bytes.length);
                }, throwable -> {
                    Log.e(TAG, "ic卡--------------发送指令异常" + throwable.toString());
                });

    }

    @SuppressLint("CheckResult")
    private void onReceiveSubscribeIC() {
        receiveDisposable_ic= Flowable.create(new FlowableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(FlowableEmitter<byte[]> emitter) throws Exception {
                InputStream is = mSerialPort_ic.getInputStream();
                int available;
                int first;
                while (!isInterrupted_ic && mSerialPort_ic != null && is != null && (first = is.read()) != -1) {
                    do {
                        available = is.available();
                        SystemClock.sleep(1);
                    } while (available != is.available());

                    available = is.available();
                    byte[] bytes = new byte[available + 1];
                    is.read(bytes, 1, available);
                    bytes[0] = (byte) (first & 0xFF);
                    emitter.onNext(bytes);
                }
                close_ic();

            }
        }, BackpressureStrategy.MISSING)
                .retry()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<byte[]>() {
                    @Override
                    public void accept(byte[] bytes) throws Exception {
                        if (bytes.length > 0) {


                        String s1 = initListener(bytes);

                        if (isICstaue) {
                            if (view != null) {
                                view.setICNumber(s1);
                            }


                        } else {

                            List<DoorNumberInFo> all = LitePal.findAll(DoorNumberInFo.class);
                            for (DoorNumberInFo doorNumberInFo : all) {
                                Log.e(TAG, "accept: " + doorNumberInFo.getIcNumber());
                                if (s1.equals(doorNumberInFo.getIcNumber())) {
                                    if (!doorNumberInFo.isOpenState()) {
                                        //开门
                                        sendData((doorNumberInFo.getDoorNumber() - 1), 0);


                                    } else {
                                        ToastUtils.showShort("门已开");
                                    }
                                }
                            }

                        }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: ic卡读取异常" + throwable.toString());
                    }
                });

    }

    public void sendData(int pos, int type) {
            byte[] bytes = null;
            try {
                String sendData = "";

                if (type == 0) {
                    if (key.size() > pos) {
                        sendData = key.get(pos);
                    } else {
                    }
                }
                Log.e(TAG, "sendData: 门对于的指令  "+sendData );
                if (TextUtils.isEmpty(sendData)) {
                    ToastUtils.showShort("指令不能为空");
                    return;
                }
                String replace = sendData.replace(" ", "");
                bytes  = ByteUtils.hexStringToBytes(replace);
                if (mSerialPort_box == null) {
                    return;
                }
                if (bytes != null) {
                    mEmitter.onNext(bytes);
                }

            } catch (Exception e) {
                e.fillInStackTrace();
            }
    }

    private void close_ic() {
        mSerialPort_ic = null;
        isInterrupted_ic = true;
        disposable(receiveDisposable_ic);
        disposable(subscribe_ic);



    }

    private void OpenDoorSerialPort_x86() {
        //门控制
        try {
            mSerialPort_x86 = new SerialPort(new File(mPath_x86), mBaudRate_x86, mCheckDigit_x86, mDataBits_x86, mStopBit_x86, 0);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "OpenDoorSerialPort: "+e.toString() );
            ToastUtils.showShort("PC串口开启失败");

        }
        if (mSerialPort_x86 != null) {
            isInterrupted_x86 = false;
            onSendSubscribe_x86();
            onReceiveSubscribe_x86();

            ToastUtils.showShort("PC串口开启成功");
        }

    }

    @SuppressLint("CheckResult")
    private void onReceiveSubscribe_x86() {
        receivesubscribe_x86  = Flowable.create(new FlowableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(FlowableEmitter<byte[]> emitter) throws Exception {
                InputStream is = mSerialPort_x86.getInputStream();
                int available;
                int first;
                while (!isInterrupted_x86 && mSerialPort_x86 != null && is != null && (first = is.read()) != -1) {
                    do {
                        available = is.available();
                        SystemClock.sleep(1);
                    } while (available != is.available());
                    available = is.available();
                    byte[] bytes = new byte[available + 1];
                    is.read(bytes, 1, available);
                    bytes[0] = (byte) (first & 0xFF);
                    emitter.onNext(bytes);
                }
                close_x86();
            }
        }, BackpressureStrategy.MISSING)
                .retry()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<byte[]>() {
                    @Override
                    public void accept(byte[] bytes) throws Exception {

                        if (bytes.length == 0) {
                            return;
                        }
                        Log.e(TAG, "accept:pc下发的指令长度： " + bytes.length);
                        String PC_KEY = ByteUtils.bytesToHexString(bytes);
                        Log.e(TAG, "accept: pc指令：" + PC_KEY);
                        if (view != null) {

                            view.DoorStatus("pc下发的指令" + PC_KEY);
                            ToastUtils.showShort("pc下发的指令" + PC_KEY);
                        }



                        if (bytes.length > 0) {

                            String reverse1_2 = PC_KEY.substring(0, 1);

                            String reverse1_8 = PC_KEY.substring(1);


                            if (reverse1_2.equals("1")) {

                                sendData((Integer.valueOf(reverse1_8).intValue() - 1), 0);

                            } else if (reverse1_2.equals("2")) {
                                sendData(((Integer.valueOf(reverse1_8).intValue() + 8) - 1), 0);
                            }
                            if (PC_KEY.equals("ff")) {
                                byte[] bytes1 = ByteUtils.hexStringToBytes(QueryLock1);
                                mEmitter.onNext(bytes1);
                                byte[] bytes2 = ByteUtils.hexStringToBytes(QueryLock2);
                                mEmitter.onNext(bytes2);
                            }

//                        mEmitter.onNext(bytes);


                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }

    private void close_x86() {
        mSerialPort_x86 = null;
        isInterrupted_x86 = true;
        disposable(receivesubscribe_x86);
        disposable(subscribe_x86);

    }

    private void onSendSubscribe_x86() {
        Log.e(TAG, "onSendSubscribe: 写入" );
        subscribe_x86= Observable.create((ObservableOnSubscribe<byte[]>) emitter -> {
            mEmitter_x86 = emitter;
        }).doOnNext(bytes ->

                mSerialPort_x86.getOutputStream().write(bytes)

        ).observeOn(AndroidSchedulers.mainThread())
                .subscribe(bytes -> {
                    Log.e(TAG, "PC--------------发送的指令 " + bytes.length);
                }, throwable -> {
                    Log.e(TAG, "PC--------------发送指令异常" + throwable.toString());
                });

    }

    @Override
    public void setAllOpen() {
        if (isAllOpen) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                isAllOpen = true;

                if (key.size() == 16) {

                    for (int i = 0; i < key.size(); i++) {
                        BoxStatus boxStatus = null;
                        if (i < 8) {
                            boxStatus = boxStatusList1_8.get(i);

                        }
                        if (7 < i) {
                            boxStatus = boxStatusList9_16.get(i - 8);
                        }
                        if (boxStatus != null) {
                            if (!boxStatus.isOpenState()) {
                                //开门
                                sendData(i, 0);

                                try {
                                    Thread.sleep(800);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    }
                }
                isAllOpen = false;
            }
        }).start();

    }
    public void SelectedItem(boolean isICstaue) {

        this.isICstaue = isICstaue;
    }

    public void onDestroy(){
        view = null;
        close();
        close_ic();
        close_x86();

    }
}