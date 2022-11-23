package com.mp3.ewas_covid_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mp3.ewas_covid_app.Models.Transaction;
import com.mp3.ewas_covid_app.R;

import java.util.ArrayList;

public class OrgTransacAdapter extends RecyclerView.Adapter<OrgTransacAdapter.MyViewHolder> {
    private ArrayList<Transaction> orgArrayList;
    public OrgTransacAdapter(ArrayList<Transaction> orgArrayList){
        this.orgArrayList = orgArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTV;


        public MyViewHolder(final View view){
            super(view);
            nameTV = view.findViewById(R.id.tv_org_name);

        }
    }

    @NonNull
    @Override
    public OrgTransacAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_transac_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrgTransacAdapter.MyViewHolder holder, int position) {
        String orgName = orgArrayList.get(position).getName();


        holder.nameTV.setText(orgName);
    }

    @Override
    public int getItemCount() {
        return orgArrayList.size();
    }
}
