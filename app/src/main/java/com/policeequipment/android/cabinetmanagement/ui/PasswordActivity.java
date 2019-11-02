package com.policeequipment.android.cabinetmanagement.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.policeequipment.android.cabinetmanagement.R;
import com.policeequipment.android.cabinetmanagement.base.BaseActivity;
import com.policeequipment.android.cabinetmanagement.bean.ActivityMessageEvent;
import com.policeequipment.android.cabinetmanagement.bean.DoorNumberInFo;
import com.policeequipment.android.cabinetmanagement.bean.ServerMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.List;

import static com.policeequipment.android.cabinetmanagement.util.SPKey.Administrator_password;

public class PasswordActivity extends BaseActivity {


    private AppCompatEditText appCompatEditText;
    private TextView in_button_Features;
    private int tapy;
    @Override
    protected void initView() {
        tapy  = getIntent().getIntExtra("tapy", 0);

        in_button_Features = (TextView) findViewById(R.id.in_button_Features);
        switch (tapy){
            case 1:
                in_button_Features.setText("注册");
                break;
            case 2:
                in_button_Features.setText("开锁");
                break;
            case 3:
                in_button_Features.setText("设置");
                break;
                default:
        }
        appCompatEditText = (AppCompatEditText) findViewById(R.id.appCompatEditText);
        appCompatEditText.clearFocus();
        appCompatEditText.setInputType(InputType.TYPE_NULL);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(appCompatEditText.getWindowToken(),0);


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EvnServer(ServerMessageEvent messageEvent){

    }
    @Override
    protected int initLayout() {
        return R.layout.activity_password;
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
                int start = appCompatEditText.length() - 1;
                if (start >= 0) {
                    appCompatEditText.getText().delete(start, start + 1);
                }
                break;
            case R.id.in_button_Features:

                onClickButton();

                break;
            case R.id.password_back_tv:
               PasswordActivity.this.finish();
                break;
                default:
        }
    }

    private void onClickButton() {
        String trim = appCompatEditText.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            ToastUtils.showShort("密码不能为空！");
            return;
        }
        ActivityMessageEvent activityMessageEvent = new ActivityMessageEvent();
        switch (tapy) {
            case 1:
                if (TextUtils.isEmpty(trim)) {
                    ToastUtils.showShort("请输入管理员密码");
                }else{
                    if (trim.equals(Administrator_password)) {
                        activityMessageEvent.setMessenger(2);
                        EventBus.getDefault().post(activityMessageEvent);
                        startActivity(new Intent(PasswordActivity.this, RegisteredActivity.class));
                        PasswordActivity.this.finish();
                    } else {
                        ToastUtils.showShort("管理员密码错误");
                    }
                }
                break;
            case 2:
                if (trim.equals(Administrator_password)) {
                    //全开//

                    activityMessageEvent.setMessenger(0);


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

                        EventBus.getDefault().post(activityMessageEvent);
                        PasswordActivity.this.finish();

                    } else {
                        ToastUtils.showShort("无效密码");
                    }



                } else {

                    ToastUtils.showShort("未有注册信息");
                }

                break;
            case 3:
                if (trim.equals(Administrator_password)) {
                    activityMessageEvent.setMessenger(2);
                    EventBus.getDefault().post(activityMessageEvent);
                    startActivityForResult(new Intent(PasswordActivity.this, SettingActivity.class), 0x01);
                    PasswordActivity.this.finish();
                } else {
                    ToastUtils.showShort("管理员密码错误");
                }
                break;
                default:
        }

    }

    private void settext(String insert) {
        appCompatEditText.append(insert);
        appCompatEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

    }
}
