package com.mp3.ewas_covid_app.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mp3.ewas_covid_app.Activities.ProfileActivity;
import com.mp3.ewas_covid_app.Models.Transaction;
import com.mp3.ewas_covid_app.R;

import java.util.ArrayList;

public class UserListTransacAdapter extends RecyclerView.Adapter<UserListTransacAdapter.MyViewHolder> {
    private ArrayList<Transaction> userArrayList;
    private Context contextCopy;


    public UserListTransacAdapter(ArrayList<Transaction> userArrayList, Context baseContext){
        this.userArrayList = userArrayList;
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
    public UserListTransacAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_transac_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListTransacAdapter.MyViewHolder holder, int position) {
        //SET values per item
        holder.nameTV.setText(userArrayList.get(position).getName());
        holder.dateTV.setText(userArrayList.get(position).getDate());
        holder.timeTV.setText(userArrayList.get(position).getTime());


        holder.viewCopy.setOnClickListener(v -> {
            Intent toProfile = new Intent(v.getContext(), ProfileActivity.class);
            toProfile.putExtra("userSelectedUID", userArrayList.get(position).getTransacUID());
            toProfile.putExtra("isViewing", true);
            toProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            contextCopy.startActivity(toProfile);

            Toast.makeText(contextCopy, "AKO si " + position, Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
}
