package com.policeequipment.android.cabinetmanagement.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.policeequipment.android.cabinetmanagement.R;
import com.policeequipment.android.cabinetmanagement.bean.DoorNumberInFo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class RegisteredActivity extends AppCompatActivity {

    private EditText setting_registered_pwd_et;
    private TextView in_button_Features;
    private Spinner setting_registered_list_sp;
    private List<String> door_list;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registered);

        setting_registered_pwd_et = (EditText) findViewById(R.id.setting_registered_pwd_et);
        in_button_Features = (TextView) findViewById(R.id.in_button_Features);
        setting_registered_list_sp = (Spinner) findViewById(R.id.setting_registered_list_sp);

        setICSetting();
        in_button_Features.setText("确定");
        setting_registered_pwd_et.clearFocus();
        setting_registered_pwd_et.setInputType(InputType.TYPE_NULL);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(setting_registered_pwd_et.getWindowToken(), 0);
    }

    private void setICSetting() {
        door_list = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            door_list.add((i + 1) + "号门箱");
        }

        ArrayAdapter<String> integerArrayAdapter_door = new ArrayAdapter<>(this, R.layout.item_tv, door_list);
        setting_registered_list_sp.setAdapter(integerArrayAdapter_door);

        setting_registered_list_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showShort("选中" + door_list.get(position));
                RegisteredActivity.this.position = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onClick(View view) {
        finish();

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
                int start = setting_registered_pwd_et.length() - 1;
                if (start >= 0) {
                    setting_registered_pwd_et.getText().delete(start, start + 1);
                }
                break;
            case R.id.in_button_Features:
                String pwd = setting_registered_pwd_et.getText().toString().trim();

                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showShort("密码不能为空");
                } else {
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

                        Intent intent = new Intent(RegisteredActivity.this, ICRegisteredActivity.class);
                        intent.putExtra("position", position);
                        startActivity(intent);
                        finish();

                    } else {
                        DoorNumberInFo doorNumberInFo = new DoorNumberInFo(position, "注册入库", pwd, "", false);
                        boolean save = doorNumberInFo.save();
                        if (save) {
                            ToastUtils.showShort(position + "号门注册完成");
                            Intent intent = new Intent(RegisteredActivity.this, ICRegisteredActivity.class);
                            intent.putExtra("position", position);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtils.showShort(position + "号门注册失败");
                        }
                    }

                }
                break;
        }
    }

    private void settext(String s) {
        setting_registered_pwd_et.append(s);

    }
}