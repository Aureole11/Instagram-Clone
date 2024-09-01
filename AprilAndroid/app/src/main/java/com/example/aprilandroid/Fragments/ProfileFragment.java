package com.example.aprilandroid.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aprilandroid.Activities.EditProfileActivity;
import com.example.aprilandroid.Activities.LoginActivity;
import com.example.aprilandroid.Model.Users;
import com.example.aprilandroid.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ProfileFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class ProfileFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public ProfileFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ProfileFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ProfileFragment newInstance(String param1, String param2) {
//        ProfileFragment fragment = new ProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    TextView userName, fullName,bio,posts, followers, following;
    String userId = "";
//    String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    CircleImageView userProfilePicture;
    MaterialButton edit_follow_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        Button logOut = view.findViewById(R.id.logOut);
        userName = view.findViewById(R.id.username);
        fullName = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        userProfilePicture = view.findViewById(R.id.user_profile_picture);
        edit_follow_btn = view.findViewById(R.id.edit_follow_btn);
        SharedPreferences preferences = requireContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        userId = preferences.getString("profileId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        if (userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            edit_follow_btn.setText("Edit Profile");
            edit_follow_btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey));
            edit_follow_btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        }else{
            checkFollow();
        }
        edit_follow_btn.setOnClickListener(v -> {
            String btnText = edit_follow_btn.getText().toString();
            switch (btnText){
                case "Edit Profile":
                    editProfile();
                    break;
                case "Follow":
                    followUser();
                    break;
                case "Following":
                    unfollowUser();
                    break;
            }
        });
        logOut.setOnClickListener(this::logOutUser);
        getUserData();
        getFollowers();
        getFollowing();
        getPosts();
        return view;
    }

    private void unfollowUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("follow").child(userId).child("follower");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
        reference = FirebaseDatabase.getInstance().getReference().child("follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("follow");
        reference.child(userId).removeValue();
        edit_follow_btn.setText("Follow");
        edit_follow_btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primaryBtn));
        edit_follow_btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
    }


    private void followUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("follow").child(userId).child("follower");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
        reference = FirebaseDatabase.getInstance().getReference().child("follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        reference.child(userId).setValue(true);
        edit_follow_btn.setText("Following");
        edit_follow_btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey));
        edit_follow_btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
    }

    private void editProfile() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        startActivity(intent);
    }


    private void checkFollow() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(userId).exists()){
                    edit_follow_btn.setText("Following");
                    edit_follow_btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    edit_follow_btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                }else{
                    edit_follow_btn.setText("Follow");
                    edit_follow_btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primaryBtn));
                    edit_follow_btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = 0;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (Objects.requireNonNull(dataSnapshot.child("publisherId").getValue()).toString().equals(userId)){
                            counter++;
                        }
                    }
                }
                posts.setText(counter + "\nPosts");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFollowing() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("follow").child(userId).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.setText(snapshot.getChildrenCount()+ "\nFollowing");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFollowers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("follow").child(userId).child("follower");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.setText(snapshot.getChildrenCount()+ "\nFollower");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                userName.setText(user.getUserName());
                fullName.setText(user.getFullName());
                bio.setText(user.getBio());
                if (user.getBio().equals("")){
                    bio.setVisibility(View.GONE);
                }else {
                    bio.setVisibility(View.VISIBLE);
                }
                if (user.getImageUrl().equals("default")){
                    userProfilePicture.setImageResource(R.mipmap.ic_launcher);
                }else{

                    Glide.with(requireActivity()).load(user.getImageUrl()).into(userProfilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void logOutUser(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent =new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
