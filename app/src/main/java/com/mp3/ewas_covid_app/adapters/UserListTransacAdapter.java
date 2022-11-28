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

public class UserListTransacAdapter extends RecyclerView.Adapter<UserListTransacAdapter.MyViewHolder> {
    private ArrayList<Transaction> userArrayList;
    public UserListTransacAdapter(ArrayList<Transaction> userArrayList){
        this.userArrayList = userArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //declare TV's from item.xml to be put used in RV
        private TextView nameTV;


        public MyViewHolder(final View view){
            super(view);
            nameTV = view.findViewById(R.id.tv_org_name);

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
        String userName = userArrayList.get(position).getName();
        holder.nameTV.setText(userName);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
}
