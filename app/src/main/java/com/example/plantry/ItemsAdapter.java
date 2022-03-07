package com.example.plantry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>
{
    HashSet<Item> data;
    Context context;
    public ItemsAdapter(Context context, HashSet<Item> data){
        this.data=data;
        this.context=context;
    }
    @NonNull
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.item_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ViewHolder holder,int position)
    {
        Item[] itemArray = new Item[data.size()];
        itemArray = data.toArray(itemArray);
        holder.name.setText(itemArray[position].getItemName().toString());
        holder.addedBy.setText("added by " + itemArray[position].getAddedBy().toString());

        // Calculate days to expired
        Calendar today = Calendar.getInstance();
        long diff = today.getTimeInMillis() - itemArray[position].getExpiryDate();
        long days = -diff / (24*60*60*1000);
        holder.expiryDate.setText(days + " days");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,addedBy,expiryDate;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            addedBy = itemView.findViewById(R.id.added_by);
            expiryDate = itemView.findViewById(R.id.expiry_date);
        }
    }
}