package com.policeequipment.android.cabinetmanagement.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.policeequipment.android.cabinetmanagement.R;
import com.policeequipment.android.cabinetmanagement.base.BaseActivity;
import com.policeequipment.android.cabinetmanagement.bean.ActivityMessageEvent;
import com.policeequipment.android.cabinetmanagement.bean.BoxStatus;
import com.policeequipment.android.cabinetmanagement.bean.DoorNumberInFo;
import com.policeequipment.android.cabinetmanagement.bean.ServerMessageEvent;
import com.policeequipment.android.cabinetmanagement.service.SerialPortService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static com.policeequipment.android.cabinetmanagement.util.SPKey.Administrator_password;

public class HomeActivity extends BaseActivity {

    private EditText main_pwd_ed;
    private List<Button> buttonList = new ArrayList<>();
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
    private  Intent intent;

    @Override
    protected int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @SuppressLint("CheckResult")
    protected void initView() {
        intent =new Intent(HomeActivity.this, SerialPortService.class);
        main_pwd_ed = (EditText) findViewById(R.id.main_pwd_ed);

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
        main_pwd_ed.clearFocus();
        main_pwd_ed.setInputType(InputType.TYPE_NULL);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(main_pwd_ed.getWindowToken(),0);

        startService(intent);



    }

    @Override
    protected void onResume() {
        super.onResume();
        setSetting_ic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }
    private void setSetting_ic() {
        ActivityMessageEvent activityMessageEvent = new ActivityMessageEvent();
        activityMessageEvent.setMessenger(2);
        EventBus.getDefault().post(activityMessageEvent);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EvnServer(ServerMessageEvent messageEvent){
        switch (messageEvent.getMessenger()) {
            case 0://更新状态
                List<BoxStatus> list = messageEvent.getList();
                upDoor(list);

                break;
        }
    }

    private void upDoor(List<BoxStatus> list) {

        for (int i = 0; i < list.size(); i++) {
            Button button = buttonList.get(i);
            BoxStatus boxStatus = list.get(i);
            if (boxStatus.isOpenState()) {
                button.setBackgroundResource(R.mipmap.ic_bg_open_state);

            } else {
                button.setBackgroundResource(R.mipmap.ic_bg_lock_door_satus);

            }
        }
    }
    public void onClick(View view) {
        String trim = main_pwd_ed.getText().toString().trim();
        ActivityMessageEvent activityMessageEvent = new ActivityMessageEvent();
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

            case R.id.main_open_but://注册
                if (TextUtils.isEmpty(trim)) {
                    ToastUtils.showShort("请输入管理员密码");
                }else{
                    if (trim.equals(Administrator_password)) {
                        activityMessageEvent.setMessenger(2);
                        EventBus.getDefault().post(activityMessageEvent);
                        startActivity(new Intent(HomeActivity.this, RegisteredActivity.class));
                        main_pwd_ed.setText("");
                    } else {
                        ToastUtils.showShort("管理员密码错误");
                    }
                }



                break;
            case R.id.main_setting_but://进入设置



                if (TextUtils.isEmpty(trim)) {
                    ToastUtils.showShort("请输入管理员密码");
                }else{
                    if (trim.equals(Administrator_password)) {
                        activityMessageEvent.setMessenger(2);
                        EventBus.getDefault().post(activityMessageEvent);
                        startActivityForResult(new Intent(HomeActivity.this, SettingActivity.class), 0x01);
                        main_pwd_ed.setText("");
                    } else {
                        ToastUtils.showShort("管理员密码错误");
                    }
                }

                break;
        }
    }

    public void ButtonEvent(View view) {
        switch (view.getId()) {
            case R.id.in_button1:
                settext("1");

                break;
            case R.id.in_button2:
                settext("2");
                break;
            case R.id.in_button3:
                settext("3");
                break;
            case R.id.in_button4:
                settext("4");
                break;
            case R.id.in_button5:
                settext("5");
                break;
            case R.id.in_button6:
                settext("6");
                break;
            case R.id.in_button7:
                settext("7");
                break;
            case R.id.in_button8:
                settext("8");
                break;
            case R.id.in_button9:
                settext("9");
                break;
            case R.id.in_button0:
                settext("0");
                break;
            case R.id.in_button_delete:
                int start = main_pwd_ed.length() - 1;
                if (start >= 0) {
                    main_pwd_ed.getText().delete(start, start + 1);
                }
                break;
            case R.id.in_button_Features:
                String trim = main_pwd_ed.getText().toString().trim();
                ActivityMessageEvent activityMessageEvent = new ActivityMessageEvent();

                if (!TextUtils.isEmpty(trim)) {
                    if (trim.equals(Administrator_password)) {
                        //全开//

                        activityMessageEvent.setMessenger(0);

                        main_pwd_ed.setText("");
                        EventBus.getDefault().post(activityMessageEvent);


                        return;
                    }

                    List<DoorNumberInFo> doorNumberInFos = LitePal.findAll(DoorNumberInFo.class);

                    if (doorNumberInFos.size() > 0) {
                        int Number = -1;
                        for (int i = 0; i < doorNumberInFos.size(); i++) {
                            DoorNumberInFo doorNumberInFo = doorNumberInFos.get(i);
                            String doorNumberPassword = doorNumberInFo.getDoorNumberPassword();


                            if (!TextUtils.isEmpty(doorNumberPassword)) {

                                if (doorNumberPassword.equals(trim)) {
                                    Number = doorNumberInFo.getDoorNumber();
                                }
                            }


                        }


                        if (!(Number < 0)) {
                            activityMessageEvent.setMessenger(1);
                            activityMessageEvent.setDoor_number(((Number - 1)));
                            main_pwd_ed.setText("");
                            EventBus.getDefault().post(activityMessageEvent);

                        } else {
                            ToastUtils.showShort("无效密码");
                        }



                    } else {

                        ToastUtils.showShort("未有注册信息");
                    }


                }else{
                    ToastUtils.showShort("请输入开锁密码");
                }

                break;
        }
    }

    private void settext(String insert) {
        main_pwd_ed.append(insert);
        main_pwd_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());

    }
}
