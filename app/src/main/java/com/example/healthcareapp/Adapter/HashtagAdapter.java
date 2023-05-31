package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Fragments.Fragment_baiviet2;
import com.example.healthcareapp.Model.hashtag;
import com.example.healthcareapp.R;

import java.util.ArrayList;

public class HashtagAdapter extends RecyclerView.Adapter<HashtagAdapter.ViewHolder> {
    private Context context;
    private ArrayList<hashtag> items;
    public HashtagAdapter(Context activity, ArrayList<hashtag> items)
    {
        this.items = items;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tag_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tag.setText(items.get(position).getName());
        if(items.get(position).getTick() == false){
            holder.tag.setBackgroundResource(R.drawable.border_hashtag);
        }
        else{
            holder.tag.setBackgroundResource(R.drawable.border_hashtag2);
        }
        holder.tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.get(position).setTick(!items.get(position).getTick());
                if(items.get(position).getTick() == false){
                    holder.tag.setBackgroundResource(R.drawable.border_hashtag);
                    Fragment_baiviet2.hashtags.remove(items.get(position).getName());
                }
                else{
                    holder.tag.setBackgroundResource(R.drawable.border_hashtag2);
                    Fragment_baiviet2.hashtags.add(items.get(position).getName());
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
    Button tag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             tag = itemView.findViewById(R.id.hashtag);
        }
    }

}
