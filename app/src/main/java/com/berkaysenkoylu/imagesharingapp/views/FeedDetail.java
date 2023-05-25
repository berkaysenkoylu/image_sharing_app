package com.berkaysenkoylu.imagesharingapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.berkaysenkoylu.imagesharingapp.R;
import com.berkaysenkoylu.imagesharingapp.databinding.ActivityFeedDetailBinding;
import com.berkaysenkoylu.imagesharingapp.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class FeedDetail extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;

    ActivityFeedDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Intent intent = getIntent();
        Post selectedPost = (Post) intent.getSerializableExtra("post");

        binding.authorView.setText(selectedPost.email);
        Picasso.get().load(selectedPost.imageUrl).into(binding.postImageView);
        binding.commentText.setText(selectedPost.comment);

        if (!user.getUid().equals(selectedPost.userId)) {
            binding.feedDetailCta.setVisibility(View.GONE);
        }


    }
}