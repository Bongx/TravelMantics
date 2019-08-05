package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListAcitvity extends AppCompatActivity {
    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_list);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu,menu);
        MenuItem insertMenu = menu.findItem(R.id.insert_menu);
        if (FirebaseUtil.isAdmin == true){
            insertMenu.setVisible(true);

        }
        else {
            insertMenu.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.insert_menu:
                Intent intent= new Intent(this, DealActivity.class);
                startActivity(intent);

                return true;
            case R.id.logout_menu:
                int id = item.getItemId();
                if (id == R.id.logout_menu) {
                    // This is first step.
                    showPopup();
                    return true;
                }
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ListAcitvity.this,"Image Succesfully uploaded",Toast.LENGTH_LONG).show();

                                Log.d("Logout", "User Logged out");
                               FirebaseUtil.attachListener();
                            }
                        });

                FirebaseUtil.detachListener();
            return true;
        }
    return super.onOptionsItemSelected(item);
    }
    // first step helper function
    private void showPopup() {
       AlertDialog.Builder alert = new AlertDialog.Builder(ListAcitvity.this);
        alert.setMessage("Are you sure you want to LogOut?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()                 {

                    public void onClick(DialogInterface dialog, int which) {

                        logout(); // Last step. Logout function

                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    private void logout() {
        startActivity(new Intent(this, ListAcitvity.class));
        finish();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            hideSystemUi();
        }
    }
    private void hideSystemUi() {
        View decorView=getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE


        );
    }

   @Override
    protected  void onPause(){
        super.onPause();
        FirebaseUtil.detachListener();
    }
    @Override
    protected  void onResume() {
        super.onResume();
        // FirebaseUtil.openFbReference("travelDeals",this);


        FirebaseUtil.openFbReference("traveldeals",this);
        RecyclerView rvDeals= findViewById(R.id.rvDeals);
        final DealAdapter adapter = new DealAdapter();
        rvDeals.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvDeals.setLayoutManager(linearLayoutManager);
       FirebaseUtil.attachListener();
    }


    public  void showMenu(){
        invalidateOptionsMenu();
    }
}
