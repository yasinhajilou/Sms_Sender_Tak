package com.example.yasin.taksmssender.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasin.taksmssender.Model.Contacts;
import com.example.yasin.taksmssender.R;
import com.pchmn.materialchips.ChipsInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecyclerAdapterContacts extends RecyclerView.Adapter<RecyclerAdapterContacts.MyViewHolder> {

    ChipsInput chipsInput;
    private LayoutInflater inflater;
    private List<Contacts> mData;
    private Context context;
    public RecyclerAdapterContacts(Context context , List<Contacts> mData , ChipsInput chipsInput){
        this.mData = mData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.chipsInput = chipsInput;

        Collections.sort(mData, new Comparator<Contacts>() {
            @Override
            public int compare(Contacts o1, Contacts o2) {
                return o1.getFullName().compareTo(o2.getFullName());
            }
        });
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_view_contacts , parent , false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Contacts contacts = mData.get(position);

        holder.setData(contacts , position , context);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{
        TextView txtFullName;
        TextView txtPhoneNumber;
        int position;
        RelativeLayout layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtFullName = (TextView)itemView.findViewById(R.id.txtFullName);
            txtPhoneNumber = (TextView)itemView.findViewById(R.id.txtPhoneNumber);
            layout = (RelativeLayout)itemView.findViewById(R.id.layoutContacts);
        }

        private void setData(final Contacts contacts, final int position, final Context context) {
            txtPhoneNumber.setText(contacts.getPhoneNumber());
            txtFullName.setText(contacts.getFullName());
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipsInput.addChip(contacts.getFullName(),contacts.getPhoneNumber());
                }
            });
        }
    }
}
