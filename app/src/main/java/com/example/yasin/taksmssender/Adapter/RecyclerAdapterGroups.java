package com.example.yasin.taksmssender.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yasin.taksmssender.Model.Groups;
import com.example.yasin.taksmssender.Model.LandScape;
import com.example.yasin.taksmssender.R;

import java.util.List;

public class RecyclerAdapterGroups extends RecyclerView.Adapter<RecyclerAdapterGroups.ViewHolder> {

    List<Groups> list;
    LayoutInflater inflater;
    Context context;

    public RecyclerAdapterGroups (Context context  , List<Groups> list){
        this.context = context;
        this.inflater = LayoutInflater.from(context) ;
        this.list = list;
    }
    @NonNull
    @Override
    public RecyclerAdapterGroups.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_view_groups, parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterGroups.ViewHolder holder, int position) {
        Groups currentObj = list.get(position);
        holder.setData(currentObj , position , context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtGroupName;
        TextView txtSize;
        public ViewHolder(View itemView) {
            super(itemView);
            txtSize = (TextView)itemView.findViewById(R.id.txtGroupSize);
            txtGroupName = (TextView)itemView.findViewById(R.id.txtGroupName);
        }

        public void setData(Groups currentObj, int position, Context context) {
            this.txtGroupName.setText(currentObj.getGroupName());
            this.txtSize.setText(currentObj.getGroupSize());
        }
    }
}
