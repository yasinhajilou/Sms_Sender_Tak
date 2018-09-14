package com.example.yasin.taksmssender.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yasin.taksmssender.Model.Contacts;
import com.example.yasin.taksmssender.R;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class RecyclerAdapterAllContacts extends RecyclerView.Adapter<RecyclerAdapterAllContacts.ViewHolder> {

    List<Contacts> contacts;
    LayoutInflater inflater;
    Context context;

    public RecyclerAdapterAllContacts(Context context , List<Contacts> contacts){
        this.context = context;
        this.contacts = contacts;
        this.inflater = LayoutInflater.from(context);
        Collections.sort(contacts, new Comparator<Contacts>() {
            @Override
            public int compare(Contacts o1, Contacts o2) {
                return o1.getFullName().compareTo(o2.getFullName());
            }
        });
    }

    @NonNull
    @Override
    public RecyclerAdapterAllContacts.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_view_all_contacts , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterAllContacts.ViewHolder holder, int position) {
        Random randomGenerator = new Random();
        float red = randomGenerator.nextInt(256);
        int green = randomGenerator.nextInt(256);
        int blue = randomGenerator.nextInt(256);
        Contacts myContacts = contacts.get(position);
        holder.fullName.setText(myContacts.getFullName());
        holder.phoneNumber.setText(myContacts.getPhoneNumber());
        holder.materialLetterIcon.setLetter(myContacts.getFullName().substring(0 , 1));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int randomColor = Color.rgb(red,green,blue);
            holder.materialLetterIcon.setShapeColor(randomColor);

        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullName , phoneNumber;
        MaterialLetterIcon materialLetterIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.txtContactFulLName);
            phoneNumber = itemView.findViewById(R.id.txtContactPhoneNumber);
            materialLetterIcon = itemView.findViewById(R.id.materialIcon);

        }
    }
}
