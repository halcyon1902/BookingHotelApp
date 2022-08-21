package com.example.adminbookinghotel.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.swipereveallayout.core.SwipeLayout;
import com.example.adminbookinghotel.R;
import com.example.adminbookinghotel.Model.UserAdmin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private List<UserAdmin> list;
    private Context context;


    public AccountAdapter(List<UserAdmin> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override

    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        return new AccountViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {

        UserAdmin userAdmin = list.get(position);
        if (userAdmin == null) {
            return;
        }

        holder.email.setText(userAdmin.getEmail());
        holder.phone.setText(userAdmin.getPhone());
        holder.name.setText(userAdmin.getName());
        if(!userAdmin.isStatus()){
            holder.swipeLayout.setBackgroundColor(Color.RED);
        }else{
            holder.swipeLayout.setBackgroundColor(R.color.indian_red);

        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("admin");
                reference.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            UserAdmin userAdmin1 = child.getValue(UserAdmin.class);
                            if (userAdmin1.getEmail().equals(userAdmin.getEmail())) {
                                if(userAdmin1.isStatus()){
                                    reference.child(child.getKey()).child("status").setValue(false);
                                }else {
                                    reference.child(child.getKey()).child("status").setValue(true);
                                }
                                Toast.makeText(v.getContext(), "update is successful", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(v.getContext(), "Warning!", Toast.LENGTH_LONG).show();
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

    public class AccountViewHolder extends RecyclerView.ViewHolder {

        private TextView email;
        private TextView phone;
        private TextView name;
        private TextView delete;
        private SwipeLayout swipeLayout;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.swipeAccount);
            email = itemView.findViewById(R.id.tv_email_account);
            phone = itemView.findViewById(R.id.tv_phone_account);
            name = itemView.findViewById(R.id.tv_name_account);
            delete = itemView.findViewById(R.id.delete_account);
        }


    }
}

