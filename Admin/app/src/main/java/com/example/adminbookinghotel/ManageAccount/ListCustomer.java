package com.example.adminbookinghotel.ManageAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.session.PlaybackState;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.adminbookinghotel.Adapter.AccountAdapter;
import com.example.adminbookinghotel.Adapter.ListCustomerAdapter;
import com.example.adminbookinghotel.Model.UserAdmin;
import com.example.adminbookinghotel.Model.UserCustomer;
import com.example.adminbookinghotel.R;
import com.example.adminbookinghotel.SideBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListCustomer extends SideBar {

    private RecyclerView rcvListCustomer;
    private ListCustomerAdapter customerAdapter;
    private List<UserCustomer> listCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_list_customer, null, false);
        mDraweLayout.addView(v, 0);


        getListUserFromRealtimeDataBase();
        initUi();
    }

    private void getListUserFromRealtimeDataBase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("user");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listCustomer.clear();
                for(DataSnapshot child : snapshot.getChildren()){
                    UserCustomer userCustomer = child.getValue(UserCustomer.class);
                    listCustomer.add(userCustomer);
                }
                customerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Get list customer Failed");
            }
        });
    }

    private void initUi() {
        listCustomer = new ArrayList<>();
        rcvListCustomer = findViewById(R.id.rcv_customer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvListCustomer.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvListCustomer.addItemDecoration(dividerItemDecoration);


        customerAdapter = new ListCustomerAdapter(listCustomer,this);
        rcvListCustomer.setAdapter(customerAdapter);
    }
    private void showToast(String mess) {
        Toast.makeText(this,mess,Toast.LENGTH_SHORT);
    }
}