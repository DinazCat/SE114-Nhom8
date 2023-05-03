package com.example.healthcareapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.appevents.ml.Model;
import com.jgabrielfreitas.core.BlurImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


public class CustomAdapter3 extends RecyclerView.Adapter {
    private ArrayList<PostInformation> dataSet;
    Context mContext;
    int total_types;
    FragmentManager fragmentManager;


    public static class NoImageType extends RecyclerView.ViewHolder{
        TextView FName, FIngredient, FMaking, FSummary ;
        RatingBar FRating;
        TextView time, username ;
        CircleImageView userimg;
        public NoImageType(View itemView) {
            super(itemView);
            this.username = itemView.findViewById(R.id.username);
            this.userimg = itemView.findViewById(R.id.userimg);
            this.FName = itemView.findViewById(R.id.write);
            this.FRating = itemView.findViewById(R.id.ratingbar);
            this.FIngredient = itemView.findViewById(R.id.write1);
            this.FMaking = itemView.findViewById(R.id.write2);
            this.FSummary = itemView.findViewById(R.id.write3);
            this.time = itemView.findViewById(R.id.time);
        }
    }
    public static class TwoImageType extends RecyclerView.ViewHolder{
        TextView FName, FIngredient, FMaking, FSummary ;
        RatingBar FRating;
        TextView time, username ;
        ImageView img1, img2, img3;
        CircleImageView userimg;
        LinearLayout layout2;
        public TwoImageType(View itemView) {
            super(itemView);
            this.username = itemView.findViewById(R.id.username);
            this.userimg = itemView.findViewById(R.id.userimg);
            this.FName = itemView.findViewById(R.id.write);
            this.FRating = itemView.findViewById(R.id.ratingbar);
            this.FIngredient = itemView.findViewById(R.id.write1);
            this.FMaking = itemView.findViewById(R.id.write2);
            this.FSummary = itemView.findViewById(R.id.write3);
            this.time = itemView.findViewById(R.id.time);
            this.img1 = itemView.findViewById(R.id.img1);
            this.img2 = itemView.findViewById(R.id.img2);
            this.img3 = itemView.findViewById(R.id.img3);
            this.layout2 = itemView.findViewById(R.id.layout2img);
        }
    }
    public static class FourImageType extends RecyclerView.ViewHolder{
        TextView FName, FIngredient, FMaking, FSummary ;
        RatingBar FRating;
        ImageView img1, img2, img3, img4, img5, img6;
        BlurImageView img7;
        TextView time, countimg, username ;
        CircleImageView userimg;
        LinearLayout layout3, layout4;
        public FourImageType(View itemView) {
            super(itemView);
            this.username = itemView.findViewById(R.id.username);
            this.userimg = itemView.findViewById(R.id.userimg);
            this.FName = itemView.findViewById(R.id.write);
            this.FRating = itemView.findViewById(R.id.ratingbar);
            this.FIngredient = itemView.findViewById(R.id.write1);
            this.FMaking = itemView.findViewById(R.id.write2);
            this.FSummary = itemView.findViewById(R.id.write3);
            this.time = itemView.findViewById(R.id.time);
            this.countimg = itemView.findViewById(R.id.countimg);
            this.img1 = itemView.findViewById(R.id.img4);
            this.img2 = itemView.findViewById(R.id.img5);
            this.img3 = itemView.findViewById(R.id.img6);
            this.img4 = itemView.findViewById(R.id.img7);
            this.img5 = itemView.findViewById(R.id.img8);
            this.img6 = itemView.findViewById(R.id.img9);
            this.img7 = itemView.findViewById(R.id.img10);
            this.layout3 = itemView.findViewById(R.id.layout3img);
            this.layout4 = itemView.findViewById(R.id.layout4img);
        }
    }
    public CustomAdapter3(ArrayList<PostInformation> data, Context context, FragmentManager fragmentManager) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
        this.fragmentManager = fragmentManager;
    }
    @Override
    public int getItemViewType(int position) {


        if(dataSet.get(position).postimgs.size() == 0)
        {
            return 0;
        }
        else if(dataSet.get(position).postimgs.size() < 3)
        {
            return 1;
        }
        else if(dataSet.get(position).postimgs.size() > 2)
        {
            return 2;
        }
        return -1;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int i) {
        View view;
        switch (i) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_layout0img, parent, false);
                return new NoImageType(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_postitem, parent, false);
                return new TwoImageType(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_layout4img, parent, false);
                return new FourImageType(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i)  {
        PostInformation object = dataSet.get(i);
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(object.posttime));
        String pTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        View.OnClickListener mListener = new View.OnClickListener(){
            public void onClick(View view){
                PostDetailActivity.info = dataSet.get(i);
                mContext.startActivity(new Intent(mContext,PostDetailActivity.class));
            }
        };
        View.OnClickListener userimgListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = object.userid;
                ProfileFragment fragment = new ProfileFragment();
                Bundle args = new Bundle();
                args.putString("userId", userId);
                fragment.setArguments(args);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
        if(object !=null){
            if(object.postimgs.size() == 0)
            {
                ((NoImageType) viewHolder).FName.setText(object.postFoodName);
                ((NoImageType) viewHolder).FRating.setRating(Float.parseFloat(object.postFoodRating));
                ((NoImageType) viewHolder).FIngredient.setText(object.postFoodIngredient);
                ((NoImageType) viewHolder).FMaking.setText(object.postFoodMaking);
                ((NoImageType) viewHolder).FSummary.setText(object.postFoodSummary);
                ((NoImageType) viewHolder).time.setText(pTime);
                ((NoImageType) viewHolder).username.setText(object.username);
                try{
                    Picasso.get().load(object.userimg).into(((NoImageType) viewHolder).userimg);
                }
                catch (Exception e){
                    String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
                    Picasso.get().load(uri).into(((NoImageType) viewHolder).userimg);
                }
                ((NoImageType) viewHolder).userimg.setOnClickListener(userimgListener);
            }
            else if(object.postimgs.size() < 3)
            {
                ((TwoImageType) viewHolder).FName.setText(object.postFoodName);
                ((TwoImageType) viewHolder).FRating.setRating(Float.parseFloat(object.postFoodRating));
                ((TwoImageType) viewHolder).FIngredient.setText(object.postFoodIngredient);
                ((TwoImageType) viewHolder).FMaking.setText(object.postFoodMaking);
                ((TwoImageType) viewHolder).FSummary.setText(object.postFoodSummary);
                ((TwoImageType) viewHolder).time.setText(pTime);
                ((TwoImageType) viewHolder).username.setText(object.username);
                try{
                    Picasso.get().load(object.userimg).into(((TwoImageType) viewHolder).userimg);
                }
                catch (Exception e){
                    String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
                    Picasso.get().load(uri).into(((TwoImageType) viewHolder).userimg);
                }
                ((TwoImageType) viewHolder).userimg.setOnClickListener(userimgListener);
                if(object.postimgs.size() == 1)
                {
                    Picasso.get().load(object.postimgs.get(0)).into(((TwoImageType) viewHolder).img1);
                    ((TwoImageType) viewHolder).img1.setOnClickListener(mListener);
                    ((TwoImageType) viewHolder).img1.setVisibility(View.VISIBLE);
                    ((TwoImageType) viewHolder).layout2.setVisibility(View.INVISIBLE);
                }
                if(object.postimgs.size() == 2)
                {
                    Picasso.get().load(object.postimgs.get(0)).into(((TwoImageType) viewHolder).img2);
                    Picasso.get().load(object.postimgs.get(1)).into(((TwoImageType) viewHolder).img3);
                    ((TwoImageType) viewHolder).img2.setOnClickListener(mListener);
                    ((TwoImageType) viewHolder).img3.setOnClickListener(mListener);
                    ((TwoImageType) viewHolder).img1.setVisibility(View.INVISIBLE);
                    ((TwoImageType) viewHolder).layout2.setVisibility(View.VISIBLE);
                }

            }
            else if(object.postimgs.size() > 2)
            {
                ((FourImageType) viewHolder).FName.setText(object.postFoodName);
                ((FourImageType) viewHolder).FRating.setRating(Float.parseFloat(object.postFoodRating));
                ((FourImageType) viewHolder).FIngredient.setText(object.postFoodIngredient);
                ((FourImageType) viewHolder).FMaking.setText(object.postFoodMaking);
                ((FourImageType) viewHolder).FSummary.setText(object.postFoodSummary);
                ((FourImageType) viewHolder).time.setText(pTime);
                ((FourImageType) viewHolder).username.setText(object.username);
                try{
                    Picasso.get().load(object.userimg).into(((FourImageType) viewHolder).userimg);
                }
                catch (Exception e){
                    String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
                    Picasso.get().load(uri).into(((FourImageType) viewHolder).userimg);
                }
                ((FourImageType) viewHolder).userimg.setOnClickListener(userimgListener);
                if(object.postimgs.size() == 3)
                {
                    Picasso.get().load(object.postimgs.get(0)).into(((FourImageType) viewHolder).img1);
                    Picasso.get().load(object.postimgs.get(1)).into(((FourImageType) viewHolder).img2);
                    Picasso.get().load(object.postimgs.get(2)).into(((FourImageType) viewHolder).img3);
                    ((FourImageType) viewHolder).img1.setOnClickListener(mListener);
                    ((FourImageType) viewHolder).img2.setOnClickListener(mListener);
                    ((FourImageType) viewHolder).img3.setOnClickListener(mListener);
                    ((FourImageType) viewHolder).layout3.setVisibility(View.VISIBLE);
                    ((FourImageType) viewHolder).layout4.setVisibility(View.INVISIBLE);
                }
                if(object.postimgs.size() >= 4)
                {
                    Picasso.get().load(object.postimgs.get(0)).into(((FourImageType) viewHolder).img4);
                    Picasso.get().load(object.postimgs.get(1)).into(((FourImageType) viewHolder).img5);
                    Picasso.get().load(object.postimgs.get(2)).into(((FourImageType) viewHolder).img6);
                    Picasso.get().load(object.postimgs.get(3)).into(((FourImageType) viewHolder).img7);
                    ((FourImageType) viewHolder).img4.setOnClickListener(mListener);
                    ((FourImageType) viewHolder).img5.setOnClickListener(mListener);
                    ((FourImageType) viewHolder).img6.setOnClickListener(mListener);
                    ((FourImageType) viewHolder).img7.setOnClickListener(mListener);
                    ((FourImageType) viewHolder).countimg.setVisibility(View.INVISIBLE);
                    if(object.postimgs.size() > 4)
                    {
                        Picasso.get().load(object.postimgs.get(3)).into(((FourImageType) viewHolder).img7,new com.squareup.picasso.Callback() {

                            @Override
                            public void onSuccess() {
                                ((FourImageType) viewHolder).img7.setBlur(8);
                            }

                            @Override
                            public void onError(Exception e) {
                            }
                        });

                        ((FourImageType) viewHolder).countimg.setText("+ "+(object.postimgs.size()-3));
                        ((FourImageType) viewHolder).countimg.setVisibility(View.VISIBLE);
                    }
                    ((FourImageType) viewHolder).layout3.setVisibility(View.INVISIBLE);
                    ((FourImageType) viewHolder).layout4.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

