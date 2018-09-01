package com.example.yasin.taksmssender.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yasin.taksmssender.Model.SmsHistory;
import com.example.yasin.taksmssender.R;

import java.util.List;

public class RecyclerAdapterSmsHistory extends RecyclerView.Adapter<RecyclerAdapterSmsHistory.ViewHolder> {
    LayoutInflater inflater;
    Context context;
    List<SmsHistory> list;
    public RecyclerAdapterSmsHistory(Context context , List<SmsHistory> list){
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_view_sms_history, parent , false);
        return new RecyclerAdapterSmsHistory.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SmsHistory history = list.get(position);
        holder.message.setText(history.getMessageContent());
        holder.to.setText(history.getSendTo());
        holder.time.setText(history.getTime());
        holder.date.setText(history.getDate());
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0){
            return 0;
        }else {
            return list.size();
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView time;
        TextView message;
        TextView to;
        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txtSmsDate);
            time = itemView.findViewById(R.id.txtSmsTime);
            message = itemView.findViewById(R.id.txtSmsContent);
            to = itemView.findViewById(R.id.txtSmsTo);
        }
    }
}
