package com.policeequipment.android.cabinetmanagement.bean;

import java.util.List;

public class ActivityMessageEvent {

    private int messenger;
    private int Door_number;
    private int boardNumber;
    private List<String> keylist;


    public ActivityMessageEvent() {
    }

    public int getBoardNumber() {
        return boardNumber;
    }

    public void setBoardNumber(int boardNumber) {
        this.boardNumber = boardNumber;
    }

    public List<String> getKeylist() {
        return keylist;
    }

    public void setKeylist(List<String> keylist) {
        this.keylist = keylist;
    }

    public int getDoor_number() {
        return Door_number;
    }

    public void setDoor_number(int door_number) {
        Door_number = door_number;
    }

    public int getMessenger() {
        return messenger;
    }

    public void setMessenger(int messenger) {
        this.messenger = messenger;
    }
}
