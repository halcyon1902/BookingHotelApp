package com.example.adminbookinghotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adminbookinghotel.Login.SignIn;
import com.example.adminbookinghotel.ManageAccount.AddStaff;
import com.example.adminbookinghotel.ManageAccount.ListAdmin;
import com.example.adminbookinghotel.ManageAccount.ListCustomer;
import com.example.adminbookinghotel.ManageRoom.AddRoom;
import com.example.adminbookinghotel.ManageRoom.AddTypeRoom;
import com.example.adminbookinghotel.ManageRoom.ListReview;
import com.example.adminbookinghotel.ManageRoom.ListRoom;
import com.example.adminbookinghotel.ManageRoom.ListTicket;
import com.example.adminbookinghotel.Model.UserAdmin;
import com.example.adminbookinghotel.Profile.Account;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SideBar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final  String userId = FirebaseAuth.getInstance().getUid();
    public static final  String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private String permission ;
    public DrawerLayout mDraweLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView username, username_gmail;
    private ImageView profile_image;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_bar);

        toolbar = findViewById(R.id.toolbar);
        mDraweLayout = findViewById(R.id.mainscreen);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);

        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username);
        username_gmail = headerView.findViewById(R.id.username_gmail);
        profile_image = headerView.findViewById(R.id.profile_image);
        progressDialog = new ProgressDialog(SideBar.this);
        progressDialog.setMessage("Log Out...");

        initNavigationDrawer();
        disPlayProfile();
    }

    private void disPlayProfile() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAdmin userAdmin = snapshot.getValue(UserAdmin.class);
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getKey().equals(userId)) {
                        userAdmin = child.getValue(UserAdmin.class);
                        assert userAdmin != null;
                        username_gmail.setText(userAdmin.getEmail());
                        username.setText(userAdmin.getName());
                        permission = userAdmin.getPermission();
                        if (userAdmin.getImage() == null) {
                            Drawable drawable = getResources().getDrawable(R.drawable.avatar);
                            profile_image.setImageDrawable(drawable);
                        } else {
                            Picasso.get().load(userAdmin.getImage()).into(profile_image);
                        }
                        String permission =  userAdmin.getPermission();
                        if(permission.equals("2")){
                            permissionStaff();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        //tạo menu mở drawer trên toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDraweLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDraweLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setItemIconTintList(null);
    }
    private void permissionStaff()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.add_staff).setEnabled(false);
        nav_Menu.findItem(R.id.list_admin).setEnabled(false);
        nav_Menu.findItem(R.id.list_customer).setEnabled(false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.account:
                startActivity(new Intent(this,Account.class));
                this.finish();
                break;

            case R.id.add_staff:
                startActivity(new Intent(this, AddStaff.class));
                this.finish();
                break;

            case R.id.list_admin:
                startActivity(new Intent(this, ListAdmin.class));
                this.finish();
                break;

            case R.id.list_customer:
                startActivity(new Intent(this, ListCustomer.class));
                this.finish();
                break;

            case R.id.list_room:
                startActivity(new Intent(this, ListRoom.class));
                this.finish();
                break;

            case R.id.add_type_room:
                startActivity(new Intent(this, AddTypeRoom.class));
                this.finish();
                break;

            case R.id.list_review:
                startActivity(new Intent(this, ListReview.class));
                this.finish();
                break;

            case R.id.list_ticket:
                startActivity(new Intent(this, ListTicket.class));
                this.finish();
                break;

            case R.id.add_room:
                startActivity(new Intent(this, AddRoom.class));
                this.finish();
                break;

            case R.id.sign_out:
//                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("remember", "false");
//                editor.apply();
                progressDialog.show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, SignIn.class));
                finishAffinity();
                System.exit(0);
                progressDialog.dismiss();
                break;

        }
        return false;
    }
}