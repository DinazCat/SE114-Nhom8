package com.example.healthcareapp.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Adapter.PostAdapter;
import com.example.healthcareapp.Adapter.UserAdapter;
import com.example.healthcareapp.Model.PostInformation;
import com.example.healthcareapp.Model.User;
import com.example.healthcareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

LinearLayout formFriendList, formSearched;
RecyclerView listfriend, mRecyclerView;
EditText searchbox;
Button postbtn, peoplebtn;
ImageButton Backbtn;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser curUser = auth.getCurrentUser();
    private ArrayList<User> friendlist, Peoplelist;
    private ArrayList<PostInformation> PostList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        searchbox = view.findViewById(R.id.searchbox);
        Backbtn = view.findViewById(R.id.back);
        formFriendList = view.findViewById(R.id.formlistfriend);
        formSearched = view.findViewById(R.id.formsearched);
        listfriend = view.findViewById(R.id.listfriend);
        mRecyclerView = view.findViewById(R.id.listsearch);
        postbtn = view.findViewById(R.id.bvBtn);
        peoplebtn = view.findViewById(R.id.friendBtn);
        formFriendList.setVisibility(View.VISIBLE);
        formSearched.setVisibility(View.INVISIBLE);
        friendlist = new ArrayList<>(BlogFragment.friendlist);
        PostList = new ArrayList<>();
        Peoplelist = new ArrayList<>();
        Backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogFragment fragment = new BlogFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        for(int i = 0; i < friendlist.size()-1; i++)
        {
            for(int j = i+1; j < friendlist.size();j++){
               if(friendlist.get(i).getId().equals(friendlist.get(j).getId()))
               {
                   BlogFragment.friendlist.remove(i);
               }
            }

        }
            if(BlogFragment.friendlist.size() > 0)
            {
                System.out.println(BlogFragment.friendlist.size());
                UserAdapter adapter = new UserAdapter(getContext(), BlogFragment.friendlist, true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                listfriend.setLayoutManager(linearLayoutManager);
                listfriend.setItemAnimator(new DefaultItemAnimator());
                listfriend.setAdapter(adapter);
            }



        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                formFriendList.setVisibility(View.INVISIBLE);
                formSearched.setVisibility(View.VISIBLE);
                readPost();
                postbtn.setBackgroundColor(Color.parseColor("#D3FBB8"));
                peoplebtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                if(searchbox.getText().toString().equals(""))
                {
                    formFriendList.setVisibility(View.VISIBLE);
                    formSearched.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                postbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postbtn.setBackgroundColor(Color.parseColor("#D3FBB8"));
                        peoplebtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        readPost();

                    }
                });
                peoplebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postbtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        peoplebtn.setBackgroundColor(Color.parseColor("#D3FBB8"));
                        readPeople();

                    }
                });

            }
        });
        return view;
    }
    public int i = 0;

    private void readPost(){
        PostList.clear();
        db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PostInformation Info ;
                                Info = document.toObject(PostInformation.class);
                                Info.id = document.getId();
                                boolean result = Info.postFoodName.toLowerCase().contains(searchbox.getText().toString().toLowerCase());
                                if (result == true) PostList.add(Info);
                            }
                                PostAdapter adapter = new PostAdapter(PostList,getContext(), getActivity().getSupportFragmentManager(),"blog");
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                                mRecyclerView.setLayoutManager(linearLayoutManager);
                                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                mRecyclerView.setAdapter(adapter);

                        } else {
                            System.out.println(task.getException());
                        }
                    }
                });
    }
    private void readPeople(){
        Peoplelist.clear();
        databaseReference = database.getReference("users");
        Query query = databaseReference.orderByChild("id");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    boolean result = user.getName().toLowerCase().contains(searchbox.getText().toString().toLowerCase());
                    if (result == true) Peoplelist.add(user);
                }

                            System.out.println("fr");
                            UserAdapter adapter = new UserAdapter(getContext(), Peoplelist, true);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });

    }
}