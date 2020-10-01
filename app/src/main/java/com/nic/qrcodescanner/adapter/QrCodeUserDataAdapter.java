package com.nic.qrcodescanner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.nic.qrcodescanner.R;
import com.nic.qrcodescanner.databinding.ActivityTicketDataBinding;
import com.nic.qrcodescanner.databinding.AdapterTicketDataBinding;
import com.nic.qrcodescanner.pojo.QrcodePojo;

import java.util.ArrayList;
import java.util.List;

public class QrCodeUserDataAdapter extends PagedListAdapter<QrcodePojo, QrCodeUserDataAdapter.MyViewHolder> {

    private List<QrcodePojo> userListValues;
    private String letter;
    private Context context;


    private LayoutInflater layoutInflater;
    private static DiffUtil.ItemCallback<QrcodePojo> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<QrcodePojo>() {
                @Override
                public boolean areItemsTheSame(QrcodePojo oldItem, QrcodePojo newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(QrcodePojo oldItem, QrcodePojo newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public QrCodeUserDataAdapter(Context context, ArrayList<QrcodePojo> userListValues) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.userListValues = userListValues;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private AdapterTicketDataBinding adapterTicketDataBinding;

        public MyViewHolder(AdapterTicketDataBinding Binding) {
            super(Binding.getRoot());
            adapterTicketDataBinding = Binding;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        AdapterTicketDataBinding adapterTicketDataBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.adapter_ticket_data, viewGroup, false);
        return new MyViewHolder(adapterTicketDataBinding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (userListValues.get(0).userdata.get(position).getName().isEmpty()) {
            holder.adapterTicketDataBinding.nameLayout.setVisibility(View.GONE);
        } else {
            holder.adapterTicketDataBinding.nameLayout.setVisibility(View.VISIBLE);
            holder.adapterTicketDataBinding.name.setText(userListValues.get(0).userdata.get(position).getName());

        }

        if (userListValues.get(0).userdata.get(position).getAge().isEmpty()) {
            holder.adapterTicketDataBinding.ageLayout.setVisibility(View.GONE);
        } else {

            holder.adapterTicketDataBinding.ageLayout.setVisibility(View.VISIBLE);
            holder.adapterTicketDataBinding.age.setText(userListValues.get(0).userdata.get(position).getAge());
        }

        if (userListValues.get(0).userdata.get(position).getGender().isEmpty()) {
            holder.adapterTicketDataBinding.genderLayout.setVisibility(View.GONE);
        } else {
            holder.adapterTicketDataBinding.genderLayout.setVisibility(View.VISIBLE);
            holder.adapterTicketDataBinding.gender.setText(userListValues.get(0).userdata.get(position).getGender());
        }

        if (userListValues.get(0).userdata.get(position).getProof_desc().isEmpty()) {
            holder.adapterTicketDataBinding.proofDescLayout.setVisibility(View.GONE);
        } else {
            holder.adapterTicketDataBinding.proofDescLayout.setVisibility(View.VISIBLE);
            holder.adapterTicketDataBinding.proofDesc.setText(userListValues.get(0).userdata.get(position).getProof_desc());
        }

        if (userListValues.get(0).userdata.get(position).getProof_no().isEmpty()) {
            holder.adapterTicketDataBinding.proofLayout.setVisibility(View.GONE);
        } else {
            holder.adapterTicketDataBinding.proofLayout.setVisibility(View.VISIBLE);
            holder.adapterTicketDataBinding.proofNo.setText(userListValues.get(0).userdata.get(position).getProof_no());
        }

        if (userListValues.get(0).userdata.get(position).getService_amount().isEmpty()) {
            holder.adapterTicketDataBinding.serviceAmountLayout.setVisibility(View.GONE);
        } else {
            holder.adapterTicketDataBinding.serviceAmountLayout.setVisibility(View.VISIBLE);
            holder.adapterTicketDataBinding.serviceAmount.setText(userListValues.get(0).userdata.get(position).getService_amount());
        }


    }


    @Override
    public int getItemCount() {
        return userListValues == null ? 0 : userListValues.get(0).userdata.size();
    }
}
