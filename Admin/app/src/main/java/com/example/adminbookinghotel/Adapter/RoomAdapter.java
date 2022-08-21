package com.example.adminbookinghotel.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.swipereveallayout.core.SwipeLayout;
import com.apachat.swipereveallayout.core.ViewBinder;
import com.example.adminbookinghotel.R;
import com.example.adminbookinghotel.Model.Room;
import com.example.adminbookinghotel.ManageRoom.UpdateRoom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> mListRoom;
    private Context context;
    private ViewBinder viewBinder = new ViewBinder();

    public RoomAdapter(Context context, List<Room> mListRoom) {
        this.mListRoom = mListRoom;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {

        Room room = mListRoom.get(position);
        if (room == null) {
            return;
        }

        viewBinder.bind(holder.swipeLayout, room.getRoomnumber());

        Locale locale = new Locale("vi","VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String giaphong = numberFormat.format( Long.parseLong(room.getPrice()));

        holder.tvFloor.setText("Tầng: " + room.getFloor());
        holder.tvPrice.setText("Giá: " + giaphong);
        holder.tvRoomNb.setText("Số Phòng: " + room.getRoomnumber());
        holder.tvType.setText("Type: " + room.getTyperoom());
        if (room.getImage1().equals("")){
            Drawable drawable = context.getDrawable(R.mipmap.ic_launcher);
            holder.imgUrl1.setImageDrawable(drawable);
        }else{
            Picasso.get().load(room.getImage1()).fit().centerCrop().into(holder.imgUrl1);
        }

        if(!room.isStatus()){
            holder.swipeLayout.setBackgroundColor(Color.RED);
        }else{
            holder.swipeLayout.setBackgroundColor(R.color.PrimaryColor);
        }

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("hotel");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Room room1 = child.getValue(Room.class);
                            if (child.getKey().equals(room.getRoomnumber())) {
//                                reference.child(child.getKey()).removeValue();
                                if(room1.isStatus()){
                                    reference.child(child.getKey()).child("status").setValue(false);
                                }else {
                                    reference.child(child.getKey()).child("status").setValue(true);
                                }
                                Toast.makeText(v.getContext(), "Update is successful", Toast.LENGTH_LONG).show();
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

        holder.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateRoom.class);
                intent.putExtra("clickEdit", room);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListRoom != null)
            return mListRoom.size();
        return 0;
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFloor, tvPrice, tvType, tvRoomNb, tvDelete, tvEdit, tvUpdate;
        private ImageView imgUrl1;
        private LinearLayout layoutSwipe;
        private SwipeLayout swipeLayout;


        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.mSwipereveallayout);
            tvDelete = itemView.findViewById(R.id.delete_room);
            tvUpdate = itemView.findViewById(R.id.update_room);
            tvEdit = itemView.findViewById(R.id.update_room);
            tvFloor = itemView.findViewById(R.id.tv_floor);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvType = itemView.findViewById(R.id.tv_type_room);
            tvRoomNb = itemView.findViewById(R.id.tv_room_number);
            imgUrl1 = itemView.findViewById(R.id.img_room);

        }
    }
}
