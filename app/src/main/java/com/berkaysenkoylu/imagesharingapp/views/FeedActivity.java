package com.berkaysenkoylu.imagesharingapp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.berkaysenkoylu.imagesharingapp.R;
import com.berkaysenkoylu.imagesharingapp.databinding.ActivityFeedBinding;
import com.google.firebase.auth.FirebaseAuth;

public class FeedActivity extends AppCompatActivity {

    ActivityFeedBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
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
                auth.signOut();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (item.getItemId() == R.id.toolbar_add) {
            Intent intent = new Intent(this, UploadActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}