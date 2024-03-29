package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class viewMessagesUser extends AppCompatActivity {

    ListView lv;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages_user);
        db = new DatabaseHelper(getApplicationContext());
        lv = findViewById(R.id.messagesListView);

        User user = (User) getIntent().getSerializableExtra("user");


        viewMessagesListAdapter viewMessagesListAdapters = new viewMessagesListAdapter(getApplicationContext(), db.getMessageSpecific(user.getId()));
        lv.setAdapter(viewMessagesListAdapters);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.men, menu);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        User user = (User) getIntent().getSerializableExtra("user");

        getSupportActionBar().setTitle("user: " +db.getDataSpecific(user.getId()).getUsername());

        Bitmap bmp = BitmapFactory.decodeByteArray(db.getUserFoodImage(user.getId()), 0, db.getUserFoodImage(user.getId()).length);
        Drawable draw = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bmp, 300, 300, true));

        getSupportActionBar().setHomeAsUpIndicator(draw);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.subItemHome:
                Intent iHome = new Intent(getApplicationContext(), MainActivity.class);
                User userHome = (User) getIntent().getSerializableExtra("user");
                iHome.putExtra("user", userHome);
                iHome.putExtra("check", "user");
                startActivity(iHome);


                return true;

            case R.id.subItemOrder:

                Intent iOrder = new Intent(getApplicationContext(), OrderNow.class);
                User userOrder = (User) getIntent().getSerializableExtra("user");
                iOrder.putExtra("user", userOrder);
                startActivity(iOrder);

                return true;

            case R.id.orderHistory:

                return true;

            case R.id.subItemMenu:
                Intent iMenu = new Intent(getApplicationContext(), FoodMenu.class);
                User userMenu = (User) getIntent().getSerializableExtra("user");
                iMenu.putExtra("user", userMenu);
                startActivity(iMenu);
                return true;

            case R.id.subItemLocations:

                Intent iLocation = new Intent(getApplicationContext(), Location.class);
                User userLocation = (User) getIntent().getSerializableExtra("user");
                iLocation.putExtra("user", userLocation);
                startActivity(iLocation);
                return true;

            case R.id.subItemContact:
                Intent iContact = new Intent(getApplicationContext(), Contact.class);
                User userContact = (User) getIntent().getSerializableExtra("user");
                iContact.putExtra("user", userContact);
                startActivity(iContact);
                return true;

            case R.id.subItemAccount:
                Intent iAccount = new Intent(getApplicationContext(), EditUser.class);
                User userAccount = (User) getIntent().getSerializableExtra("user");
                iAccount.putExtra("user", userAccount);
                startActivity(iAccount);
                return true;

            case R.id.subItemOptions:
                Intent iOptions = new Intent(getApplicationContext(), Options.class);
                User userOptions = (User) getIntent().getSerializableExtra("user");
                iOptions.putExtra("user", userOptions);
                iOptions.putExtra("music", getIntent().getBooleanExtra("music", false));
                startActivity(iOptions);
                return true;

            case R.id.subItemLogout:
                Intent iLogout = new Intent(getApplicationContext(), Login.class);
                startActivity(iLogout);
                Options.mediaPlayer.pause();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}