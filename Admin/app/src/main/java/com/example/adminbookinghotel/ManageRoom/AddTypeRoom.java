package com.example.adminbookinghotel.ManageRoom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminbookinghotel.Adapter.AccountAdapter;
import com.example.adminbookinghotel.Adapter.ListTypeRoomAdapter;
import com.example.adminbookinghotel.Login.SignIn;
import com.example.adminbookinghotel.ManageAccount.AddStaff;
import com.example.adminbookinghotel.ManageRoom.ListRoom;
import com.example.adminbookinghotel.Model.Category;
import com.example.adminbookinghotel.Model.TypeRoom;
import com.example.adminbookinghotel.Model.UserAdmin;
import com.example.adminbookinghotel.R;
import com.example.adminbookinghotel.SideBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTypeRoom extends SideBar {

    private EditText edtTypeRoom;
    private Button btnAddTypeRoom;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;

    private RecyclerView rcvTypeRoom;
    private ListTypeRoomAdapter listTypeRoomAdapter;
    private List<TypeRoom> listTypeRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_add_type_room, null, false);
        mDraweLayout.addView(v, 0);

        getListTypeRoomFromRealtimeDataBase();
        initUi();

        btnAddTypeRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAdd();
            }
        });
    }

    private void getListTypeRoomFromRealtimeDataBase() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("typeroom");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTypeRoom.clear();
                for( DataSnapshot child : snapshot.getChildren()){
                    TypeRoom typeRoom = child.getValue(TypeRoom.class);
                    listTypeRoom.add(typeRoom);
                }
                listTypeRoomAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initUi() {
        edtTypeRoom = findViewById(R.id.edt_typeroom);
        btnAddTypeRoom = findViewById(R.id.btn_add_type_room);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding...");

        listTypeRoom = new ArrayList<>();
        rcvTypeRoom = findViewById(R.id.rcv_type_room);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvTypeRoom.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvTypeRoom.addItemDecoration(dividerItemDecoration);


        listTypeRoomAdapter = new ListTypeRoomAdapter(listTypeRoom,this);
        rcvTypeRoom.setAdapter(listTypeRoomAdapter);
    }

    private void clickAdd() {
        String strTypeRoom = edtTypeRoom.getText().toString().trim();
        if(strTypeRoom.isEmpty()){
          edtTypeRoom.setError("Please enter type room!");
          edtTypeRoom.requestFocus();
          return;
        }
        progressDialog.show();
        reference = FirebaseDatabase.getInstance().getReference("typeroom");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean exist = false;
                for (DataSnapshot child : snapshot.getChildren()) {
                    TypeRoom typeRoom = child.getValue(TypeRoom.class);
                    if (typeRoom.getType().equals(strTypeRoom)) {
                        exist = true;
                        break;
                    }
                }
                if (exist == true) {
                    progressDialog.dismiss();
                    edtTypeRoom.setError("Type Room is exist!");
                    edtTypeRoom.requestFocus();
                    return;
                } else {
                    Map<String, Object> typeroom = new HashMap<>();
                    typeroom.put("type", strTypeRoom);
                    typeroom.put("status", true);
                    reference.push().setValue(typeroom);
                    edtTypeRoom.setText("");
                    progressDialog.dismiss();
                    showToast("Add Type Room successfully.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Add Type Room Failed");
            }
        });

    }
    private void showToast(String mess){
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }
}