package com.example.healthcareapp;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class CustomAdapter1 extends BaseAdapter {
    private Activity activity;
    private ArrayList<Uri> items;
    public CustomAdapter1(Activity activity, ArrayList<Uri> items)
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
        view = inflater.inflate(R.layout.activity_imgitem,null);
        ImageView img = view.findViewById(R.id.img);
        img.setImageURI(items.get(i));

        ImageButton deletebtn = view.findViewById(R.id.delete);
        deletebtn.setVisibility(View.VISIBLE);
       // deletebtn.setImageResource(R.drawable.ic_delete);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddImgFragment.images.remove(items.get(i));
                if(AddImgFragment.images.size()==0) AddImgFragment.Layout.setVisibility(View.VISIBLE);
                CustomAdapter1 adapter = new CustomAdapter1(activity, AddImgFragment.images);
                AddImgFragment.listView.setAdapter(adapter);
            }
        });
        if(AddImgFragment.images.size()!=0) {AddImgFragment.Layout.setVisibility(View.INVISIBLE);}
        else AddImgFragment.Layout.setVisibility(View.VISIBLE);
        return view;
    }
}
