package com.example.adminbookinghotel.ManageRoom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.adminbookinghotel.Model.Ticket;
import com.example.adminbookinghotel.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class TicketDetail extends AppCompatActivity {
    private TextView tvTypeRoom, tvDateCome, tvDateLeave, tvStayDate, tvAmount, tvEmail, tvName, tvPhone, tvCurrentDate;
    private Button btnBack;

    private Ticket ticket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        initUi();
        displayTicketDetail();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void displayTicketDetail() {
        Intent intent = getIntent();
        ticket = (Ticket) intent.getSerializableExtra("clickTicket");
        tvTypeRoom.setText(ticket.getTyperoom());
        tvDateCome.setText(ticket.getDatecome());
        tvDateLeave.setText(ticket.getDateleave());
        tvStayDate.setText(ticket.getStaydate());
        tvAmount.setText(ticket.getAmount());
        tvEmail.setText(ticket.getEmail());
        tvName.setText(ticket.getName());
        tvPhone.setText(ticket.getPhone());
        tvCurrentDate.setText(ticket.getCurrentdate());
    }

    private void initUi() {
        tvTypeRoom = findViewById(R.id.type_room_ticket_detail);
        tvDateCome = findViewById(R.id.ChkIn_ticket_detail);
        tvDateLeave = findViewById(R.id.ChkOut_ticket_detail);
        tvStayDate = findViewById(R.id.staydate_ticket_detail);
        tvAmount = findViewById(R.id.edt_amount_ticket_detail);
        tvEmail = findViewById(R.id.email_ticket_detail);
        tvName = findViewById(R.id.edt_email_booking);
        tvPhone = findViewById(R.id.edt_phone_booking);
        tvCurrentDate= findViewById(R.id.tv_current_date_ticket);
        btnBack = findViewById(R.id.btn_back_ticket_detail);
    }
}