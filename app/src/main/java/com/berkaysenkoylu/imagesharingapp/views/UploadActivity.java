package com.berkaysenkoylu.imagesharingapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.berkaysenkoylu.imagesharingapp.databinding.ActivityUploadBinding;

public class UploadActivity extends AppCompatActivity {

    ActivityUploadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onImageSelectPressedHandler(View view) {}

    public void onPostButtonPressedHandler(View view) {}
}