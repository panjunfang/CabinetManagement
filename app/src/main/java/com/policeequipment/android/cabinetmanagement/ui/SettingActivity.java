package com.policeequipment.android.cabinetmanagement.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.deemons.serialportlib.SerialPortFinder;
import com.policeequipment.android.cabinetmanagement.R;
import com.policeequipment.android.cabinetmanagement.SPKey;
import com.policeequipment.android.cabinetmanagement.bean.ActivityMessageEvent;
import com.policeequipment.android.cabinetmanagement.bean.DoorNumberInFo;
import com.policeequipment.android.cabinetmanagement.bean.ServerMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private Spinner stting_serial_port_spinner;
    private Spinner setting_baud_rate_sp;
    private String mPath;
    private int mBaudRate;
    private int mCheckDigit;
    private int mDataBits;
    private int mStopBit;

    private String mPath_IC;
    private int mBaudRate_IC;
    private int mCheckDigit_IC;
    private int mDataBits_IC;
    private int mStopBit_IC;

    private String mPath_x86;
    private int mBaudRate_x86;
    private int mCheckDigit_x86;
    private int mDataBits_x86;
    private int mStopBit_x86;


    private Spinner stting_serial_port_ic_spinner;
    private Spinner setting_baud_rate_ic_sp;
    private List<String> door_list;
    private RadioGroup setting_registered_rg;
    private Spinner setting_registered_list_sp;
    private boolean isRegistered = false;
    private TextView setting_registered_status_tv;
    private EditText setting_registered_pwd_et;
    private int position;
    private RadioGroup setting_registered1_rg;
    private RadioGroup setting_registered2_rg;

    private List<String> key1_8;
    private List<String> key9_16;
    private Spinner stting_serial_port_x86_spinner;
    private Spinner setting_baud_rate_x86_sp;
    private int BoardNumber1 = 0;
    private int BoardNumber2 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        EventBus.getDefault().register(this);
        initView();
        setDoorLock();
        setICSetting();
        setSetting_ic();
        setDoorKey();

    }

    private void setDoorKey() {
        key1_8 = new ArrayList<>();
        key9_16 = new ArrayList<>();
        for (int i = 0; i < SPKey.InstructionSet.length; i++) {
            if (i < 8) {
                key1_8.add(SPKey.InstructionSet[i]);
            }
            if (i > 7) {
                key9_16.add(SPKey.InstructionSet[i]);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EvnServer(ServerMessageEvent messageEvent) {
        Log.e("tag", "Evn: 服务");
        switch (messageEvent.getMessenger()) {
            case 1://更新状态
                setting_registered_pwd_et.setText(messageEvent.getIcNumber());
                break;
        }
    }

    private void setICSetting() {
        door_list = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            door_list.add((i + 1) + "号门箱");
        }

        ArrayAdapter<String> integerArrayAdapter_door = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, door_list);
        setting_registered_list_sp.setAdapter(integerArrayAdapter_door);

        setting_registered_list_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showShort("选中" + door_list.get(position));
                SettingActivity.this.position = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDoorLock() {


        int[] intArray = Utils.getApp().getResources().getIntArray(R.array.baud_rate);
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (int i = 0; i < intArray.length; i++) {

            integerArrayList.add(Integer.valueOf(intArray[i]));
        }

        ArrayAdapter<Integer> integerArrayAdapter = new ArrayAdapter<>(this, R.layout.item_tv, integerArrayList);
        setting_baud_rate_sp.setAdapter(integerArrayAdapter);
        for (int j = 0; j < integerArrayList.size(); j++) {
            Integer integer = integerArrayList.get(j);
            if (integer == mBaudRate) {
                setting_baud_rate_sp.setSelection(j, true);
            }
        }
        setting_baud_rate_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPUtils.getInstance().put(SPKey.BAUD_RATE, integerArrayList.get(position) + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //ic
        ArrayAdapter<Integer> integerArrayAdapter_ic = new ArrayAdapter<>(this, R.layout.item_tv, integerArrayList);
        setting_baud_rate_ic_sp.setAdapter(integerArrayAdapter_ic);
        for (int j = 0; j < integerArrayList.size(); j++) {
            Integer integer = integerArrayList.get(j);
            if (integer == mBaudRate_IC) {
                setting_baud_rate_ic_sp.setSelection(j, true);
            }
        }
        setting_baud_rate_ic_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPUtils.getInstance().put(SPKey.BAUD_RATE_IC, integerArrayList.get(position) + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //pc波特率
        ArrayAdapter<Integer> integerArrayAdapter_x86 = new ArrayAdapter<>(this, R.layout.item_tv, integerArrayList);
        setting_baud_rate_x86_sp.setAdapter(integerArrayAdapter_x86);
        for (int j = 0; j < integerArrayList.size(); j++) {
            Integer integer = integerArrayList.get(j);
            if (integer == mBaudRate_x86) {
                setting_baud_rate_x86_sp.setSelection(j, true);
            }
        }
        setting_baud_rate_x86_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPUtils.getInstance().put(SPKey.BAUD_RATE_X86, integerArrayList.get(position) + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        SerialPortFinder serialPortFinder = new SerialPortFinder();
        String[] allDevices = serialPortFinder.getAllDevices();
        String[] allDevicesPath = serialPortFinder.getAllDevicesPath();


        if (allDevicesPath.length > 0) {
            ArrayList<String> allDevicesPathList = new ArrayList<>(Arrays.asList(allDevicesPath));


            ArrayAdapter<String> allDevicesPathAdapter = new ArrayAdapter<String>(this, R.layout.item_tv, allDevicesPathList);
            stting_serial_port_spinner.setAdapter(allDevicesPathAdapter);

            for (int x = 0; x < allDevicesPathList.size(); x++) {
                String DevicesPath = allDevicesPathList.get(x);

                if (mPath.equals(DevicesPath)) {
                    stting_serial_port_spinner.setSelection(x, true);
                }
            }
            //选择串口
            stting_serial_port_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SPUtils.getInstance().put(SPKey.SERIAL_PORT, allDevicesPathList.get(position) + "");

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //ic
            ArrayAdapter<String> allDevicesPathAdapter_ic = new ArrayAdapter<String>(this, R.layout.item_tv, allDevicesPathList);
            stting_serial_port_ic_spinner.setAdapter(allDevicesPathAdapter_ic);

            for (int x = 0; x < allDevicesPathList.size(); x++) {
                String DevicesPath = allDevicesPathList.get(x);

                if (mPath_IC.equals(DevicesPath)) {
                    stting_serial_port_ic_spinner.setSelection(x, true);
                }
            }
            //选择串口
            stting_serial_port_ic_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SPUtils.getInstance().put(SPKey.SERIAL_PORT_IC, allDevicesPathList.get(position) + "");

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

//pc串口
//            stting_serial_port_x86_spinner
            ArrayAdapter<String> allDevicesPathAdapter_86 = new ArrayAdapter<String>(this, R.layout.item_tv, allDevicesPathList);
            stting_serial_port_x86_spinner.setAdapter(allDevicesPathAdapter_86);

            for (int x = 0; x < allDevicesPathList.size(); x++) {
                String DevicesPath = allDevicesPathList.get(x);

                if (mPath_x86.equals(DevicesPath)) {
                    stting_serial_port_x86_spinner.setSelection(x, true);
                }
            }
            //选择串口
            stting_serial_port_x86_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SPUtils.getInstance().put(SPKey.SERIAL_PORT_X86, allDevicesPathList.get(position) + "");

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        } else {
            ToastUtils.showShort("读取串口列表失败");
        }


    }

    private void setSetting_ic() {
        ActivityMessageEvent activityMessageEvent = new ActivityMessageEvent();
        activityMessageEvent.setMessenger(3);
        EventBus.getDefault().post(activityMessageEvent);

    }

    private void initView() {
        setting_registered1_rg = (RadioGroup) findViewById(R.id.setting_registered1_rg);
        setting_registered2_rg = (RadioGroup) findViewById(R.id.setting_registered2_rg);


        stting_serial_port_x86_spinner = (Spinner) findViewById(R.id.stting_serial_port_x86_spinner);

        setting_baud_rate_x86_sp = (Spinner) findViewById(R.id.setting_baud_rate_x86_sp);


        stting_serial_port_spinner = (Spinner) findViewById(R.id.stting_serial_port_spinner);
        setting_baud_rate_sp = (Spinner) findViewById(R.id.setting_baud_rate_sp);

        stting_serial_port_ic_spinner = (Spinner) findViewById(R.id.stting_serial_port_ic_spinner);
        setting_baud_rate_ic_sp = (Spinner) findViewById(R.id.setting_baud_rate_ic_sp);

        setting_registered_list_sp = (Spinner) findViewById(R.id.setting_registered_list_sp);
        setting_registered_rg = (RadioGroup) findViewById(R.id.setting_registered_rg);
        setting_registered_status_tv = (TextView) findViewById(R.id.setting_registered_status_tv);
        setting_registered_pwd_et = (EditText) findViewById(R.id.setting_registered_pwd_et);

        setting_registered_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.setting_pwd_rb:
                        ToastUtils.showShort("密码注册");
                        isRegistered = false;
                        setting_registered_pwd_et.setText("");
                        setting_registered_pwd_et.setHint("请输入");
                        setting_registered_status_tv.setText("密码：");
                        break;
                    case R.id.setting_ic_rb:
                        isRegistered = true;
                        setting_registered_pwd_et.setText("");
                        setting_registered_pwd_et.setHint("请刷卡");
                        setting_registered_status_tv.setText("IC卡");
                        ToastUtils.showShort("IC卡注册");
                        break;
                }
            }
        });


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
        mBaudRate_x86 = Integer.parseInt(SPUtils.getInstance().getString(SPKey.BAUD_RATE_X86, "115200"));
        mCheckDigit_x86 = Integer.parseInt(SPUtils.getInstance().getString(SPKey.CHECK_DIGIT_X86, "0"));
        mDataBits_x86 = Integer.parseInt(SPUtils.getInstance().getString(SPKey.DATA_BITS_X86, "8"));
        mStopBit_x86 = Integer.parseInt(SPUtils.getInstance().getString(SPKey.STOP_BIT_X86, "1"));


        BoardNumber1 = SPUtils.getInstance().getInt(SPKey.BoardNumber1, 1);
        BoardNumber2 = SPUtils.getInstance().getInt(SPKey.BoardNumber2, 2);


        if (BoardNumber1 == 1) {

            setting_registered1_rg.check(R.id.setting_door_panel1_rb);

        } else if (BoardNumber1 == 2) {
            setting_registered1_rg.check(R.id.setting_door_panel2_rb);
        }
        if (BoardNumber2 == 1) {

            setting_registered2_rg.check(R.id.setting_door_panel2_1_rb);

        } else if (BoardNumber2 == 2) {
            setting_registered2_rg.check(R.id.setting_door_panel2_2_rb);
        }

        setting_registered1_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                List<String> key_list = new ArrayList<>();

                switch (checkedId) {
                    case R.id.setting_door_panel1_rb:
                        BoardNumber1 = 1;
                        key_list.addAll(key1_8);
                        break;
                    case R.id.setting_door_panel2_rb:
                        BoardNumber1 = 2;
                        key_list.addAll(key9_16);

                        break;
                }

                SPUtils.getInstance().put(SPKey.BoardNumber1, BoardNumber1);

                ActivityMessageEvent activityMessageEvent = new ActivityMessageEvent();
                activityMessageEvent.setBoardNumber(BoardNumber1);
                activityMessageEvent.setMessenger(5);
                activityMessageEvent.setKeylist(key_list);

                EventBus.getDefault().post(activityMessageEvent);

            }
        });
        setting_registered2_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                List<String> key_list = new ArrayList<>();


                switch (checkedId) {
                    case R.id.setting_door_panel2_1_rb:
                        BoardNumber2 = 1;
                        key_list.addAll(key9_16);
                        break;
                    case R.id.setting_door_panel2_2_rb:
                        BoardNumber2 = 2;
                        key_list.addAll(key1_8);
                        break;
                }

                SPUtils.getInstance().put(SPKey.BoardNumber2, BoardNumber2);
                ActivityMessageEvent activityMessageEvent = new ActivityMessageEvent();
                activityMessageEvent.setBoardNumber(BoardNumber2);
                activityMessageEvent.setMessenger(6);
                activityMessageEvent.setKeylist(key_list);

                EventBus.getDefault().post(activityMessageEvent);


            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent();
            setResult(0x01, i);
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    public void onClick(View view) {

        Log.e("tag", "onClick: " + position);

        switch (view.getId()) {
            case R.id.setting_registered_but:
                String pwd = setting_registered_pwd_et.getText().toString().trim();
                if (isRegistered) {//IC卡注册
                    if (!TextUtils.isEmpty(pwd)) {
                        //得到卡号注册
                        List<DoorNumberInFo> doorNumberInFos = LitePal.where("doorNumber ==?", position + "").find(DoorNumberInFo.class);
                        if (doorNumberInFos.size() > 0) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("icNumber", pwd);
                            LitePal.updateAll(DoorNumberInFo.class, contentValues, "doorNumber ==?", position + "");
                            ToastUtils.showShort(position + "号门注册完成");

                        } else {
                            DoorNumberInFo doorNumberInFo = new DoorNumberInFo(position, "注册入库", "", pwd, false);

                            boolean save = doorNumberInFo.save();
                            if (save) {
                                ToastUtils.showShort(position + "号门注册完成");

                            } else {
                                ToastUtils.showShort(position + "号门注册失败");
                            }
                        }
                    } else {
                        ToastUtils.showShort("卡号不能为空");
                    }

                } else {//密码注册

                    if (TextUtils.isEmpty(pwd)) {
                        ToastUtils.showShort("密码不能为空");
                        return;
                    }

                    List<DoorNumberInFo> doorNumberInFos = LitePal.where("doorNumber ==?", position + "").find(DoorNumberInFo.class);

                    if (doorNumberInFos.size() > 0) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("DoorNumberPassword", pwd);
                        LitePal.updateAll(DoorNumberInFo.class, contentValues, "doorNumber ==?", position + "");
                        ToastUtils.showShort(position + "号门注册完成");

                    } else {
                        DoorNumberInFo doorNumberInFo = new DoorNumberInFo(position, "注册入库", pwd, "", false);
                        boolean save = doorNumberInFo.save();
                        if (save) {
                            ToastUtils.showShort(position + "号门注册完成");

                        } else {
                            ToastUtils.showShort(position + "号门注册失败");
                        }
                    }

                }
                break;
            case R.id.setting_back_but:

                break;
            case R.id.setting_up_but:
                ActivityMessageEvent activityMessageEvent = new ActivityMessageEvent();
                activityMessageEvent.setMessenger(4);
                EventBus.getDefault().post(activityMessageEvent);
                break;
            case R.id.setting_back_ib:
                Intent i = new Intent();
                setResult(0x01, i);
                finish();
                break;
        }

    }
}