package com.codeeraayush.firestoreimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewImage extends AppCompatActivity {
ActionBarDrawerToggle actionBarDrawerToggle;
DrawerLayout drawerLayout;
NavigationView navigationView;
Toolbar toolbar;


private RecyclerView mRecyclerView;
private ImageAdapter imageAdapter;
private DatabaseReference mDatabaseRef;
private List<Upload>mUploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        mRecyclerView=findViewById(R.id.imageLoader);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads=new ArrayList<>();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d:snapshot.getChildren()){
                    Upload u=d.getValue(Upload.class);
                    mUploads.add(u);
                }

                imageAdapter=new ImageAdapter(ViewImage.this,mUploads);
                mRecyclerView.setAdapter(imageAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("VIEW_IMAGE", "onCancelled: "+error.getMessage());
            }
        });



        setUpActionBar();
    }
    private void setUpActionBar() {

        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navBar);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Lifeline,R.string.Lifeline);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}