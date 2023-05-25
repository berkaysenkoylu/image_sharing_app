package com.berkaysenkoylu.imagesharingapp.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.berkaysenkoylu.imagesharingapp.R;
import com.berkaysenkoylu.imagesharingapp.adapters.FeedAdapter;
import com.berkaysenkoylu.imagesharingapp.databinding.ActivityFeedBinding;
import com.berkaysenkoylu.imagesharingapp.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    ActivityFeedBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<Post> postList;
    private FeedAdapter feedAdapter;

    ListenerRegistration listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        postList = new ArrayList<Post>();

        getInitialData();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedAdapter = new FeedAdapter(postList);
        binding.recyclerView.setAdapter(feedAdapter);
    }

    private void getInitialData() {
        listener = firebaseFirestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    System.out.println("HEY LISTEN");
                    Toast.makeText(FeedActivity.this, "Something went wrong while fetching the data!", Toast.LENGTH_SHORT).show();
                } else {
                    for(DocumentSnapshot document : value.getDocuments()) {
                        Post post = new Post(
                                document.getId(),
                                document.get("email").toString(),
                                document.get("comment").toString(),
                                document.get("imageUrl").toString(),
                                document.get("userId").toString(),
                                document.getDate("date")
                        );
                        postList.add(post);
                    }
                    feedAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.toolbar_logout) {
            if (auth.getCurrentUser() != null) {
                listener.remove();
                auth.signOut();
                finish();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } else if (item.getItemId() == R.id.toolbar_add) {
            Intent intent = new Intent(this, UploadActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}