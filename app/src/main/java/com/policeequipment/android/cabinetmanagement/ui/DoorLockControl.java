package com.policeequipment.android.cabinetmanagement.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.policeequipment.android.cabinetmanagement.R;
import com.policeequipment.android.cabinetmanagement.adapter.DoorLockAdapter;
import com.policeequipment.android.cabinetmanagement.adapter.GridSpacingItemDecoration;
import com.policeequipment.android.cabinetmanagement.adapter.SpacesItemDecoration;
import com.policeequipment.android.cabinetmanagement.base.BaseActivity;
import com.policeequipment.android.cabinetmanagement.bean.ActivityMessageEvent;
import com.policeequipment.android.cabinetmanagement.bean.BoxStatus;
import com.policeequipment.android.cabinetmanagement.bean.ServerMessageEvent;
import com.policeequipment.android.cabinetmanagement.service.SerialPortService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.policeequipment.android.cabinetmanagement.util.SPKey.Administrator_password;

public class DoorLockControl extends BaseActivity {


    private List<BoxStatus> boxStatusList;
    private Button main_box_door1_but;
    private Button main_box_door2_but;
    private Button main_box_door3_but;
    private Button main_box_door4_but;
    private Button main_box_door5_but;
    private Button main_box_door6_but;
    private Button main_box_door7_but;
    private Button main_box_door8_but;
    private Button main_box_door9_but;
    private Button main_box_door10_but;
    private Button main_box_door11_but;
    private Button main_box_door12_but;
    private Button main_box_door13_but;
    private Button main_box_door14_but;
    private Button main_box_door15_but;
    private Button main_box_door16_but;
    private List<Button> buttonList = new ArrayList<>();
    private Intent intent;

    @Override
    protected void initView() {

        main_box_door1_but = (Button) findViewById(R.id.main_box_door1_but);
        main_box_door2_but = (Button) findViewById(R.id.main_box_door2_but);
        main_box_door3_but = (Button) findViewById(R.id.main_box_door3_but);
        main_box_door4_but = (Button) findViewById(R.id.main_box_door4_but);
        main_box_door5_but = (Button) findViewById(R.id.main_box_door5_but);
        main_box_door6_but = (Button) findViewById(R.id.main_box_door6_but);
        main_box_door7_but = (Button) findViewById(R.id.main_box_door7_but);
        main_box_door8_but = (Button) findViewById(R.id.main_box_door8_but);
        main_box_door9_but = (Button) findViewById(R.id.main_box_door9_but);
        main_box_door10_but = (Button) findViewById(R.id.main_box_door10_but);
        main_box_door11_but = (Button) findViewById(R.id.main_box_door11_but);
        main_box_door12_but = (Button) findViewById(R.id.main_box_door12_but);
        main_box_door13_but = (Button) findViewById(R.id.main_box_door13_but);
        main_box_door14_but = (Button) findViewById(R.id.main_box_door14_but);
        main_box_door15_but = (Button) findViewById(R.id.main_box_door15_but);
        main_box_door16_but = (Button) findViewById(R.id.main_box_door16_but);
        main_box_door1_but.setOnClickListener(this::onClick);
        main_box_door2_but.setOnClickListener(this::onClick);
        main_box_door3_but.setOnClickListener(this::onClick);
        main_box_door4_but.setOnClickListener(this::onClick);
        main_box_door5_but.setOnClickListener(this::onClick);
        main_box_door6_but.setOnClickListener(this::onClick);
        main_box_door7_but.setOnClickListener(this::onClick);
        main_box_door8_but.setOnClickListener(this::onClick);
        main_box_door9_but.setOnClickListener(this::onClick);
        main_box_door10_but.setOnClickListener(this::onClick);
        main_box_door11_but.setOnClickListener(this::onClick);
        main_box_door12_but.setOnClickListener(this::onClick);
        main_box_door13_but.setOnClickListener(this::onClick);
        main_box_door14_but.setOnClickListener(this::onClick);
        main_box_door15_but.setOnClickListener(this::onClick);
        main_box_door16_but.setOnClickListener(this::onClick);

        buttonList.add(main_box_door1_but);
        buttonList.add(main_box_door2_but);
        buttonList.add(main_box_door3_but);
        buttonList.add(main_box_door4_but);
        buttonList.add(main_box_door5_but);
        buttonList.add(main_box_door6_but);
        buttonList.add(main_box_door7_but);
        buttonList.add(main_box_door8_but);
        buttonList.add(main_box_door9_but);
        buttonList.add(main_box_door10_but);
        buttonList.add(main_box_door11_but);
        buttonList.add(main_box_door12_but);
        buttonList.add(main_box_door13_but);
        buttonList.add(main_box_door14_but);
        buttonList.add(main_box_door15_but);
        buttonList.add(main_box_door16_but);
        initData();
        intent =new Intent(DoorLockControl.this, SerialPortService.class);
        startService(intent);
    }
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.main_box_door1_but:
                break;
            case R.id.main_box_door2_but:

                break;
            case R.id.main_box_door3_but:
                break;
            case R.id.main_box_door4_but:

                break;
            case R.id.main_box_door5_but:
                break;
            case R.id.main_box_door6_but:
                break;
            case R.id.main_box_door7_but:
                break;
            case R.id.main_box_door8_but:
                break;
            case R.id.main_box_door9_but:
                break;
            case R.id.main_box_door10_but:
                break;
            case R.id.main_box_door11_but:
                break;
            case R.id.main_box_door12_but:
                break;
            case R.id.main_box_door13_but:
                break;
            case R.id.main_box_door14_but:
                break;
            case R.id.main_box_door15_but:
                break;
            case R.id.main_box_door16_but:
                break;
            case R.id.door_lock_registered_tv:
                //注册
                setStart(1);

                break;
            case R.id.door_lock_operation_maintenance_tv:
               //开门
                setStart(2);
                break;
            case R.id.door_lock_setting_tv:
                //设置
                setStart(3);

                break;
                default:

        }
    }

    private void setStart(int i) {
        Intent intent = new Intent(DoorLockControl.this, PasswordActivity.class);
          intent.putExtra("tapy", i);

        startActivity(intent);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EvnServer(ServerMessageEvent messageEvent){
        switch (messageEvent.getMessenger()) {
            case 0://更新状态
                List<BoxStatus> list = messageEvent.getList();
                upDoor(list);

                break;
            case 2:
                String icNumber = messageEvent.getIcNumber();

//                tv.append(icNumber);
                break;
        }
    }

    private void upDoor(List<BoxStatus> list) {
        for (int i = 0; i < list.size(); i++) {
            Button button = buttonList.get(i);
            BoxStatus boxStatus = list.get(i);
            if (boxStatus.isOpenState()) {
                button.setBackgroundResource(R.drawable.ic_bg_open_state);
            } else {
                button.setBackgroundResource(R.drawable.ic_bg_lock_door_satus);
            }
        }
    }

    private void initData() {
        boxStatusList=new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            BoxStatus boxStatus=new BoxStatus(i,false,false);
            boxStatusList.add(boxStatus);
            buttonList.get(i).setBackgroundResource(R.drawable.ic_bg_lock_door_satus);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }

    @Override
    protected int initLayout() {

        return R.layout.activity_door_lock_control;
    }


}
