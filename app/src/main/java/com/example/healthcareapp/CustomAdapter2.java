package com.example.healthcareapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CustomAdapter2 extends BaseAdapter {
    private Fragment activity;
    private ArrayList<PostInformation> items;
    public CustomAdapter2(Fragment activity, ArrayList<PostInformation> items)
    {
        this.items = items;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.activity_postitem,null);
        ListView listimage = view.findViewById(R.id.listimg);
        TextView write = view.findViewById(R.id.write);
        TextView time = view.findViewById(R.id.time);

        write.setText(items.get(i).post);

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(items.get(i).posttime));
        String pTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        time.setText(pTime);


        CustomAdapter3 adapter3 = new CustomAdapter3((Activity) view.getContext(),items.get(i).postimgs);
        listimage.setAdapter(adapter3);

        return view;
    }
}
