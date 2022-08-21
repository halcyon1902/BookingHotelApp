package com.example.adminbookinghotel.ManageRoom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminbookinghotel.Model.Room;
import com.example.adminbookinghotel.Model.TypeRoom;
import com.example.adminbookinghotel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateTypeRoom extends AppCompatActivity {

    private EditText edtOldType,edtNewType;
    private Button btnUpdateType;

    private DatabaseReference reference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_type_room);

        initUi();
        Intent intent = getIntent();

        edtOldType.setText(intent.getStringExtra("clickEdit"));

        btnUpdateType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickUpdate();
            }
        });

    }

    private void initUi() {
        edtOldType = findViewById(R.id.edt_old_type_room);
        edtNewType = findViewById(R.id.edt_new_typeroom);
        btnUpdateType = findViewById(R.id.btn_update_type_room);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting update....");

    }

    private void clickUpdate() {
        String strNewTypeRoom = edtNewType.getText().toString().trim();
        String strOldTypeRoom = edtOldType.getText().toString().trim();
        boolean a = false;
        if(strNewTypeRoom.isEmpty()){
            edtNewType.setError("Please enter type room!");
            edtNewType.requestFocus();
            return;
        }
        progressDialog.show();
        reference = FirebaseDatabase.getInstance().getReference("typeroom");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean exist = false;
                String key = "";
                for (DataSnapshot child : snapshot.getChildren()) {
                    TypeRoom typeRoom = child.getValue(TypeRoom.class);
                    if(typeRoom.getType().equals(strOldTypeRoom)){
                        key = child.getKey();
                    }
                    if (typeRoom.getType().equals(strNewTypeRoom)) {
                        exist = true;
                        break;
                    }

                }
                if (exist == true) {
                    progressDialog.dismiss();
                    edtNewType.setError("Type Room is exist!");
                    edtNewType.requestFocus();
                    return;
                }
                else{
                    reference.child(key).child("type").setValue(strNewTypeRoom);
                    updateListRoom(strOldTypeRoom,strNewTypeRoom);
                    startActivity(new Intent(UpdateTypeRoom.this, AddTypeRoom.class));
                    finish();
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

    private void updateListRoom(String strOldTypeRoom, String strNewTypeRoom) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("hotel");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()){
                    Room room = child.getValue(Room.class);
                    if(room.getTyperoom().equals(strOldTypeRoom)){
                        reference.child(child.getKey()).child("typeroom").setValue(strNewTypeRoom);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showToast(String mess){
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }
}