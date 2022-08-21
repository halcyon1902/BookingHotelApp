package com.example.adminbookinghotel.ManageRoom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.example.adminbookinghotel.SideBar;
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

public class AddRoom extends SideBar {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();

    private EditText edtMota, edtGia;
    private ImageView img1, img2, img3, img4;
    private Button backButton, addButton;
    private TextView tv_selectImage;
    private Uri mImageUri;
    private ProgressDialog progressDialog;
    private DatabaseReference reference;
    private Spinner spnFloor, spnType, spnRoomNb;
    private CategoryAdapter floorAdapter,typeAdapter,roomNbAdapter;
    private String  strFloor, strTypeRoom, strRoomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_add_room, null, false);
        mDraweLayout.addView(v, 0);

//        setContentView(R.layout.activity_add_room);

        initUI();

        spnFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strFloor = floorAdapter.getItem(position).getType();
                showToast(floorAdapter.getItem(position).getType());
                List<Category> list = new ArrayList<>();

                switch (strFloor){
                    case "1":
                        list.add(new Category("101"));
                        list.add(new Category("102"));
                        list.add(new Category("103"));
                        list.add(new Category("104"));
                        list.add(new Category("105"));
                        DisplayRoom(list);
                        break;
                    case "2":
                        list.add(new Category("201"));
                        list.add(new Category("202"));
                        list.add(new Category("203"));
                        list.add(new Category("204"));
                        list.add(new Category("205"));
                        DisplayRoom(list);
                        break;
                    case "3":
                        list.add(new Category("301"));
                        list.add(new Category("302"));
                        list.add(new Category("303"));
                        list.add(new Category("304"));
                        list.add(new Category("305"));
                        DisplayRoom(list);
                        break;
                    case "4":
                        list.add(new Category("401"));
                        list.add(new Category("402"));
                        list.add(new Category("403"));
                        list.add(new Category("404"));
                        list.add(new Category("405"));
                        DisplayRoom(list);
                        break;
                    case "5":
                        list.add(new Category("501"));
                        list.add(new Category("502"));
                        list.add(new Category("503"));
                        list.add(new Category("504"));
                        list.add(new Category("505"));
                        DisplayRoom(list);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strTypeRoom = typeAdapter.getItem(position).getType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tv_selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickAddRoom();
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
                            Picasso.get().load(mImageUri).fit().centerCrop().into(img1);

                        }
                        if(i == 1){
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).fit().centerCrop().into(img2);
                        }
                        if(i == 2){
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).fit().centerCrop().into(img3);
                        }
                        if(i == 3){
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).fit().centerCrop().into(img4);
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


        edtMota = findViewById(R.id.edt_mota);
        edtGia = findViewById(R.id.edt_gia);
        tv_selectImage = findViewById(R.id.tv_select_Iamge);
        img1 = findViewById(R.id.img_1);
        img2 = findViewById(R.id.img_2);
        img3 = findViewById(R.id.img_3);
        img4 = findViewById(R.id.img_4);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Room updating please wait......");


        spnFloor = findViewById(R.id.spn_floor);
        floorAdapter = new CategoryAdapter(this, R.layout.item_selected, getListFloor());
        spnFloor.setAdapter(floorAdapter);

        spnType = findViewById(R.id.spn_type_room);
        typeAdapter = new CategoryAdapter(this, R.layout.item_selected, getListType());
        spnType.setAdapter(typeAdapter);

        addButton = findViewById(R.id.btn_addroom);

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

    private List<Category> getListFloor() {
        List<Category> list = new ArrayList<>();
        list.add(new Category("1"));
        list.add(new Category("2"));
        list.add(new Category("3"));
        list.add(new Category("4"));
        list.add(new Category("5"));
        return list;
    }

    private void DisplayRoom(List<Category> getListRoomNb) {
        spnRoomNb = findViewById(R.id.spn_room_nb);
        roomNbAdapter = new CategoryAdapter(this, R.layout.item_selected, getListRoomNb);
        spnRoomNb.setAdapter(roomNbAdapter);

        spnRoomNb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strRoomNumber = roomNbAdapter.getItem(position).getType();
                showToast(roomNbAdapter.getItem(position).getType());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showToast(String mess){
        Toast.makeText(getApplicationContext(),mess,Toast.LENGTH_LONG).show();
    }

    private void ClickAddRoom() {

        String strMoTa = edtMota.getText().toString().trim();
        String strPrice = edtGia.getText().toString().trim();


        if (!Check(strTypeRoom)) {
            showToast("Please enter Type Room");
            spnType.requestFocus();
            return;
        }
        if (!Check(strMoTa)) {
            edtMota.setError("Please enter Room Number");
            edtMota.requestFocus();
            return;
        }
        if (!Check(strPrice)) {
            edtGia.setError("Please enter Price");
            edtGia.requestFocus();
            return;
        }
        if (ImageList.size() != 4){
            showToast("Please select 4 image");
          return;
        }
        progressDialog.show();
        reference = FirebaseDatabase.getInstance().getReference("hotel");
        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("Room_Images");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean exist = false;
                for(DataSnapshot child : snapshot.getChildren()){
                    Room room = child.getValue(Room.class);

                    if(room.getRoomnumber().equals(strRoomNumber)){
                        exist = true;
                        break;
                    }
                }
                if (exist == true){
                    progressDialog.dismiss();
                    showToast("Room Number is exist");
                    spnRoomNb.requestFocus();
                    return;
                }
                else{
                    Room room = new Room(strFloor, strTypeRoom, strRoomNumber, strMoTa, strPrice,"","","","",true);
                    reference.child(strRoomNumber).setValue(room);
                    DatabaseReference totalroomRef = FirebaseDatabase.getInstance().getReference().child("total room");
                    totalroomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int cout = 0;
                            for (DataSnapshot child : snapshot.getChildren()){
                                if(child.getKey().equals(strTypeRoom)) {
                                    cout = child.getValue(Integer.class);
                                }
                            }
                            cout++;
                            totalroomRef.child(strTypeRoom).setValue(cout);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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
                                        reference.child(strRoomNumber).updateChildren(hashMap);
                                    }
                                    if(finalI == 1){
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("image2", downloadUri.toString());
                                        reference.child(strRoomNumber).updateChildren(hashMap);
                                    }
                                    if(finalI == 2){
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("image3", downloadUri.toString());
                                        reference.child(strRoomNumber).updateChildren(hashMap);
                                    }
                                    if(finalI == 3){
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("image4", downloadUri.toString());
                                        reference.child(strRoomNumber).updateChildren(hashMap);
                                    }
                                }
                            }
                        });
                    }
                    startActivity(new Intent(AddRoom.this, ListRoom.class));
                    showToast("Add Room successful");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Add Room Failed");
            }
        });
    }

    private boolean Check(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        return true;
    }
}