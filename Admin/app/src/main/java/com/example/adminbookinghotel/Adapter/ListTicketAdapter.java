package com.example.adminbookinghotel.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.swipereveallayout.core.SwipeLayout;
import com.example.adminbookinghotel.Model.Ticket;
import com.example.adminbookinghotel.R;
import com.example.adminbookinghotel.ManageRoom.TicketDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ListTicketAdapter extends RecyclerView.Adapter<ListTicketAdapter.ListTicketHolder>{

    private List<Ticket> list;
    private Context context;

    public ListTicketAdapter(List<Ticket> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ListTicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new ListTicketAdapter.ListTicketHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ListTicketHolder holder, int position) {

        Ticket ticket = list.get(position);
        if (ticket == null) {
            return;
        }

        holder.tvEmail.setText(ticket.getEmail());
        holder.tvDateCome.setText(ticket.getDatecome());
        holder.tvDateLeave.setText(ticket.getDateleave());
        holder.tvTypeRoom.setText(ticket.getTyperoom());
        holder.tvCurrentDate.setText(ticket.getCurrentdate());
        if(!ticket.isStatus()){
            holder.swipeLayout.setBackgroundColor(Color.RED);
        }else{
            holder.swipeLayout.setBackgroundColor(R.color.indian_red);
        }
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ticket.isStatus()){
                    Toast.makeText(v.getContext(), "Ticket is disable", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ticket booking");
                reference.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Ticket ticket1 = child.getValue(Ticket.class);

//                            if (ticket1.getEmail().equals(ticket.getEmail()) && ticket1.getDatecome().equals(ticket.getDatecome()) && ticket1.getDateleave().equals(ticket.getDateleave())) {
//                                reference.child(child.getKey()).removeValue();
//                                DeleteBooking(ticket.getEmail(), ticket.getTyperoom(),ticket.getDatecome(), ticket.getStaydate());
//                                Toast.makeText(v.getContext(), "Delete is successful", Toast.LENGTH_SHORT).show();
//                            }

                            if (ticket1.getEmail().equals(ticket.getEmail()) && ticket1.getDatecome().equals(ticket.getDatecome()) && ticket1.getDateleave().equals(ticket.getDateleave())) {
//                                reference.child(child.getKey()).removeValue();
                                if(ticket1.isStatus()){
                                    reference.child(child.getKey()).child("status").setValue(false);
                                    DeleteBooking(ticket.getEmail(), ticket.getTyperoom(),ticket.getDatecome(), ticket.getStaydate());
                                    Toast.makeText(v.getContext(), "Disable is successful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(v.getContext(), "Warning!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void DeleteBooking(String email, String typeroom, String datecome, String datestay) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

                int n = Integer.parseInt(datestay);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("booking").child(typeroom);

                int day = Integer.parseInt(datecome.substring(0,2));
                int month = Integer.parseInt(datecome.substring(3,5));
                int year = Integer.parseInt(datecome.substring(6));
                Calendar calendar = Calendar.getInstance();

                for(int i= 0 ; i<n; i++){
                    calendar.set(year, month - 1, day + i);
                    String date = simpleDateFormat.format(calendar.getTime());
                    Log.e("vao delte",date + " email: "+ ticket.getEmail());
                    DatabaseReference bookingRef = reference.child(date);
                    bookingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for (DataSnapshot child : snapshot.getChildren())
                                {
                                    Ticket ticket1 = child.getValue(Ticket.class);
                                    Log.e("vao if",date + " email: "+ ticket1.getEmail());
                                    assert ticket1 != null;
                                    if(ticket1.getEmail().equals(email)){
                                        bookingRef.child(child.getKey()).removeValue();
                                    }
                                }
                            }else{
                                Log.e("vao else",date);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        holder.liner_ticket.setOnClickListener(v -> {
            Intent intent = new Intent(context, TicketDetail.class);
            intent.putExtra("clickTicket", ticket);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        if(list.size() != 0)
            return list.size();
        return 0;
    }

    public class ListTicketHolder extends RecyclerView.ViewHolder {

        private TextView tvDateCome;
        private TextView tvDateLeave;
        private TextView tvCurrentDate;
        private TextView tvTypeRoom;
        private TextView tvEmail;
        private TextView tvDelete;
        private SwipeLayout swipeLayout;
        private LinearLayout liner_ticket;

        public ListTicketHolder(@NonNull View itemView) {
            super(itemView);

            liner_ticket = itemView.findViewById(R.id.linear_ticket);
            swipeLayout = itemView.findViewById(R.id.swipeTicket);
            tvDateCome = itemView.findViewById(R.id.ChkIn);
            tvDateLeave = itemView.findViewById(R.id.ChkOut);
            tvCurrentDate = itemView.findViewById(R.id.tv_current_date);
            tvTypeRoom = itemView.findViewById(R.id.tv_type_room_ticket);
            tvEmail = itemView.findViewById(R.id.tv_email_ticket);
            tvDelete = itemView.findViewById(R.id.delete_ticket);

        }
    }
}
