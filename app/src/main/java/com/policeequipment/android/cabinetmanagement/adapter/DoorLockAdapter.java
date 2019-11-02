package com.policeequipment.android.cabinetmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.policeequipment.android.cabinetmanagement.R;
import com.policeequipment.android.cabinetmanagement.bean.BoxStatus;

import java.util.ArrayList;
import java.util.List;

public class DoorLockAdapter extends RecyclerView.Adapter<DoorLockAdapter.DoorLockViewHolder> {
private Context context;
private List<BoxStatus> boxStatusList=new ArrayList<>();

    public DoorLockAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DoorLockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoorLockViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.door_lock_item_ly,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DoorLockViewHolder holder, int position) {
        BoxStatus boxStatus = boxStatusList.get(position);

        if (boxStatus.isOpenState()){
            holder.item_iv.setImageResource(R.mipmap.ic_bg_open_state);
        }else {
            holder.item_iv.setImageResource(R.mipmap.ic_bg_lock_door_satus);
        }
    }

    @Override
    public int getItemCount() {
        return boxStatusList.size();
    }

    public void setItemList(List<BoxStatus> boxStatusList) {
        this.boxStatusList=boxStatusList;
    }

    class DoorLockViewHolder extends RecyclerView.ViewHolder{
        ImageView item_iv;
    public DoorLockViewHolder(@NonNull View itemView) {
        super(itemView);
        item_iv=itemView.findViewById(R.id.item_iv);
    }
}

}
