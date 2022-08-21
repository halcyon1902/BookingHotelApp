package com.example.adminbookinghotel.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.swipereveallayout.core.SwipeLayout;
import com.example.adminbookinghotel.Model.Review;
import com.example.adminbookinghotel.Model.Room;
import com.example.adminbookinghotel.Model.Ticket;
import com.example.adminbookinghotel.Model.UserAdmin;
import com.example.adminbookinghotel.Model.UserCustomer;
import com.example.adminbookinghotel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListCustomerAdapter extends RecyclerView.Adapter<ListCustomerAdapter.ListCustomerViewHolder> {

    List<UserCustomer> list;
    Context context;

    public ListCustomerAdapter(List<UserCustomer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ListCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        return new ListCustomerAdapter.ListCustomerViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ListCustomerViewHolder holder, int position) {
        UserCustomer userCustomer = list.get(position);
        if (userCustomer == null) {
            return;
        }

        holder.email.setText(userCustomer.getEmail());
        holder.phone.setText(userCustomer.getPhone());
        holder.name.setText(userCustomer.getName());
        if(!userCustomer.isStatus()){
            holder.swipeLayout.setBackgroundColor(Color.RED);
        }else{
            holder.swipeLayout.setBackgroundColor(R.color.indian_red);

        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
                reference.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            UserCustomer userCustomer1 = child.getValue(UserCustomer.class);
                            if (userCustomer1.getEmail().equals(userCustomer.getEmail())) {
//                                reference.child(child.getKey()).removeValue();
//                                DeleteReview(child.getKey());
//                                DeleteTicket(userCustomer.getEmail());
                                if(userCustomer1.isStatus()){
                                    reference.child(child.getKey()).child("status").setValue(false);
                                }else {
                                    reference.child(child.getKey()).child("status").setValue(true);
                                }
                                Toast.makeText(v.getContext(), "Update is successful", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(v.getContext(), "Warning!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void DeleteTicket(String email) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ticket booking");
                reference.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Ticket ticket1 = child.getValue(Ticket.class);
                            if (ticket1.getEmail().equals(email)) {
                                reference.child(child.getKey()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Warning!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void DeleteReview(String email) {
                DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("review");
                reviewRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()){
                            if(child.getKey().equals(email)){
                                reviewRef.child(child.getKey()).removeValue();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list.size() != 0)
            return list.size();
        return 0;
    }

    public class ListCustomerViewHolder extends RecyclerView.ViewHolder {

        private TextView email;
        private TextView phone;
        private TextView name;
        private TextView delete;
        private SwipeLayout swipeLayout;


        public ListCustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.swipeAccount);
            email = itemView.findViewById(R.id.tv_email_account);
            phone = itemView.findViewById(R.id.tv_phone_account);
            name = itemView.findViewById(R.id.tv_name_account);
            delete = itemView.findViewById(R.id.delete_account);

        }
    }
}
