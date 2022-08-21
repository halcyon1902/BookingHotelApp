package com.example.adminbookinghotel.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adminbookinghotel.Model.UserAdmin;
import com.example.adminbookinghotel.R;
import com.example.adminbookinghotel.SideBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Account extends SideBar {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView updateImage;
    private MaterialCardView updateProfile, updatePassword;
    private TextInputEditText edt_email;
    private TextInputEditText edt_phone;
    private TextInputEditText edt_name;
    private CircleImageView profile_image;
//    private String userId;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private Button btn_save;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_account);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_account, null, false);
        mDraweLayout.addView(v, 0);

        initUI();
        displayProfile();

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_save.setVisibility(View.VISIBLE);
                edt_name.setEnabled(true);
                edt_phone.setEnabled(true);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String userName =edt_name.getText().toString().trim();
                String phone =edt_phone.getText().toString().trim();
                updateUser(userName,phone);

            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, UpdatePassword.class));
            }
        });

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
                btn_save.setVisibility(View.VISIBLE);
                edt_name.setEnabled(true);
                edt_phone.setEnabled(true);

            }
        });
    }

    private void openFileChoose() {
        Intent intent = new Intent();
        intent.setAction((Intent.ACTION_GET_CONTENT));
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(profile_image);
        }
    }

    private void updateUser(String userName, String phone) {

        if (edt_name.getText().toString().trim().isEmpty()) {
            progressDialog.dismiss();
            edt_name.setError("Please enter full name!");
            edt_name.requestFocus();

            return;
        }

        if (edt_phone.getText().toString().length() != 10) {
            progressDialog.dismiss();
            edt_phone.setError("Phone number wrong format!");
            edt_phone.requestFocus();
            return;
        }

        HashMap User = new HashMap<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        User.put("name", userName);
        User.put("phone", phone);

        databaseReference = FirebaseDatabase.getInstance().getReference("admin");
        if(mImageUri != null){

            StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("Admin_Avatar");
            final StorageReference ImageName = ImageFolder.child("Image"+mImageUri.getLastPathSegment());

            ImageName.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    final Uri downloadUri = uriTask.getResult();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("image", downloadUri.toString());
                    databaseReference.child(userId).updateChildren(hashMap);
                }
            });

        }
        databaseReference.child(userId).updateChildren(User).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Account.this, "Update Profile successful!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_save.setVisibility(View.INVISIBLE);
        edt_name.setEnabled(false);
        edt_phone.setEnabled(false);
        progressDialog.dismiss();


    }

    private void displayProfile() {
        progressDialog.show();
//        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        userId = auth.getUid();
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("admin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAdmin userAdmin;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getKey().equals(userId)) {
                        userAdmin = child.getValue(UserAdmin.class);
                        assert userAdmin != null;
                        edt_email.setText(userAdmin.getEmail());
                        edt_phone.setText(userAdmin.getPhone());
                        edt_name.setText(userAdmin.getName());
                        if (userAdmin.getImage() == null) {
                            Drawable drawable = getResources().getDrawable(R.drawable.avatar);
                            profile_image.setImageDrawable(drawable);
                        } else {
//                            Glide.with(requireActivity()).load(image).into(profile_image);
                            Picasso.get().load(userAdmin.getImage()).fit().centerCrop().into(profile_image);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        progressDialog.dismiss();
    }

    private void initUI() {
        edt_email = findViewById(R.id.tiet_email);
        edt_phone = findViewById(R.id.tiet_phone);
        edt_name = findViewById(R.id.tiet_fullname);
        updateImage = findViewById(R.id.updateImage);
        updateProfile = findViewById(R.id.updateProfile);
        updatePassword = findViewById(R.id.updatePassword);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait....");
        profile_image = findViewById(R.id.profile_image);
        btn_save = findViewById(R.id.btn_Save);
    }
}