package com.example.healthcareapp.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.example.healthcareapp.Adapter.HashtagAdapter;
import com.example.healthcareapp.Adapter.PostAdapter;
import com.example.healthcareapp.Model.Comment;
import com.example.healthcareapp.Model.Noti;
import com.example.healthcareapp.Model.PostInformation;
import com.example.healthcareapp.Model.User;
import com.example.healthcareapp.Model.hashtag;
import com.example.healthcareapp.PostActivity;
import com.example.healthcareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
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
    LinearLayoutManager layoutManager ;
    private int positionLV, topView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        layoutManager = new LinearLayoutManager(getContext());
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
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                positionLV = layoutManager.findFirstVisibleItemPosition();
                View startView = mRecyclerView.getChildAt(0);
                topView = (startView == null) ? 0 : (startView.getTop() - mRecyclerView.getPaddingTop());

               // System.out.println("ps "+positionLV);
            }
        });
//        mRecyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                positionLV = mRecyclerView.computeVerticalScrollOffset();
//
//                // Use the scroll position as needed
//            }
//        });
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
                Fragment_baiviet1.listIdata = new ArrayList<>();
                Fragment_baiviet2.hashtags = new ArrayList<>();
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
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.out.println( "Listen failed."+error);
                            return;
                        }

                        if (value != null) {
                            int i = 0;
                           for (DocumentSnapshot document : value.getDocuments()){
                               Noti Info ;
                                Info = document.toObject(Noti.class);
                                Info.id = document.getId();
                                if(Info.PostownerId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
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
                           // Log.d(TAG, "Current data: null");
                        }
                    }
                });
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            int i = 0;
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Noti Info ;
//                                Info = document.toObject(Noti.class);
//                                Info.id = document.getId();
//                                if(Info.PostownerId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                                    if ( Info.Read.equals("no") ){
//                                        i = 1;
//                                    }
//                                }
//                            }
//                            if(i==1){
//                                signal.setVisibility(View.VISIBLE);
//                            }
//                            else  signal.setVisibility(View.INVISIBLE);
//                        } else {
//                            System.out.println(task.getException());
//                        }
//                    }
//                });

    }
    private void getPost(){
        db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .orderBy("posttime", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.out.println( "Listen failed."+error);
                            return;
                        }
                        postlist = new ArrayList<>();


                        //LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                        //int scrollPosition = layoutManager.findFirstVisibleItemPosition();
                       // int currentVisiblePosition = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                        for (DocumentSnapshot document : value.getDocuments()) {
                                PostInformation Info ;
                                System.out.println(document.toObject(PostInformation.class));
                                Info = document.toObject(PostInformation.class);
                                Info.id = document.getId();
                                postlist.add(Info);
                            }
                            PostAdapter adapter = new PostAdapter(postlist,getContext(), getActivity().getSupportFragmentManager(),"blog");
                           // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                          //  mRecyclerView.setLayoutManager(layoutManager);
                          //  mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerView.setAdapter(adapter);
                            layoutManager.scrollToPositionWithOffset(positionLV, topView);
                            refresh.setRefreshing(false);
                    }
                });
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                PostInformation Info ;
//                                System.out.println(document.toObject(PostInformation.class));
//                                Info = document.toObject(PostInformation.class);
//                                Info.id = document.getId();
//                                postlist.add(Info);
//                            }
//                            PostAdapter adapter = new PostAdapter(postlist,getContext(), getActivity().getSupportFragmentManager(),"blog");
//                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//                            mRecyclerView.setLayoutManager(linearLayoutManager);
//                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                            mRecyclerView.setAdapter(adapter);
//                            refresh.setRefreshing(false);
//
//                        } else {
//                            System.out.println(task.getException());
//                        }
//                    }
//                });

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
    public static ArrayList<String> hashtags = new ArrayList<>();
    public ArrayList<hashtag> mealtype = new ArrayList<>(Arrays.asList(new hashtag("Breakfast", false), new hashtag("Lunch", false), new hashtag("Dinner", false),new hashtag("Snack", false)));
    public ArrayList<hashtag> cookingstyle = new ArrayList<>(Arrays.asList(new hashtag("Fast Prep", false),new hashtag("No cooking", false), new hashtag("Fast & Easy", false),new hashtag("Slow Cooker", false),new hashtag("Grilling", false)));
    public ArrayList<hashtag> course = new ArrayList<>(Arrays.asList(new hashtag("Salads & Dressings", false), new hashtag("Desserts", false),new hashtag("Sides", false), new hashtag("Beverages & Smoothies", false), new hashtag("Soups & Stews", false)));
    public ArrayList<hashtag> mainingredient = new ArrayList<>(Arrays.asList(new hashtag(" Beans & Peas", false),new hashtag("Beef", false),new hashtag("Chicken", false),new hashtag("Egg", false),new hashtag( "Seafood", false),new hashtag("Pork", false),new hashtag("Pasta", false)));
    public ArrayList<hashtag> diettype = new ArrayList<>(Arrays.asList(new hashtag("Low-Fat", false), new hashtag("High-Protein", false),new hashtag("Vegetarian", false),new hashtag("Keto", false),new hashtag("Mediterranean", false),new hashtag("High-Fiber", false)));
    private void showCategorySheet (){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.category_layout, null);

        TextView tvNewest = bottomSheetView.findViewById(R.id.tv_newest);
        TextView tvHottest = bottomSheetView.findViewById(R.id.tv_hottest);
        TextView tvLiked = bottomSheetView.findViewById(R.id.tv_liked);
        TextView tvCommented = bottomSheetView.findViewById(R.id.tv_commented);
        TextView tvFollowing = bottomSheetView.findViewById(R.id.tv_following);
        RecyclerView G1 = bottomSheetView.findViewById(R.id.mealtype);
        RecyclerView G2 = bottomSheetView.findViewById(R.id.cookingstyle);
        RecyclerView G3 = bottomSheetView.findViewById(R.id.course);
        RecyclerView G4 = bottomSheetView.findViewById(R.id.mainingredient);
        RecyclerView G5 = bottomSheetView.findViewById(R.id.diettype);
        TextView ok = bottomSheetView.findViewById(R.id.tv_ok);
        TextView cancel = bottomSheetView.findViewById(R.id.tv_cancel);

        for(int i = 0; i < mealtype.size(); i++){
            if(hashtags.contains(mealtype.get(i).getName())){
                mealtype.get(i).setTick(true);
            }
        }
        for(int i = 0; i < cookingstyle.size(); i++){
            if(hashtags.contains(cookingstyle.get(i).getName())){
                cookingstyle.get(i).setTick(true);
            }
        }
        for(int i = 0; i < course.size(); i++){
            if(hashtags.contains(course.get(i).getName())){
                course.get(i).setTick(true);
            }
        }
        for(int i = 0; i < mainingredient.size(); i++){
            if(hashtags.contains(mainingredient.get(i).getName())){
                mainingredient.get(i).setTick(true);
            }
        }
        for(int i = 0; i < diettype.size(); i++){
            if(hashtags.contains(diettype.get(i).getName())){
                diettype.get(i).setTick(true);
            }
        }
        HashtagAdapter adapter1 = new HashtagAdapter(this.getActivity(), mealtype, true);
        G1.setAdapter(adapter1);
        G1.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));

        HashtagAdapter adapter2 = new HashtagAdapter(this.getActivity(), cookingstyle, true);
        G2.setAdapter(adapter2);
        G2.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));

        HashtagAdapter adapter3 = new HashtagAdapter(this.getActivity(), course, true);
        G3.setAdapter(adapter3);
        G3.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));

        HashtagAdapter adapter4 = new HashtagAdapter(this.getActivity(), mainingredient, true);
        G4.setAdapter(adapter4);
        G4.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));

        HashtagAdapter adapter5 = new HashtagAdapter(this.getActivity(), diettype, true);
        G5.setAdapter(adapter5);
        G5.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));
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
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                refresh.setRefreshing(true);
                postlist = new ArrayList<>();

                getUnsortedPosts(new PostDataCallback() {
                    @Override
                    public void onPostDataReceived(ArrayList<PostInformation> posts) {
                        for (PostInformation post : posts) {
                            boolean containsAllHashtags = true;
                            for (String hashtag : hashtags) {
                                if (!post.Hashtag.contains(hashtag)) {
                                    containsAllHashtags = false;
                                    break;
                                }
                            }
                            if (containsAllHashtags) {
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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        behavior.setPeekHeight(2200);
        bottomSheetDialog.show();
    }
}