package com.example.healthcareapp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.healthcareapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailimgAdapter extends BaseAdapter {
    private Activity activity;
    private List<String> items;
    public DetailimgAdapter(Activity activity, List<String> items)
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
        Picasso.get().load(items.get(i)).into(img);

//        ImageButton deletebtn = view.findViewById(R.id.delete);
//        deletebtn.setVisibility(View.INVISIBLE);

        return view;
    }

}
