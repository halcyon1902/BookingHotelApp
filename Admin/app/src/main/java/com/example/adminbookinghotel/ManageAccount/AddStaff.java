package com.example.adminbookinghotel.ManageAccount;

import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adminbookinghotel.Adapter.CategoryAdapter;
import com.example.adminbookinghotel.ManageRoom.ListRoom;
import com.example.adminbookinghotel.Model.Category;
import com.example.adminbookinghotel.R;
import com.example.adminbookinghotel.SideBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddStaff extends SideBar {
// test lần 1
    private Button  sign_upButton;
    private EditText edt_HoTen, edt_Email, edt_Sdt, edt_MatKhau, edt_XacNhanMK;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private Spinner spnCategory;
    private CategoryAdapter categoryAdapter;
    private String permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_add_staff, null, false);
        mDraweLayout.addView(v, 0);

        initUI();
        sign_upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignUP();
            }
        });

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                permission = categoryAdapter.getItem(position).getType();
                showToast(categoryAdapter.getItem(position).getType());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initUI() {
        sign_upButton = findViewById(R.id.btn_sign_up);
        edt_Email = findViewById(R.id.edt_Email);
        edt_Sdt = findViewById(R.id.edt_phone);
        edt_HoTen = findViewById(R.id.edt_fullname);
        edt_MatKhau = findViewById(R.id.edt_password);
        edt_XacNhanMK = findViewById(R.id.edt_verifypass);
        progressDialog = new ProgressDialog(this);
        spnCategory = findViewById(R.id.spn_category);
        categoryAdapter = new CategoryAdapter(getApplicationContext(), R.layout.item_selected, getListCategory());
        spnCategory.setAdapter(categoryAdapter);
    }

    private List<Category> getListCategory() {
        List<Category> list = new ArrayList<>();
        list.add(new Category("1"));
        list.add(new Category("2"));

        return  list;
    }

    private boolean Check(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        return true;
    }

    public void onClickSignUP() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String strEmail = edt_Email.getText().toString().trim();
        String strMatKhau = edt_MatKhau.getText().toString().trim();
        String strHoTen = edt_HoTen.getText().toString().trim();
        String strSdt = edt_Sdt.getText().toString().trim();
        String mpermission = permission;

        if (!Check(strEmail)) {
            edt_Email.setError("Please enter email");
            edt_Email.requestFocus();
            return;
        }

        if (!Check(strHoTen)) {
            edt_HoTen.setError("Please enter full name");
            edt_HoTen.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(edt_Email.getText().toString().trim()).matches()) {
            edt_Email.setError("Email wrong format");
            edt_Email.requestFocus();
            return;
        }

        if (strSdt.length() != 10) {
            edt_Sdt.setError("Phone number wrong format!");
            edt_Sdt.requestFocus();
            return;
        }


        if (strMatKhau.length() < 6) {
            edt_MatKhau.setError("Password must be bigger than 6 characters!");
            edt_MatKhau.requestFocus();
            return;
        }
        if (!Check(strMatKhau)) {
            edt_MatKhau.setError("Please enter password!");
            edt_MatKhau.requestFocus();
            return;
        }
        if (!edt_XacNhanMK.getText().toString().trim().equals(strMatKhau)) {
            edt_XacNhanMK.setError("Password doesn't match!");
            edt_XacNhanMK.requestFocus();
            return;
        }
        progressDialog.show();
        auth.createUserWithEmailAndPassword(strEmail, strMatKhau)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            databaseReference = FirebaseDatabase.getInstance().getReference("admin");
                            Map<String, Object> user = new HashMap<>();
                            user.put("email", strEmail);
                            user.put("name", strHoTen);
                            user.put("phone", strSdt);
                            user.put("permission",mpermission);
                            user.put("status", true);
                            databaseReference.child(auth.getCurrentUser().getUid()).setValue(user);
                            auth.signOut();

                            showToast("Account created successfully.");
                            startActivity(new Intent(AddStaff.this, ListRoom.class));
                            finish();
                        } else {
                            showToast("Create an account failed");
                        }
                    }
                });
    }

    private void showToast(String mess){
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }
}