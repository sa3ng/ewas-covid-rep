package com.mp3.ewas_covid_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mp3.ewas_covid_app.Activities.OrgMain;
import com.mp3.ewas_covid_app.Activities.ProfileActivity;
import com.mp3.ewas_covid_app.Models.OrgTransaction;
import com.mp3.ewas_covid_app.Models.Transaction;
import com.mp3.ewas_covid_app.R;

import java.util.ArrayList;

public class OrgListTransacAdapter extends RecyclerView.Adapter<OrgListTransacAdapter.MyViewHolder> {
    private ArrayList<Transaction> orgArrayList;
    private Context contextCopy;

    public OrgListTransacAdapter(ArrayList<Transaction> orgArrayList, Context baseContext){
        this.orgArrayList = orgArrayList;
        this.contextCopy = baseContext;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //declare TV's from item.xml to be put used in RV
        private TextView nameTV;
        private TextView dateTV;
        private TextView timeTV;
        public View viewCopy;


        public MyViewHolder(final View view){
            super(view);
            viewCopy = view;
            nameTV = view.findViewById(R.id.tv_org_name);
            dateTV = view.findViewById(R.id.tv_dateTime);
            timeTV = view.findViewById(R.id.tv_time_hm);

        }
    }

    @NonNull
    @Override
    public OrgListTransacAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_transac_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrgListTransacAdapter.MyViewHolder holder, int position) {
        //SET values per item
        holder.nameTV.setText(orgArrayList.get(position).getName());
        holder.dateTV.setText(orgArrayList.get(position).getDate());
        holder.timeTV.setText(orgArrayList.get(position).getTime());


        holder.viewCopy.setOnClickListener(v -> {
            Intent toProfile = new Intent(v.getContext(), OrgMain.class);
            toProfile.putExtra("orgSelectedUID", orgArrayList.get(position).getTransacUID());
            toProfile.putExtra("isViewing", true);
            toProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            contextCopy.startActivity(toProfile);
        });

    }

    @Override
    public int getItemCount() {
        return orgArrayList.size();
    }
}
