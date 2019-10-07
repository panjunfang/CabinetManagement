package com.policeequipment.android.cabinetmanagement.bean;

import org.litepal.crud.LitePalSupport;

public class DoorNumberInFo extends LitePalSupport {
    private int doorNumber;
    private String msg;
    private String DoorNumberPassword;
    private String icNumber;
    private boolean isOpenState;

    public int getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(int doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDoorNumberPassword() {
        return DoorNumberPassword;
    }

    public void setDoorNumberPassword(String doorNumberPassword) {
        DoorNumberPassword = doorNumberPassword;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public boolean isOpenState() {
        return isOpenState;
    }

    public void setOpenState(boolean openState) {
        isOpenState = openState;
    }

    public DoorNumberInFo(int doorNumber, String msg, String doorNumberPassword, String icNumber, boolean isOpenState) {
        this.isOpenState = isOpenState;
        this.doorNumber = doorNumber;
        this.msg = msg;
        DoorNumberPassword = doorNumberPassword;
        this.icNumber = icNumber;
    }

    @Override
    public String toString() {
        return "DoorNumberInFo{" +
                "doorNumber=" + doorNumber +
                ", msg='" + msg + '\'' +
                ", DoorNumberPassword='" + DoorNumberPassword + '\'' +
                ", icNumber='" + icNumber + '\'' +
                ", isOpenState=" + isOpenState +
                '}';
    }
}
