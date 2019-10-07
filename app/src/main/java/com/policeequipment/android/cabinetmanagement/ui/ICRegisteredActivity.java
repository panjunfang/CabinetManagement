package com.policeequipment.android.cabinetmanagement.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.policeequipment.android.cabinetmanagement.R;
import com.policeequipment.android.cabinetmanagement.bean.ActivityMessageEvent;
import com.policeequipment.android.cabinetmanagement.bean.DoorNumberInFo;
import com.policeequipment.android.cabinetmanagement.bean.ServerMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.List;

public class ICRegisteredActivity extends AppCompatActivity {

    private TextView ic_tv;
    private ImageView IC_IC_iv;
    private Button ic_but;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_icregistered);
        initView();
        position = getIntent().getIntExtra("position", 0);

        EventBus.getDefault().register(this);

        setSetting_ic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
    private void setSetting_ic() {
        ActivityMessageEvent activityMessageEvent = new ActivityMessageEvent();
        activityMessageEvent.setMessenger(3);
        EventBus.getDefault().post(activityMessageEvent);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EvnServer(ServerMessageEvent messageEvent) {
        Log.e("tag", "Evn: 服务");
        switch (messageEvent.getMessenger()) {
            case 1://更新状态
                ic_tv.setText("注册完成");
//                IC_IC_iv.setImageResource(R.mipmap);
                ic_but.setText("确认");
                String pwd = messageEvent.getIcNumber();

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
                break;
        }
    }

    private void initView() {

        ic_tv = (TextView) findViewById(R.id.ic_tv);
        IC_IC_iv = (ImageView) findViewById(R.id.IC_IC_iv);
        ic_but = (Button) findViewById(R.id.ic_but);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_back_ib:
                finish();
                break;
            case R.id.ic_but:
                ic_but.setEnabled(false);

                ic_tv.setText("注册完成");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);

                break;

        }
    }
}