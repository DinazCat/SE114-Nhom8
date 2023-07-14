package com.example.healthcareapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Fragments.ProfileFragment;
import com.example.healthcareapp.Fragments.SavedPostFragment;
import com.example.healthcareapp.Model.PostInformation;
import com.example.healthcareapp.PostDetailActivity;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class savedpAdapter extends BaseAdapter {
    private Activity activity;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser curUser = auth.getCurrentUser();
    private ArrayList<PostInformation> items;

    public savedpAdapter(Activity activity, ArrayList<PostInformation> items) {
        this.items = items;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
             view = inflater.inflate(R.layout.savedpostform,null);

        TextView foodname = view.findViewById(R.id.foodname);
        TextView calo = view.findViewById(R.id.calories);
        TextView diffi = view.findViewById(R.id.tv_difficulty);
        CircleImageView img = view.findViewById(R.id.img);
       ImageButton delete = view.findViewById(R.id.clear);
        LinearLayout Element = view.findViewById(R.id.press);
        Element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDetailActivity.info = items.get(i);
                activity.startActivity(new Intent(activity,PostDetailActivity.class));
            }
        });
        foodname.setText(items.get(i).postFoodName);
        calo.setText(items.get(i).Calories + " cals/serving");
        diffi.setText(items.get(i).postFoodRating);
        try {
            Picasso.get().load(items.get(i).postimgs.get(0)).into(img);
        } catch (Exception e){
            Picasso.get().load(R.drawable.foodimage).into(img);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment.savedpostlist.remove(i);
                savedpAdapter adapter = new savedpAdapter(activity, ProfileFragment.savedpostlist);
                SavedPostFragment.lv.setAdapter(adapter);
                List<String> a = new ArrayList<>();
                for(int i = 0; i < ProfileFragment.savedpostlist.size();i++)
                {
                    a.add(ProfileFragment.savedpostlist.get(i).id);
                }
                DocumentReference userRef = FirebaseFirestore.getInstance().collection("SavedPosts").document(curUser.getUid());
                userRef.update("postIds", a)
                        .addOnSuccessListener(aVoid -> {
                            // Thành công, xử lý tương ứng (ví dụ: hiển thị thông báo)
                            Toast.makeText(activity, "You have successfully removed the post from the list", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Xảy ra lỗi, xử lý tương ứng (ví dụ: hiển thị thông báo lỗi)
                        });
            }
        });


        return view;
    }
}
