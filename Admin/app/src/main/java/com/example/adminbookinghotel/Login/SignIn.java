package com.example.adminbookinghotel.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminbookinghotel.ManageRoom.ListRoom;
import com.example.adminbookinghotel.Model.UserAdmin;
import com.example.adminbookinghotel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    private Button btn_sign_in;
    private TextInputEditText edt_Email, edt_MatKhau;
    private ProgressDialog progressDialog;
    private TextView textView;
//    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initUi();

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignIn();
            }
        });
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(compoundButton.isChecked()){
//
//                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("remember","true");
//                    editor.apply();
//                    showToast("Checked");
//
//                }else if(!compoundButton.isChecked()){
//
//                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("remember","false");
//                    editor.apply();
//                    showToast("Unchecked");
//
//                }
//            }
//        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, FogotPassword.class));
            }
        });
    }

    private void onClickSignIn() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String strEmail = edt_Email.getText().toString().trim();
        String strMatKhau = edt_MatKhau.getText().toString().trim();

        if (!Check(strEmail)) {
            edt_Email.setError("Please enter email");
            edt_Email.requestFocus();
            return;
        }

        if (!Check(strMatKhau)) {
            edt_MatKhau.setError("Please enter password");
            edt_MatKhau.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(edt_Email.getText().toString().trim()).matches()) {
            edt_Email.setError("Email wrong format.");
            edt_Email.requestFocus();
            return;
        }

        progressDialog.show();
        auth.signInWithEmailAndPassword(strEmail, strMatKhau)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkAdmin(strEmail);
//                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            showToast("Wrong UserName or Password!");
                        }
                    }
                });
    }

    private void checkAdmin(String strEmail) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("admin");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean exist = false;
                for (DataSnapshot child : snapshot.getChildren()) {
                    UserAdmin userAdmin = child.getValue(UserAdmin.class);
                    if (userAdmin.getEmail().equals(strEmail))  {
                        if(userAdmin.isStatus()){
                            exist = true;
                            break;
                        }else {
                            showToast("Account is being banned !!");
                            progressDialog.dismiss();
                            return;
                        }
                    }
                }

                progressDialog.dismiss();
                if (exist == true) {
                    Intent intent = new Intent(SignIn.this, ListRoom.class);
                    startActivity(intent);
                    finish();
                } else {
                    showToast("Wrong UserName or Password!");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initUi() {
        progressDialog = new ProgressDialog(this);
        btn_sign_in = findViewById(R.id.btn_DangNhap);
        textView = findViewById(R.id.tv_QuenMatKhau);
        edt_MatKhau = findViewById(R.id.edt_MatKhau);
        edt_Email = findViewById(R.id.edt_TenTaiKhoan);
//        checkBox = findViewById(R.id.chbRememberMe);

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");

//        if(checkbox.equals("true")){
//            startActivity(new Intent(SignIn.this, HomeAdmin.class));
//            finish();
//        }
//        else if(checkbox.equals("false")){
//
//        }
    }
    private void showToast(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_LONG).show();
    }

    private boolean Check(String name) {
        return !TextUtils.isEmpty(name);
    }
}