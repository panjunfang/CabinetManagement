package com.policeequipment.android.cabinetmanagement.bean;

import java.util.List;

public class ServerMessageEvent {
    private int messenger;
    private List<BoxStatus> list;
    private String icNumber;

    public List<BoxStatus> getList() {
        return list;
    }

    public void setList(List<BoxStatus> list) {
        this.list = list;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public ServerMessageEvent() {
    }

    public int getMessenger() {
        return messenger;
    }

    public void setMessenger(int messenger) {
        this.messenger = messenger;
    }
}
