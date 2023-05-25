package com.berkaysenkoylu.imagesharingapp.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.berkaysenkoylu.imagesharingapp.databinding.FeedRowBinding;
import com.berkaysenkoylu.imagesharingapp.models.Post;
import com.berkaysenkoylu.imagesharingapp.views.FeedActivity;
import com.berkaysenkoylu.imagesharingapp.views.FeedDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedRowHolder> {
    ArrayList<Post> postList;

    public FeedAdapter(ArrayList<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public FeedRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FeedRowBinding binding = FeedRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FeedRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedRowHolder holder, int position) {
        Post post = this.postList.get(position);

        holder.feedRowBinding.feedRowEmail.setText(post.email);
        holder.feedRowBinding.feedRowComment.setText(post.comment);
        Picasso.get().load(post.imageUrl).into(holder.feedRowBinding.feedRowImage);

        if (position == this.postList.size() - 1) {
            holder.feedRowBinding.feedRowDivider.setVisibility(View.GONE);
        }

        holder.feedRowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), FeedDetail.class);
                intent.putExtra("post", post);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.postList.size();
    }

    public class FeedRowHolder extends RecyclerView.ViewHolder {
        FeedRowBinding feedRowBinding;

        public FeedRowHolder(FeedRowBinding binding) {
            super(binding.getRoot());
            feedRowBinding = binding;
        }
    }
}
