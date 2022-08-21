package com.example.adminbookinghotel.ManageRoom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminbookinghotel.Adapter.CategoryAdapter;
import com.example.adminbookinghotel.Model.Category;
import com.example.adminbookinghotel.Model.Room;
import com.example.adminbookinghotel.Model.TypeRoom;
import com.example.adminbookinghotel.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateRoom extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();

    private TextView tvFloor, tvRoomNb;

    private EditText edtMota, edtGia;
    private Button backButton, updateButton;
    private ImageView img_update_1, img_update_2, img_update_3, img_update_4;
    private TextView tv_update_selectImage;
    private boolean status;


    private Uri mImageUri;
    private Room room;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;
    private FirebaseDatabase database;

    private Spinner spnType;
    private CategoryAdapter typeAdapter;
    private String strTypeRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_room);
        initUI();
        DisplayRoom();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickUpdateRoom();
            }
        });
        tv_update_selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });

        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strTypeRoom = typeAdapter.getItem(position).getType();
                showToast(typeAdapter.getItem(position).getType());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void openFileChoose() {
        Intent intent = new Intent();
        intent.setAction((Intent.ACTION_GET_CONTENT));
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST) {
            if(resultCode == RESULT_OK) {
                if (data.getClipData() != null && data.getClipData().getItemCount() == 4 ) {

                    int countGetClipData = data.getClipData().getItemCount();

                    for(int i = 0 ; i<countGetClipData; i++)
                    {
                        if(i == 0){
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).into(img_update_1);
                        }
                        if(i == 1){
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).into(img_update_2);
                        }
                        if(i == 2){
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).into(img_update_3);
                        }
                        if(i == 3){
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).into(img_update_4);
                        }
                        ImageList.add(mImageUri);
                    }

                }else{
                    showToast("Please select 4 image");
                }
            }
        }
    }
    private void initUI() {


        edtMota = findViewById(R.id.edt_update_mota);
        edtGia = findViewById(R.id.edt_update_gia);
        tv_update_selectImage = findViewById(R.id.tv_select_Iamge_update);
        img_update_1 = findViewById(R.id.img_update_1);
        img_update_2 = findViewById(R.id.img_update_2);
        img_update_3 = findViewById(R.id.img_update_3);
        img_update_4 = findViewById(R.id.img_update_4);

        tvFloor = findViewById(R.id.tv_dp_floor);
        tvRoomNb = findViewById(R.id.tv_dp_room_nb);


        backButton = findViewById(R.id.btn_back);
        updateButton = findViewById(R.id.btn_updateroom);

        spnType = findViewById(R.id.spn_update_type_room);
        typeAdapter = new CategoryAdapter(getApplicationContext(), R.layout.item_selected, getListType());
        spnType.setAdapter(typeAdapter);

        progressDialog = new ProgressDialog(UpdateRoom.this);
        progressDialog.setMessage("Room updating please wait......");
    }

    private List<Category> getListType() {
        List<Category> list = new ArrayList<>();

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("typeroom");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot child : snapshot.getChildren()){
                    TypeRoom typeRoom = child.getValue(TypeRoom.class);
                    if(typeRoom.isStatus()){
                        list.add(new Category(typeRoom.getType()));
                    }
                }
                typeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return list;

    }

    private void DisplayRoom() {
        database = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        room = (Room) intent.getSerializableExtra("clickEdit");

        tvFloor.setText(room.getFloor());
        tvRoomNb.setText(room.getRoomnumber());
        status = room.isStatus();



        List<Category> list = new ArrayList<>();
        list= getListType();
        for(int i=0; i < list.size();i++){
            if(typeAdapter.getItem(i).getType().equals(room.getTyperoom())){
                spnType.setSelection(i);
            }
        }
        edtMota.setText(room.getMota());
        edtGia.setText(room.getPrice());
        Picasso.get().load(room.getImage1()).fit().centerCrop().into(img_update_1);
        Picasso.get().load(room.getImage2()).fit().centerCrop().into(img_update_2);
        Picasso.get().load(room.getImage3()).fit().centerCrop().into(img_update_3);
        Picasso.get().load(room.getImage4()).fit().centerCrop().into(img_update_4);

    }

    private void ClickUpdateRoom() {

        String strFloor = tvFloor.getText().toString().trim();
        String strRoomNb = tvRoomNb.getText().toString().trim();

        String strMota = edtMota.getText().toString().trim();
        String strPrice = edtGia.getText().toString().trim();
        String url1 = room.getImage1();
        String url2 = room.getImage2();
        String url3 = room.getImage3();
        String url4 = room.getImage4();


        if (!Check(strTypeRoom)) {
            showToast("Please enter Type Room");
            spnType.requestFocus();
            return;
        }
        if (!Check(strMota)) {
            edtMota.setError("Email không được để trống");
            edtMota.requestFocus();
            return;
        }
        if (!Check(strPrice)) {
            edtGia.setError("Please enter Price");
            edtGia.requestFocus();
            return;
        }
        progressDialog.show();
        reference = FirebaseDatabase.getInstance().getReference("hotel");
        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("Room_Images");
        Room room = new Room(strFloor, strTypeRoom, strRoomNb, strMota, strPrice,url1,url2,url3,url4,status);
        reference.child(strRoomNb).setValue(room);
        if(ImageList.size() != 0){
            for( int i = 0; i < 4; i++){
                Uri uri = ImageList.get(i);
                final StorageReference ImageName = ImageFolder.child("Image"+uri.getLastPathSegment());
                int finalI = i;
                ImageName.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;

                        final Uri downloadUri = uriTask.getResult();

                        if(uriTask.isSuccessful()){
                            if(finalI == 0){
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("image1", downloadUri.toString());
                                reference.child(strRoomNb).updateChildren(hashMap);
                            }
                            if(finalI == 1){
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("image2", downloadUri.toString());
                                reference.child(strRoomNb).updateChildren(hashMap);
                            }
                            if(finalI == 2){
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("image3", downloadUri.toString());
                                reference.child(strRoomNb).updateChildren(hashMap);
                            }
                            if(finalI == 3){
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("image4", downloadUri.toString());
                                reference.child(strRoomNb).updateChildren(hashMap);
                            }
                        }
                    }
                });
            }
        }

        startActivity(new Intent(UpdateRoom.this, ListRoom.class));
        showToast("Update Room successful");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
        finish();
    }




    private boolean Check(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        return true;
    }

    private void showToast(String mess) {
        Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_LONG).show();
    }
}