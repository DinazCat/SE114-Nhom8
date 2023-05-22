package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private  List<String> meals;
    private HashMap<String, List<food>> foodList;
    public  ExpandableListViewAdapter(Context context, List<String> chappterList, HashMap<String,List<food>> topicsList) {
        this.context=context;
        this.meals=chappterList;
        this.foodList = topicsList;
    }
    @Override
    public int getGroupCount() {
        return this.meals.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.foodList.get(this.meals.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.meals.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String name = this.foodList.get(this.meals.get(groupPosition)).get(childPosition).getNameFood();
        String cl = this.foodList.get(this.meals.get(groupPosition)).get(childPosition).getCaloriesFood();
        food newfood = new food("",name,cl,"");
        return newfood;
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
        food topicTitle = (food) getChild(groupPosition, childPosition);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.food_layout,null);
        }
        TextView topicTV = convertView.findViewById(R.id.textViewFoods);
        TextView topicTV1 = convertView.findViewById(R.id.textViewFoodCalories);
        topicTV.setText(topicTitle.getNameFood());
        topicTV1.setText(topicTitle.getCaloriesFood());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
