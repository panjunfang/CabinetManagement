package com.policeequipment.android.cabinetmanagement.service;

import com.policeequipment.android.cabinetmanagement.bean.BoxStatus;

import java.util.List;

public interface SerialPortContract {
    interface IView {
        void setOpen(boolean isOpen);

        void getData(List<BoxStatus> list1_8, List<BoxStatus> list9_16);

        void setData(List<BoxStatus> list, int type);

        void DoorStatus(String s);
        void setICNumber(String icNumber);

    }

    interface IPresenter {
        void refreshValueFormSp();

        void setData();

        void setAllOpen();
    }
}
