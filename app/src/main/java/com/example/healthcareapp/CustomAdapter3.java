package com.example.healthcareapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter3 extends BaseAdapter {
    private Activity activity;
    private List<String> items;
    public CustomAdapter3(Activity activity, List<String> items)
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
        ImageButton btn = view.findViewById(R.id.delete);
        btn.setVisibility(View.INVISIBLE);;
//        try {
//            System.out.println("adapter3");
//            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(items.get(i)).getContent());
//            System.out.println("adapter3"+bitmap);
//            img.setImageBitmap(bitmap);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Picasso.get().load(items.get(i)).into(img);
        return view;
    }
}
