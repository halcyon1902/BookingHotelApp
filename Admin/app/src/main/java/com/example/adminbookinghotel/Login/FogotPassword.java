package com.example.adminbookinghotel.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminbookinghotel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FogotPassword extends AppCompatActivity {

    private EditText edt_email;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogot_password);

        edt_email = findViewById(R.id.edt_fogotPass);
        Button button = findViewById(R.id.btn_XacNhan);
        auth = FirebaseAuth.getInstance();
        Button btn_back = findViewById(R.id.btn_Back);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPassword();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
            }
        });
    }

    private boolean Check(String name) {
        return !TextUtils.isEmpty(name);
    }

    private void ForgotPassword() {
        String strEmail = edt_email.getText().toString().trim();
        if (!Check(strEmail)) {
            edt_email.setError("Please enter Email!");
            edt_email.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString().trim()).matches()) {
            edt_email.setError("Email wrong format!");
            edt_email.requestFocus();
        } else {
            auth.sendPasswordResetEmail(strEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(FogotPassword.this, SignIn.class));
                        ShowToast("Check mail!");
                        finish();
                    } else {
                        ShowToast("Email not exist!");
                    }
                }
            });
        }
    }

    private void ShowToast(String messenger) {
        Toast.makeText(this, messenger, Toast.LENGTH_SHORT).show();
    }
}