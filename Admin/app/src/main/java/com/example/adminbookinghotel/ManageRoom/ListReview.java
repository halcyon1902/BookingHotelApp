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
import com.example.adminbookinghotel.Adapter.ListReviewAdapter;
import com.example.adminbookinghotel.Model.Review;
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

public class ListReview extends SideBar {
    private RecyclerView rcvListReview;
    private ListReviewAdapter reviewAdapter;
    private List<Review> listReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_list_review, null, false);
        mDraweLayout.addView(v, 0);

        getListReviewFromRealtimeDataBase();
        initUi();

    }

    private void initUi() {

        listReview = new ArrayList<>();
        rcvListReview = findViewById(R.id.rcv_review);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvListReview.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvListReview.addItemDecoration(dividerItemDecoration);


        reviewAdapter = new ListReviewAdapter(listReview,this);
        rcvListReview.setAdapter(reviewAdapter);
    }

    private void getListReviewFromRealtimeDataBase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("review");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listReview.clear();
                for(DataSnapshot child : snapshot.getChildren()){
                    Review review = child.getValue(Review.class);
                    listReview.add(review);
                }
                reviewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Get list Review Failed");
            }
        });
    }
    private void showToast(String mess) {
        Toast.makeText(this,mess,Toast.LENGTH_SHORT);
    }
}