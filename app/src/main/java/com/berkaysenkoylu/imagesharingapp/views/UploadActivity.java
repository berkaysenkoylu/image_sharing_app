package com.berkaysenkoylu.imagesharingapp.views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.berkaysenkoylu.imagesharingapp.databinding.ActivityUploadBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private Uri selectedImgUri;

    ActivityUploadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerLaunchers();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void onImageSelectPressedHandler(View view) {
        String permissionStr = getPermissionString();

        if (ContextCompat.checkSelfPermission(this, permissionStr) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionStr)) {
                // Show rationale
                Snackbar.make(view, "Permission is needed to access the gallery!", Snackbar.LENGTH_INDEFINITE).setAction("Grant", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Request permission
                        permissionLauncher.launch(permissionStr);
                    }
                }).show();
            } else {
                // Request permission
                permissionLauncher.launch(permissionStr);
            }
        } else {
            // We have the permission. Go to the gallery.
            goToGallery();
        }
    }

    public String getPermissionString() {
        String perm_str = "";
        if (Build.VERSION.SDK_INT >= 33) {
            perm_str = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            perm_str = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        return perm_str;
    }

    public void onPostButtonPressedHandler(View view) {
        if (selectedImgUri != null) {
            UUID uuid = UUID.randomUUID();
            String imgName = "images/" + uuid + ".jpg";

            storageRef.child(imgName).putFile(selectedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get download uri
                    StorageReference newReference = storage.getReference(imgName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            FirebaseUser currentUser = auth.getCurrentUser();

                            if (currentUser != null) {
                                String email = currentUser.getEmail();
                                String comment = binding.commentTextView.getText().toString();

                                HashMap<String, Object> newEntry = new HashMap<String, Object>();
                                newEntry.put("email", email);
                                newEntry.put("comment", comment);
                                newEntry.put("imageUrl", downloadUrl);
                                newEntry.put("userId", currentUser.getUid());
                                newEntry.put("date", FieldValue.serverTimestamp());

                                firebaseFirestore.collection("Posts").add(newEntry).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Intent intent = new Intent(UploadActivity.this, FeedActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void registerLaunchers() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent resultIntent = result.getData();
                    if (resultIntent != null) {
                        Uri uri = resultIntent.getData();
                        selectedImgUri = uri;
                        binding.imageView.setImageURI(selectedImgUri);
                    }
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    // Go to gallery
                    goToGallery();
                } else {
                    // Permission denied
                    Toast.makeText(UploadActivity.this, "Permission is needed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToGallery() {
        Intent goToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(goToGallery);
    }
}