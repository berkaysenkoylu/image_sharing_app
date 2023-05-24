package com.berkaysenkoylu.imagesharingapp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.berkaysenkoylu.imagesharingapp.R;
import com.berkaysenkoylu.imagesharingapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    // Fields
    EditText emailField;
    EditText passwordField;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);

        emailField = binding.emailTextText;
        passwordField = binding.passwordTextView;

        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            goToFeedActivity();
        }
    }

    private void goToFeedActivity() {
        Intent intent = new Intent(MainActivity.this, FeedActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginButtonPressedHandler(View view) {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (email.matches("") || password.matches("")) {
            Toast.makeText(this, "You cannot leave the fields empty!", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        goToFeedActivity();
                    } else {
                        Toast.makeText(MainActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onSignupButtonPressedHandler(View view) {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (email.matches("") || password.matches("")) {
            Toast.makeText(this, "You cannot leave the fields empty!", Toast.LENGTH_SHORT).show();
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goToFeedActivity();
                        } else {
                            Toast.makeText(MainActivity.this, "Sign up failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }
}