package com.example.healthcareapp.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.healthcareapp.Adapter.PostAdapter;
import com.example.healthcareapp.Model.Comment;
import com.example.healthcareapp.Model.Noti;
import com.example.healthcareapp.Model.PostInformation;
import com.example.healthcareapp.Model.User;
import com.example.healthcareapp.PostActivity;
import com.example.healthcareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BlogFragment extends Fragment {
    public static ArrayList<PostInformation> postlist;
    ListView listp ;
    private FirebaseFirestore db;
    public static ArrayList<User> friendlist;
    DatabaseReference databaseReference;
    ImageButton  notibtn;
    ImageButton Searchbtn, btFilter;
    ImageView imgUser, signal;
    TextView tvAddpost;
    SwipeRefreshLayout refresh;
    RecyclerView mRecyclerView;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser curUser = auth.getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        friendlist = new ArrayList<>();
        getFrienduknow("followers");
        getFrienduknow("following");
        postlist = new ArrayList<>();
        signal = view.findViewById(R.id.signal);
        refresh = view.findViewById(R.id.refresh);
        btFilter = view.findViewById(R.id.bt_filter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postlist = new ArrayList<>();
                checkNoti();
                getPost();
            }
        });
        postlist = new ArrayList<>();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        getPost();
        checkNoti();
        Searchbtn = view.findViewById(R.id.search);
        Searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment fragment = new SearchFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        notibtn=view.findViewById(R.id.noti);
        notibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment fragment = new NotificationFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        imgUser = view.findViewById(R.id.img_avatar);
        tvAddpost = view.findViewById(R.id.tv_addPost);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser curUser = auth.getCurrentUser();
        if(curUser.getPhotoUrl() != null) {
            Picasso.get().load(curUser.getPhotoUrl()).into(imgUser);
        }else {
            String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
            Picasso.get().load(uri).into(imgUser);
        }

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = curUser.getUid();
                ProfileFragment fragment = new ProfileFragment();
                Bundle args = new Bundle();
                args.putString("userId", userId);
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        tvAddpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostActivity.thaotac="push";
                AddImgFragment.images = new ArrayList<>();
                startActivity(new Intent(getContext(), PostActivity.class));
            }
        });
        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategorySheet();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private void checkNoti(){

        db = FirebaseFirestore.getInstance();
        db.collection("Notification")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Noti Info ;
                                Info = document.toObject(Noti.class);
                                Info.id = document.getId();
                                if(!Info.PostownerId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    if ( Info.Read.equals("no") ){
                                        i = 1;
                                    }
                                }
                            }
                            if(i==1){
                                signal.setVisibility(View.VISIBLE);
                            }
                            else  signal.setVisibility(View.INVISIBLE);
                        } else {
                            System.out.println(task.getException());
                        }
                    }
                });

    }
    private void getPost(){
        db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .orderBy("posttime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PostInformation Info ;
                                System.out.println(document.toObject(PostInformation.class));
                                Info = document.toObject(PostInformation.class);
                                Info.id = document.getId();
                                postlist.add(Info);
                            }
                            PostAdapter adapter = new PostAdapter(postlist,getContext(), getActivity().getSupportFragmentManager(),"blog");
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerView.setAdapter(adapter);
                            refresh.setRefreshing(false);

                        } else {
                            System.out.println(task.getException());
                        }
                    }
                });

    }
    public void getFrienduknow(String path)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Follow").child(curUser.getUid()).child(path);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //arr = new ArrayList<>();
                final long count = dataSnapshot.getChildrenCount();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String followerID = snapshot.getKey();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(followerID);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            friendlist.add(user);


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
    public interface PostDataCallback {
        void onPostDataReceived(ArrayList<PostInformation> posts);
    }
    private void getUnsortedPosts(PostDataCallback callback){
        ArrayList<PostInformation> posts = new ArrayList<>();
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
                                posts.add(Info);
                            }
                            callback.onPostDataReceived(posts);
                        } else {
                            System.out.println(task.getException());
                        }
                    }
                });
    }
    private void showCategorySheet (){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.category_layout, null);


        TextView tvNewest = bottomSheetView.findViewById(R.id.tv_newest);
        TextView tvHottest = bottomSheetView.findViewById(R.id.tv_hottest);
        TextView tvLiked = bottomSheetView.findViewById(R.id.tv_liked);
        TextView tvCommented = bottomSheetView.findViewById(R.id.tv_commented);
        TextView tvFollowing = bottomSheetView.findViewById(R.id.tv_following);

        tvNewest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                refresh.setRefreshing(true);
                postlist = new ArrayList<>();
                getPost();
            }
        });
        tvHottest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                refresh.setRefreshing(true);
                postlist = new ArrayList<>();
                getUnsortedPosts(new PostDataCallback() {
                    @Override
                    public void onPostDataReceived(ArrayList<PostInformation> posts) {
                        Collections.sort(posts, PostInformation.sortByLikesDescending);
                        postlist = posts;
                        PostAdapter adapter = new PostAdapter(postlist,getContext(), getActivity().getSupportFragmentManager(),"blog");
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        mRecyclerView.setAdapter(adapter);
                        refresh.setRefreshing(false);
                    }
                });
            }
        });
        tvLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                refresh.setRefreshing(true);
                postlist = new ArrayList<>();
                getUnsortedPosts(new PostDataCallback() {
                    @Override
                    public void onPostDataReceived(ArrayList<PostInformation> posts) {
                        for (PostInformation post : posts) {
                            if (post.likes.contains(auth.getCurrentUser().getUid())) {
                                postlist.add(post);
                            }
                        }
                        PostAdapter adapter = new PostAdapter(postlist,getContext(), getActivity().getSupportFragmentManager(),"blog");
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        mRecyclerView.setAdapter(adapter);
                        refresh.setRefreshing(false);
                    }
                });
            }
        });
        tvCommented.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                refresh.setRefreshing(true);
                postlist = new ArrayList<>();
                ArrayList<Comment> allComments = new ArrayList<>();
                try {
                    db.collection("comments").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Comment comment = document.toObject(Comment.class);
                                            comment.setId(document.getId());
                                            allComments.add(comment);
                                        }

                                        getUnsortedPosts(new PostDataCallback() {
                                            @Override
                                            public void onPostDataReceived(ArrayList<PostInformation> posts) {
                                                for (PostInformation post : posts) {
                                                    List<String> commentIds = post.comments;
                                                    ArrayList<Comment> commentsOfPost = new ArrayList<>();
                                                    for (Comment comment : allComments) {
                                                        if (commentIds.contains(comment.getId())) {
                                                            commentsOfPost.add(comment);
                                                        }
                                                    }
                                                    for (Comment comment : commentsOfPost) {
                                                        if (comment.getUserId().equals(auth.getCurrentUser().getUid())) {
                                                            postlist.add(post);
                                                            break;
                                                        }
                                                    }
                                                }
                                                PostAdapter adapter = new PostAdapter(postlist, getContext(), getActivity().getSupportFragmentManager(), "blog");
                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                                                mRecyclerView.setLayoutManager(linearLayoutManager);
                                                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                                mRecyclerView.setAdapter(adapter);
                                                refresh.setRefreshing(false);
                                            }
                                        });

                                    } else {
                                        System.out.println(task.getException());
                                    }

                                }
                            });
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        });
        tvFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                refresh.setRefreshing(true);
                postlist = new ArrayList<>();
                List<String> followingIds = new ArrayList<>();
                FirebaseDatabase.getInstance().getReference()
                        .child("Follow").child(auth.getCurrentUser().getUid()).child("following")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                                    String followingID = datasnapshot.getKey();
                                    followingIds.add(followingID);
                                }
                                getUnsortedPosts(new PostDataCallback() {
                                    @Override
                                    public void onPostDataReceived(ArrayList<PostInformation> posts) {
                                        for (PostInformation post : posts) {
                                            if (followingIds.contains(post.userid)) {
                                                postlist.add(post);
                                            }
                                        }
                                        PostAdapter adapter = new PostAdapter(postlist, getContext(), getActivity().getSupportFragmentManager(), "blog");
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                                        mRecyclerView.setLayoutManager(linearLayoutManager);
                                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                        mRecyclerView.setAdapter(adapter);
                                        refresh.setRefreshing(false);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}