package com.example.aprilandroid.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aprilandroid.Activities.EditProfileActivity;
import com.example.aprilandroid.Fragments.ProfileFragment;
import com.example.aprilandroid.Model.Users;
import com.example.aprilandroid.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{

    List<Users> userList;
    FragmentManager fragmentManager;

    public SearchAdapter(List<Users> userList, FragmentManager fragmentManager) {
        this.userList = userList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_item, parent, false);
        return new  SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Users users = userList.get(position);
        holder.userName.setText(users.getUserName());
        holder.userFullName.setText(users.getFullName());
        if (users.getImageUrl().equals("default")) {
           holder.userProfilePitcure.setImageResource(R.mipmap.ic_launcher_round);
        }else {

            Glide.with(holder.itemView.getContext()).load(users.getImageUrl()).into(holder.userProfilePitcure);
        }
        getFollowStatus(users, holder);
        holder.followUnfollowButton.setOnClickListener(v -> {
            if(holder.followUnfollowButton.getText().toString().equalsIgnoreCase("Follow")) {
                followUser(users, holder);
            }else{
                unFollowUser(users, holder);
            }
        });
        holder.itemView.setOnClickListener(v -> {
            SharedPreferences preferences = holder.itemView.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("profileId", users.getId());
            editor.apply();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        });
    }

    private void unFollowUser(Users users, SearchViewHolder holder) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(users.getId()).child("follower");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
        reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("follow");
        reference.child(users.getId()).removeValue();
        holder.followUnfollowButton.setText("Follow");
        holder.followUnfollowButton.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primaryBtn));
        holder.followUnfollowButton.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
    }

    private void followUser(Users users, SearchViewHolder holder) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(users.getId()).child("follower");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
        reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        reference.child(users.getId()).setValue(true);
        holder.followUnfollowButton.setText("Following");
        holder.followUnfollowButton.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grey));
        holder.followUnfollowButton.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
    }

    private void getFollowStatus(Users users, SearchViewHolder holder) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Follow").child(users.getId()).child("follower");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                        holder.followUnfollowButton.setText("Following");
                        holder.followUnfollowButton.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grey));
                        holder.followUnfollowButton.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
                    }else{
                        holder.followUnfollowButton.setText("Follow");
                        holder.followUnfollowButton.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primaryBtn));
                        holder.followUnfollowButton.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userProfilePitcure;
        TextView userName, userFullName;
        MaterialButton followUnfollowButton;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfilePitcure = itemView.findViewById(R.id.user_profile_picture);
            userName = itemView.findViewById(R.id.user_name);
            userFullName = itemView.findViewById(R.id.user_full_name);
            followUnfollowButton = itemView.findViewById(R.id.follow_unfollow_btn);

        }
    }
}
