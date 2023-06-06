package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Fragments.ProfileFragment;
import com.example.healthcareapp.MainActivity;
import com.example.healthcareapp.Model.Noti;
import com.example.healthcareapp.Model.User;
import com.example.healthcareapp.R;
import com.example.healthcareapp.service.FcmNotificationsSender;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private Context mContext;
    private List<User> mUsers;
    private boolean isFargment;
    FirebaseUser curUser;
    public UserAdapter(Context mContext, List<User> mUsers, boolean isFargment) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isFargment = isFargment;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        curUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(position);
        holder.btnFollow.setVisibility(View.VISIBLE);

        holder.username.setText(user.getName());
        try {
            Picasso.get().load(user.getImg()).into(holder.imageProfile);
        } catch (Exception e){
            Picasso.get().load(R.drawable.user).into(holder.imageProfile);
        }

        isFollowed(user.getId() , holder.btnFollow);

        if (user.getId().equals(curUser.getUid())){
            holder.btnFollow.setVisibility(View.GONE);
        }

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btnFollow.getText().toString().equalsIgnoreCase(("follow"))){
                    FirebaseDatabase.getInstance().getReference().child("Follow").
                            child((curUser.getUid())).child("following").child(user.getId()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Follow").
                            child(user.getId()).child("followers").child(curUser.getUid()).setValue(true);
                    Noti item = new Noti();
                    item.PostownerId = user.getId();
                    item.guestId = curUser.getUid();
                    item.classify = "follow";
                    item.postid = "";
                    item.message = " đang follow bạn.";
                    item.Read = "no";
                    item.time = String.valueOf(System.currentTimeMillis());
                    FirebaseFirestore.getInstance().collection("Notification")
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
                            .child(user.getId())
                            .child("follow")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                sendNotification(user.getId());
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").
                            child((curUser.getUid())).child("following").child(user.getId()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow").
                            child(user.getId()).child("followers").child(curUser.getUid()).removeValue();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFargment) {
                    ProfileFragment fragment = new ProfileFragment();
                    Bundle args = new Bundle();
                    args.putString("userId", user.getId());
                    fragment.setArguments(args);
                    ((MainActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
                } else {
//                    Intent intent = new Intent(mContext, MainActivity.class);
//                    intent.putExtra("userId", user.getId());
//                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageProfile;
        public TextView username;
        public Button btnFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.img_profile);
            username = itemView.findViewById(R.id.username);
            btnFollow = itemView.findViewById(R.id.btn_follow);
        }
    }
    private void isFollowed(final String id, final Button btnFollow) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(curUser.getUid())
                .child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(id).exists())
                    btnFollow.setText("unfollow");
                else
                    btnFollow.setText("follow");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    String userToken;
    private void sendNotification(String userId){

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
                        userToken, "Social Food Blog", FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + " started following you!", mContext
                );
                notificationsSender.sendNotification();
            }
        }, 3000);
    }
}
