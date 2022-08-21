package com.example.adminbookinghotel.Profile;

import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adminbookinghotel.R;
import com.example.adminbookinghotel.SideBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatePassword extends SideBar {

    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressDialog pd;
    private Button btn_Back, btn_Update;
    private TextInputEditText oldpass, newpass, verifypass;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_update_password, null, false);
        mDraweLayout.addView(v, 0);

        initUI();

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing Password");
                updatePassWord();

            }
        });

    }

    private void initUI() {
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("admin");
        btn_Update = findViewById(R.id.btn_Update);
        btn_Back = findViewById(R.id.btn_Back);
        oldpass = findViewById(R.id.oldpasslog);
        newpass = findViewById(R.id.newpasslog);
        verifypass = findViewById(R.id.verifynewpasslog);
    }
    private void ChangePassword(String oldp, String newp){
        FirebaseUser user = firebaseAuth.getCurrentUser() ;
        AuthCredential authCredential = EmailAuthProvider.getCredential(userEmail, oldp);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(newp)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.dismiss();
                                        startActivity(new Intent(UpdatePassword.this, Account.class));
                                        finish();
                                        Toast.makeText(UpdatePassword.this, "Changed Password", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(UpdatePassword.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(UpdatePassword.this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatePassWord() {

        String oldp = oldpass.getText().toString().trim();
        String newp = newpass.getText().toString().trim();
        String verifyp = verifypass.getText().toString().trim();
        if (TextUtils.isEmpty(oldp)) {
            oldpass.setError("Current Password cant be empty");
            oldpass.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(newp)) {
            newpass.setError("New Password cant be empty");
            newpass.requestFocus();
            return;
        }
        if (newp.length() < 6) {
            newpass.setError("Password must not be less than 6 characters");
            newpass.requestFocus();
            return;
        }

        if (!verifypass.getText().toString().trim().equals(newp)) {
            verifypass.setError("Password does not match confirm password");
            verifypass.requestFocus();
            return;
        }
        if (newpass.getText().toString().trim().equals(oldp)) {
            newpass.setError("New password must not be the same as the current password");
            newpass.requestFocus();
            return;
        }

        pd.show();

        if( firebaseAuth.getCurrentUser() != null){
            ChangePassword(oldp,newp);
        }else{
            FirebaseUser user2 = firebaseAuth.getCurrentUser();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(userEmail, oldp)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                ChangePassword(oldp,newp);
                            }else{
                                pd.dismiss();
                                Toast.makeText(UpdatePassword.this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}