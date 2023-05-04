package com.example.healthcareapp.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.healthcareapp.Adapter.UserAdapter;
import com.example.healthcareapp.CustomAdapter3;
import com.example.healthcareapp.LoginActivity;
import com.example.healthcareapp.Model.User;
import com.example.healthcareapp.PostInformation;
import com.example.healthcareapp.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private String userId;
    private ImageView userimg;
    private TextView username, about;
    private Button btn1, btn2;
    private RecyclerView mRecyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser curUser = auth.getCurrentUser();
    private ArrayList<PostInformation> postlist;
    private ArrayList<User> followerlist;
    private ArrayList<User> followinglist;
    private LinearLayout bt_posts, bt_followers, bt_following;
    private TextView postsCount, followersCount, followingCount;
    private boolean isFollowed = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userimg = view.findViewById(R.id.img_user);
        username = view.findViewById(R.id.tv_name);
        about = view.findViewById(R.id.tv_about);
        btn1 = view.findViewById(R.id.bt_1);
        btn2 = view.findViewById(R.id.bt_2);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        bt_posts = view.findViewById(R.id.posts);
        bt_followers = view.findViewById(R.id.followers);
        bt_following = view.findViewById(R.id.following);
        postsCount = view.findViewById(R.id.tv_posts_count);
        followersCount = view.findViewById(R.id.tv_followers_count);
        followingCount = view.findViewById(R.id.tv_following_count);

        Bundle args = getArguments();
        if (args != null) {
            userId = args.getString("userId");
            readUserData();
            readUserPost();
            readFollowers();
            readFollowing();
            if(userId.equals(curUser.getUid())){
                btn1.setText("Edit");
                btn2.setText("Logout");
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userId = curUser.getUid();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.frame_layout, new EditProfileFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GoogleSignInOptions gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                        GoogleSignInClient gClient = GoogleSignIn.getClient(getContext(), gOptions);
                        if (AccessToken.getCurrentAccessToken() != null && auth.getCurrentUser() != null) {
                            auth.signOut();
                            LoginManager.getInstance().logOut();
                        }
                        else if (gClient != null && auth.getCurrentUser() != null) {
                            auth.signOut();
                            gClient.signOut();
                        }
                        else if (auth.getCurrentUser() != null) {
                            auth.signOut();
                        }
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
            }
            else{
                if(isFollowed)
                    btn1.setText("Unfollow");
                else btn1.setText("Follow");
                btn2.setText("Chat");
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (btn1.getText().toString().equalsIgnoreCase(("follow"))){
                            FirebaseDatabase.getInstance().getReference().child("Follow").
                                    child((curUser.getUid())).child("following").child(userId).setValue(true);

                            FirebaseDatabase.getInstance().getReference().child("Follow").
                                    child(userId).child("followers").child(curUser.getUid()).setValue(true);

                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Follow").
                                    child((curUser.getUid())).child("following").child(userId).removeValue();

                            FirebaseDatabase.getInstance().getReference().child("Follow").
                                    child(userId).child("followers").child(curUser.getUid()).removeValue();
                        }
                        isFollowed = !isFollowed;
                        if(isFollowed) {
                            btn1.setText("Unfollow");
                            followersCount.setText("" + (followerlist.size() + 1));
                        }
                        else {
                            btn1.setText("Follow");
                            followersCount.setText("" + (followerlist.size() - 1));
                        }
                    }
                });
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }


            bt_posts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomAdapter3 adapter = new CustomAdapter3(postlist,getContext(), getActivity().getSupportFragmentManager());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.setAdapter(adapter);
                    bt_posts.setBackgroundColor(Color.parseColor("#D3FBB8"));
                    bt_followers.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    bt_following.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            });
            bt_followers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserAdapter adapter = new UserAdapter(getContext(), followerlist, true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.setAdapter(adapter);
                    bt_followers.setBackgroundColor(Color.parseColor("#D3FBB8"));
                    bt_posts.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    bt_following.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            });
            bt_following.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserAdapter adapter = new UserAdapter(getContext(), followinglist, true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.setAdapter(adapter);
                    bt_following.setBackgroundColor(Color.parseColor("#D3FBB8"));
                    bt_followers.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    bt_posts.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            });
        }
        // Inflate the layout for this fragment
        return view;
    }
    private void readUserData(){
        databaseReference = database.getReference("users");
        Query query = databaseReference.orderByChild("id").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String name = ds.child("name").getValue() + "";
                    String About = ds.child("about").getValue() + "";
                    String img = ds.child("img").getValue() + "";
                    username.setText(name);
                    if(About.isEmpty()) About = "Nothing added.";
                    about.setText(About);
                    try {
                        Picasso.get().load(img).into(userimg);
                    } catch (Exception e){
                        String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
                        Picasso.get().load(uri).into(userimg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readUserPost(){
        collectionReference = db.collection("posts");
        com.google.firebase.firestore.Query query = collectionReference.whereEqualTo("userid", userId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    postlist = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        PostInformation Info ;
                        System.out.println(document.toObject(PostInformation.class));
                        Info = document.toObject(PostInformation.class);
                        postlist.add(Info);
                    }
                    CustomAdapter3 adapter = new CustomAdapter3(postlist,getContext(), getActivity().getSupportFragmentManager());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.setAdapter(adapter);
                    postsCount.setText("" + postlist.size());
                }
                else {
                    System.out.println(task.getException());
                }
            }
        });
    }
    private void readFollowers(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Follow").child(userId).child("followers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followerlist = new ArrayList<>();
                final long count = dataSnapshot.getChildrenCount();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String followerID = snapshot.getKey();
                    if(!isFollowed && followerID.equals(curUser.getUid())){
                        isFollowed = true;
                    }
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(followerID);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            followerlist.add(user);
                            if (followerlist.size() == count) {
                                // all listeners have completed, update UI
                                followersCount.setText("" + followerlist.size());
                                if(isFollowed)
                                    btn1.setText("Unfollow");
                                else btn1.setText("Follow");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            System.out.println(databaseError);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });
    }
    private void readFollowing(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Follow").child(userId).child("following");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followinglist = new ArrayList<>();
                final long count = dataSnapshot.getChildrenCount();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String followingID = snapshot.getKey();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(followingID);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            followinglist.add(user);
                            if (followinglist.size() == count) {
                                followingCount.setText("" + followinglist.size());
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            System.out.println(databaseError);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}