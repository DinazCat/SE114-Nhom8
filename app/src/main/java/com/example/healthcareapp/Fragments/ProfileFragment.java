package com.example.healthcareapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.healthcareapp.Adapter.PostAdapter;
import com.example.healthcareapp.Adapter.UserAdapter;
import com.example.healthcareapp.Model.Noti;
import com.example.healthcareapp.Model.User;
import com.example.healthcareapp.Model.PostInformation;
import com.example.healthcareapp.R;
import com.example.healthcareapp.service.FcmNotificationsSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private String userId;
    private ImageView userimg;
    private TextView username, about;
    private Button userbtn;
    private RecyclerView mRecyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser curUser = auth.getCurrentUser();
    public static ArrayList<PostInformation> postlist;
    public static ArrayList<PostInformation> savedpostlist;
    private ArrayList<User> followerlist;
    private ArrayList<User> followinglist;
    private LinearLayout bt_posts, bt_followers, bt_following;
    private TextView postsCount, followersCount, followingCount;
    private boolean isFollowed = false;
    private String userToken;
    private ImageButton menu;
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
        userbtn = view.findViewById(R.id.userbtn);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        bt_posts = view.findViewById(R.id.posts);
        bt_followers = view.findViewById(R.id.followers);
        bt_following = view.findViewById(R.id.following);
        postsCount = view.findViewById(R.id.tv_posts_count);
        followersCount = view.findViewById(R.id.tv_followers_count);
        followingCount = view.findViewById(R.id.tv_following_count);

        Bundle args = getArguments();
        menu = view.findViewById(R.id.btn_menu);
        String m = args.getString("userId");
        String n = curUser.getUid();
        if(!n.equals(m))
        {
            menu.setVisibility(View.INVISIBLE);
        }
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.inflate(R.menu.privatemenu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.storage:
                                SavedPostFragment fragment = new SavedPostFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                return true;
                        }

                        return false;
                    }
                });
            }
        });

        if (args != null) {
            userId = args.getString("userId");
            readUserData();
            readUserPost();
            readFollowers();
            readFollowing();
            readSavedPostId();
            if(userId.equals(curUser.getUid())){
                userbtn.setText("Edit Profile");

                userbtn.setOnClickListener(new View.OnClickListener() {
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
            }
            else{
                if(isFollowed)
                    userbtn.setText("Unfollow");
                else userbtn.setText("Follow");
                userbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (userbtn.getText().toString().equalsIgnoreCase(("follow"))){
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
                            userbtn.setText("Unfollow");
                            followersCount.setText("" + (followerlist.size() + 1));
                            Noti item = new Noti();
                            item.PostownerId = userId;
                            item.guestId = curUser.getUid();
                            item.classify = "follow";
                            item.postid = "";
                            item.message = " started following you!";
                            item.Read = "no";
                            item.time = String.valueOf(System.currentTimeMillis());
                            db.collection("Notification")
                                    .add(item)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            System.out.println("send noti success");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            System.out.println(e);
                                        }
                                    });

                            FirebaseDatabase.getInstance().getReference()
                                    .child("notificationSetting")
                                    .child(userId)
                                    .child("follow")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                sendNotification();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                        }
                        else {
                            userbtn.setText("Follow");
                            followersCount.setText("" + (followerlist.size() - 1));
                        }
                    }
                });
            }


            bt_posts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PostAdapter adapter = new PostAdapter(postlist,getContext(), getActivity().getSupportFragmentManager(),"profile");
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
        query.orderBy("posttime", com.google.firebase.firestore.Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    System.out.println( "Listen failed."+error);
                    return;
                }
                postlist = new ArrayList<>();
                for (DocumentSnapshot document : value.getDocuments()) {
                    PostInformation Info ;
                    System.out.println(document.toObject(PostInformation.class));
                    Info = document.toObject(PostInformation.class);
                    Info.id = document.getId();
                    postlist.add(Info);
                }
                if(isAdded()) {
                    PostAdapter adapter = new PostAdapter(postlist, getContext(), getActivity().getSupportFragmentManager(), "profile");
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.setAdapter(adapter);
                    postsCount.setText("" + postlist.size());
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
                                if(!userId.equals(curUser.getUid())) {
                                    if (isFollowed)
                                        userbtn.setText("Unfollow");
                                    else userbtn.setText("Follow");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            System.out.println(databaseError);
                        }
                    });
                }
                followersCount.setText("" + followerlist.size());
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
                followingCount.setText("" + followinglist.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readSavedPostId()
    {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("SavedPosts").document(curUser.getUid());
//        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        List<String> postIds = (List<String>) document.get("postIds");
//                        readSavedPost(postIds);
//                        savedCount.setText("" + postIds.size());
//                    }
//                }
//            };
//    });
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                savedpostlist = new ArrayList<>();
                List<String> postIds = (List<String>) value.get("postIds");
                if(postIds != null) {
                    readSavedPost(postIds);
                }
            }
        });
    }
    private void readSavedPost(List<String> Id)
    {
        for(int i = 0; i < Id.size(); i++)
        {
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("posts").document(Id.get(i));
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            PostInformation Info ;
                            Info = document.toObject(PostInformation.class);
                            Info.id = document.getId();
                            savedpostlist.add(Info);
                        }
                    }
                };
            });
        }
    }
    private void sendNotification(){
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userToken = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                        userToken, "Social Food Blog", auth.getCurrentUser().getDisplayName() + " started following you!", getContext()
                );
                notificationsSender.sendNotification();
            }
        }, 3000);
    }
}
