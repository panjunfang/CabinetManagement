package com.policeequipment.android.cabinetmanagement.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.blankj.utilcode.util.LogUtils;
import com.policeequipment.android.cabinetmanagement.R;
import com.policeequipment.android.cabinetmanagement.base.BaseActivity;
import com.policeequipment.android.cabinetmanagement.bean.ServerMessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.atomic.DoubleAccumulator;

public class MainActivity extends BaseActivity {
    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EvnServer(ServerMessageEvent messageEvent){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean ff = "FF".equals("FF");
                LogUtils.e(ff);
                startActivity(new Intent(MainActivity.this, DoorLockControl.class));
                MainActivity.this.finish();

            }
        }, 1000);
    }

    @Override
    protected void initView() {

    }
}
