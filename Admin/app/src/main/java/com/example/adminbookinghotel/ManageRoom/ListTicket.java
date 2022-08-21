package com.example.adminbookinghotel.ManageRoom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.adminbookinghotel.Adapter.AccountAdapter;
import com.example.adminbookinghotel.Adapter.ListTicketAdapter;
import com.example.adminbookinghotel.Model.Ticket;
import com.example.adminbookinghotel.Model.UserAdmin;
import com.example.adminbookinghotel.R;
import com.example.adminbookinghotel.SideBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListTicket extends SideBar {

    private RecyclerView rcvListTicket;
    private ListTicketAdapter listTicketAdapter;
    private List<Ticket> listTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list_ticket);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_list_ticket, null, false);
        mDraweLayout.addView(v, 0);


        getListTicketFromRealtimeDataBase();

        initUi();

    }

    private void initUi() {
        listTicket = new ArrayList<>();
        rcvListTicket = findViewById(R.id.rcv_ticket);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvListTicket.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvListTicket.addItemDecoration(dividerItemDecoration);


        listTicketAdapter = new ListTicketAdapter(listTicket,this);
        rcvListTicket.setAdapter(listTicketAdapter);
    }

    private void getListTicketFromRealtimeDataBase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("ticket booking");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listTicket.clear();
                for(DataSnapshot child : snapshot.getChildren()){
                    Ticket ticket = child.getValue(Ticket.class);
                    listTicket.add(ticket);
                }
                listTicketAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Get list Ticket Failed");
            }
        });
    }
    private void showToast(String mess) {
        Toast.makeText(this,mess,Toast.LENGTH_SHORT);
    }
}