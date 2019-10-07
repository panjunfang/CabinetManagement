package com.policeequipment.android.cabinetmanagement.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.policeequipment.android.cabinetmanagement.bean.ActivityMessageEvent;
import com.policeequipment.android.cabinetmanagement.bean.BoxStatus;
import com.policeequipment.android.cabinetmanagement.bean.ServerMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.policeequipment.android.cabinetmanagement.SPKey.InstructionSet;

public class SerialPortService extends Service implements SerialPortContract.IView  {

    private SerialPortPresenter portPresenter;
    private boolean is;
    private List<BoxStatus> boxStatusList1_8=new ArrayList<>();
    private List<BoxStatus> boxStatusList9_16 = new ArrayList<>();
    public SerialPortService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        portPresenter = new SerialPortPresenter(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("结束");

        portPresenter.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EvnActivity(ActivityMessageEvent messageEvent){
        LogUtils.i("服务收到的指令" + messageEvent.getMessenger());

        switch (messageEvent.getMessenger()) {
            case 0://全开
                portPresenter.setAllOpen();
                break;
            case 1:

                int door_number = messageEvent.getDoor_number();
                LogUtils.i("门号" +door_number);
                portPresenter.sendData(door_number, 0);
                break;
            case 2://读取开门状态
                portPresenter.SelectedItem(false);

                break;
            case 3://注册状态
                portPresenter.SelectedItem(true);

                break;
            case 4:

                portPresenter.refreshValueFormSp();

                break;
            case 5:
//                List<String> keylist1 = messageEvent.getKeylist();
//                int boardNumber1 = messageEvent.getBoardNumber();
//
//
//                portPresenter.upKey(keylist1, 5,boardNumber1);

                portPresenter.setData();

                break;
            case 6:
                portPresenter.setData();
                break;
            case 7:
//                portPresenter.setData();
                break;
        }
    }


    @Override
    public void setOpen(boolean isOpen) {

    }

    @Override
    public void getData(List<BoxStatus> list1_8, List<BoxStatus> list9_16) {
        this.boxStatusList1_8 = list1_8;
        this.boxStatusList9_16 = list9_16;
        List<BoxStatus> boxStatusList = new ArrayList<>();
        boxStatusList.addAll(boxStatusList1_8);
        boxStatusList.addAll(boxStatusList9_16);
        ServerMessageEvent serverMessageEvent = new ServerMessageEvent();
        serverMessageEvent.setMessenger(0);
        serverMessageEvent.setList(boxStatusList);
        EventBus.getDefault().post(serverMessageEvent);
    }

    @Override
    public void setData(List<BoxStatus> list, int type) {

    }

    @Override
    public void DoorStatus(String s) {

    }

    @Override
    public void setICNumber(String icNumber) {
        ServerMessageEvent serverMessageEvent = new ServerMessageEvent();
        serverMessageEvent.setMessenger(1);
        serverMessageEvent.setIcNumber(icNumber);
        EventBus.getDefault().post(serverMessageEvent);
    }
}
