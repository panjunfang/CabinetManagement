package com.policeequipment.android.cabinetmanagement.bean;

public class BoxStatus {
    private int SerialNumber;
    private boolean isOpenState;
    private boolean isSomething;

    public int getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        SerialNumber = serialNumber;
    }

    public boolean isOpenState() {
        return isOpenState;
    }

    public void setOpenState(boolean openState) {
        isOpenState = openState;
    }

    public boolean isSomething() {
        return isSomething;
    }

    public void setSomething(boolean something) {
        isSomething = something;
    }

    public BoxStatus(int serialNumber, boolean isOpenState, boolean isSomething) {
        SerialNumber = serialNumber;
        this.isOpenState = isOpenState;
        this.isSomething = isSomething;
    }
}
