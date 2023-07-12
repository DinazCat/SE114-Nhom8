package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthcareapp.Fragments.AddFragment;
import com.example.healthcareapp.ListInterface.ClickNewFoodItem;
import com.example.healthcareapp.Model.threeType;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private Context mainContext;
    private  List<String> meals;
    private HashMap<String, List<threeType>> typeList;
    ClickNewFoodItem clickNewFoodItem;
    private String date;

    public  ExpandableListViewAdapter(Context mainContext, Context context, List<String> chappterList, HashMap<String,List<threeType>> topicsList, String date, ClickNewFoodItem clickNewFoodItem) {
        this.mainContext = mainContext;
        this.clickNewFoodItem = clickNewFoodItem;
        this.date = date;
        this.context=context;
        this.meals=chappterList;
        this.typeList = topicsList;

    }
    @Override
    public int getGroupCount() {
        return this.meals.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return this.typeList.get(this.meals.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.meals.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String id = this.typeList.get(this.meals.get(groupPosition)).get(childPosition).getIdType();
        String name = this.typeList.get(this.meals.get(groupPosition)).get(childPosition).getNameType();
        String cl = this.typeList.get(this.meals.get(groupPosition)).get(childPosition).getNumberType();
        String unit = this.typeList.get(this.meals.get(groupPosition)).get(childPosition).getUnitType();
        String cabs = this.typeList.get(this.meals.get(groupPosition)).get(childPosition).getCabs();
        String fat = this.typeList.get(this.meals.get(groupPosition)).get(childPosition).getFat();
        String protein = this.typeList.get(this.meals.get(groupPosition)).get(childPosition).getProtein();
        threeType type = new threeType(id,name,cl,unit,cabs,fat,protein);
        return type;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String chapterTitle = (String) getGroup(groupPosition);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.meals_layout,null);
        }
        TextView chapterTV = convertView.findViewById(R.id.textViewMeals);
        chapterTV.setText(chapterTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        threeType topicTitle = (threeType) getChild(groupPosition, childPosition);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.food_layout,null);
        }
        LinearLayout nutri = convertView.findViewById(R.id.linearLayoutNutri);
        nutri.setVisibility(View.INVISIBLE);

        if (topicTitle.getUnitType().equals(" calories"))
        {
            nutri.setVisibility(View.VISIBLE);
            ProgressBar cabs = convertView.findViewById(R.id.cabsFood);
            ProgressBar fat = convertView.findViewById(R.id.fatFood);
            ProgressBar protein = convertView.findViewById(R.id.proteinFood);
            if (topicTitle.getCabs()!=null) {
                cabs.setProgress(Integer.parseInt(topicTitle.getCabs()));
                fat.setProgress(Integer.parseInt(topicTitle.getFat()));
                protein.setProgress(Integer.parseInt(topicTitle.getProtein()));
            }
            else {
                cabs.setProgress(50);
                fat.setProgress(50);
                protein.setProgress(50);
            }
        }

        TextView topicTV = convertView.findViewById(R.id.textViewFoods);
        TextView topicTV1 = convertView.findViewById(R.id.textViewFoodCalories);
        TextView topicTV2 = convertView.findViewById(R.id.type);
        Button btnDelete = convertView.findViewById(R.id.delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNewFoodItem.onClickItemDelete(topicTitle,groupPosition,childPosition);
                replaceFragment(new AddFragment());
            }
        });
        this.notifyDataSetChanged();
        topicTV.setText(topicTitle.getNameType());
        topicTV1.setText(topicTitle.getNumberType());
        topicTV2.setText(topicTitle.getUnitType());

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity)mainContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

}
