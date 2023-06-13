package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.example.healthcareapp.Model.threeType;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private  List<String> meals;
    private HashMap<String, List<threeType>> typeList;
    private String date;
    public  ExpandableListViewAdapter(Context context, List<String> chappterList, HashMap<String,List<threeType>> topicsList, String date) {
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
        threeType type = new threeType(id,name,cl,unit);
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
        TextView topicTV = convertView.findViewById(R.id.textViewFoods);
        TextView topicTV1 = convertView.findViewById(R.id.textViewFoodCalories);
        TextView topicTV2 = convertView.findViewById(R.id.type);
        Button btnDelete = convertView.findViewById(R.id.delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (meals.get(groupPosition).equals("Breakfast") || meals.get(groupPosition).equals("Lunch") || meals.get(groupPosition).equals("Dinner")
                || meals.get(groupPosition).equals("Snack") ) {
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("foodDiary");
                    database.child(uid).child(date).child(meals.get(groupPosition)).child(topicTitle.getIdType()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().removeValue();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    if (meals.get(groupPosition).equals("Exercise")) {
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference("exerciseDiary");
                        database.child(uid).child(date).child(topicTitle.getIdType()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().removeValue();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else {
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference("water");
                        database.child(uid).child(date).child(topicTitle.getIdType()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().removeValue();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }
        });
        topicTV.setText(topicTitle.getNameType());
        topicTV1.setText(topicTitle.getNumberType());
        topicTV2.setText(topicTitle.getUnitType());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
