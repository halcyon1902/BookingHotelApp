package com.example.adminbookinghotel.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.swipereveallayout.core.SwipeLayout;
import com.example.adminbookinghotel.Model.Review;
import com.example.adminbookinghotel.Model.UserAdmin;
import com.example.adminbookinghotel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListReviewAdapter extends RecyclerView.Adapter<ListReviewAdapter.ListReviewHolder> {

    private List<Review> list;
    private Context context;

    public ListReviewAdapter(List<Review> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ListReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ListReviewAdapter.ListReviewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ListReviewHolder holder, int position) {

        Review review = list.get(position);
        if (review == null) {
            return;
        }

        holder.name.setText(review.getName());
        holder.date.setText(review.getDate());
        holder.review.setText(review.getReview());
        holder.star.setRating( Float.parseFloat(review.getStar()));
        if(!review.isStatus()){
            holder.swipeLayout.setBackgroundColor(Color.RED);
        }else{
            holder.swipeLayout.setBackgroundColor(R.color.indian_red);

        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("review");
                reference.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Review review1 = child.getValue(Review.class);
                            if (review1.getEmail().equals(review.getEmail())) {
//                                reference.child(child.getKey()).removeValue();
                                if(review1.isStatus()){
                                    reference.child(child.getKey()).child("status").setValue(false);
                                }else {
                                    reference.child(child.getKey()).child("status").setValue(true);
                                }
                                Toast.makeText(v.getContext(), "Delete is successful", Toast.LENGTH_LONG).show();
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

    public class ListReviewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private RatingBar star;
        private TextView date;
        private TextView review;
        private TextView delete;
        private SwipeLayout swipeLayout;
        public ListReviewHolder(@NonNull View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.swipeReview);
            name = itemView.findViewById(R.id.tv_name_reivew);
            date = itemView.findViewById(R.id.tv_date_review);
            star = itemView.findViewById(R.id.rt_reviewUser);
            review = itemView.findViewById(R.id.tv_review);
            delete = itemView.findViewById(R.id.delete_review);
        }
    }
}
