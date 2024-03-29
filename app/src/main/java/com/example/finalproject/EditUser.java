package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class EditUser extends AppCompatActivity {


    EditText userText, passText, edit;
    Button updateBtn, deleteBtn,changeBtn;
    ImageView imageView;
    boolean checkLengthUser = true;
    boolean checkLengthPass = true;
    boolean sameUserCheck = false;



    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        edit = new EditText(EditUser.this);

        edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        userText = findViewById(R.id.editTextUsernameEdit);
        passText = findViewById(R.id.editTextPasswordEdit);
        updateBtn = findViewById(R.id.buttonUpdate);
        deleteBtn = findViewById(R.id.buttonDelete);
        imageView = findViewById(R.id.imageView);
        changeBtn = findViewById(R.id.buttonChangeImage);


        User user = (User) getIntent().getSerializableExtra("user");

        userText.setText(db.getDataSpecific(user.getId()).getUsername());

        Bitmap bmp = BitmapFactory.decodeByteArray(db.getUserFoodImage(user.getId()), 0, db.getUserFoodImage(user.getId()).length);
        imageView.setImageBitmap(bmp);


        AlertDialog alertDialog = new AlertDialog.Builder(EditUser.this)

                .setIcon(android.R.drawable.ic_dialog_alert)

                .setTitle("Please enter your current password to verify")

                .setView(edit)

                .setCancelable(false)


                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (User user3 : db.getData()) {
                            if (user3.getId() == user.getId()) {

                                if (edit.getText().toString().equals(user3.getPassword())) {

                                    passText.setText(user3.getPassword());

                                    userText.setBackgroundResource(R.drawable.my_shape_admin);
                                    passText.setBackgroundResource(R.drawable.my_shape_admin);
                                    Toast.makeText(getApplicationContext(), "Successfully verified!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EditUser.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();

                                    Intent newActivity = new Intent(EditUser.this, MainActivity.class);
                                    newActivity.putExtra("user", user3);
                                    newActivity.putExtra("check", "user");
                                    startActivity(newActivity);
                                }
                            }
                        }
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                                Intent newActivity = new Intent(EditUser.this, MainActivity.class);
                                newActivity.putExtra("user", db.getDataSpecific(user.getId()));
                                newActivity.putExtra("check", "user");
                                startActivity(newActivity);



                    }
                })

                .show();

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageChooser();
                Toast.makeText(getApplicationContext(), "Image Selected!", Toast.LENGTH_SHORT).show();

            }
        });


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageView.buildDrawingCache();
                Bitmap bmap = imageView.getDrawingCache();
                byte[] imgData = getBitmapAsByteArray(bmap);

                if(!userText.getText().toString().matches("")) {
                    userText.setBackgroundResource(R.drawable.my_shape_admin);

                    if (!passText.getText().toString().matches("")) {
                        passText.setBackgroundResource(R.drawable.my_shape_admin);

                        if(checkLengthUser && checkLengthPass) {

                            for (User user4 : db.getData()) {
                                if (user4.getUsername().equals(userText.getText().toString())) {
                                        sameUserCheck = true;
                                        break;
                                }
                            }
                            if (sameUserCheck && !db.getDataSpecific(user.getId()).getUsername().equals(userText.getText().toString())) {
                                userText.setError("Username already taken!");
                                userText.setBackgroundResource(R.drawable.my_shape_error_admin);
                                sameUserCheck = false;



                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(EditUser.this)

                                        .setIcon(android.R.drawable.ic_dialog_alert)

                                        .setTitle("Are you sure you want to update profile?")
                                        .setCancelable(false)

                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                db.updateData(user.getId(), userText.getText().toString(), passText.getText().toString(), imgData);

                                                getSupportActionBar().setTitle("user: " + db.getDataSpecific(user.getId()).getUsername());
                                                userText.setText(db.getDataSpecific(user.getId()).getUsername());
                                                passText.setText(db.getDataSpecific(user.getId()).getPassword());
                                                Toast.makeText(EditUser.this, "User Updated!", Toast.LENGTH_SHORT).show();
                                                sameUserCheck = false;
                                            }
                                        })

                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //set what should happen when negative button is clicked

                                            }
                                        })
                                        .show();


                            }
                        }
                        } else {
                            passText.setError("You cannot leave password input blank!");
                            passText.setBackgroundResource(R.drawable.my_shape_error_admin);
                        }
                    }

                else{
                    userText.setError("You cannot leave username input blank!");
                    userText.setBackgroundResource(R.drawable.my_shape_error_admin);
                    if(passText.getText().toString().matches("")){
                        passText.setError("You cannot leave password input blank!");
                        passText.setBackgroundResource(R.drawable.my_shape_error_admin);
                    }
                    else{
                        passText.setBackgroundResource(R.drawable.my_shape_admin);
                    }
                }
            }
        });
        InputFilter[] filtersMaxUser = new InputFilter[1];
        filtersMaxUser[0] = new InputFilter.LengthFilter(17);
        userText.setFilters(filtersMaxUser);

        userText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 17) {
                    userText.setError("Max of 16 characters!");
                    userText.setBackgroundResource(R.drawable.my_shape_error_admin);
                    checkLengthUser = false;
                }

                if (s.length() < 17) {
                    userText.setBackgroundResource(R.drawable.my_shape_admin);
                    checkLengthUser = true;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        InputFilter[] filtersMaxPass = new InputFilter[1];
        filtersMaxPass[0] = new InputFilter.LengthFilter(17);
        passText.setFilters(filtersMaxPass);

        passText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 17) {
                    passText.setError("Max of 16 characters!");
                    passText.setBackgroundResource(R.drawable.my_shape_error_admin);
                    checkLengthPass = false;
                }

                if (s.length() < 6) {
                    passText.setError("Minimum of 6 characters!");
                    passText.setBackgroundResource(R.drawable.my_shape_error_admin);
                    checkLengthPass = false;
                }

                if (s.length() >= 6 && s.length() < 17) {
                    InputFilter[] filtersMaxPass = new InputFilter[1];
                    filtersMaxPass[0] = new InputFilter.LengthFilter(17);
                    passText.setFilters(filtersMaxPass);
                    passText.setBackgroundResource(R.drawable.my_shape_admin);
                    checkLengthPass = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(EditUser.this)

                        .setIcon(android.R.drawable.ic_dialog_alert)

                        .setTitle("Are you sure you want to delete?")
                        .setCancelable(false)

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                db.deleteData(user.getId());
                                Toast.makeText(EditUser.this, "User Deleted!", Toast.LENGTH_SHORT).show();
                                Intent i2 = new Intent(getApplicationContext(), Login.class);
                                startActivity(i2);
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked

                            }
                        })
                        .show();


            }
        });

    }
        void imageChooser() {

            // create an instance of the
            // intent of the type image
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);

            // pass the constant to compare it
            // with the returned requestCode
            startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
        }

        // this function is triggered when user
        // selects the image from the imageChooser
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {

                // compare the resultCode with the
                // SELECT_PICTURE constant
                if (requestCode == SELECT_PICTURE) {
                    // Get the url of the image from data
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        // update the preview image in the layout
                        imageView.setImageURI(selectedImageUri);
                    }
                }
            }
        }

        public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            return outputStream.toByteArray();

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.men, menu);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        User user = (User) getIntent().getSerializableExtra("user");

        getSupportActionBar().setTitle("user: " +db.getDataSpecific(user.getId()).getUsername());

            return super.onCreateOptionsMenu(menu);
    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.subItemHome:
                Intent iHome = new Intent(getApplicationContext(), MainActivity.class);
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                User userHome = (User) getIntent().getSerializableExtra("user");
                iHome.putExtra("user", db.getDataSpecific(userHome.getId()));
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
                Intent iOrderHistory = new Intent(getApplicationContext(), OrderHistory.class);
                User userOrderHistory = (User) getIntent().getSerializableExtra("user");
                iOrderHistory.putExtra("user", userOrderHistory);
                startActivity(iOrderHistory);
                return true;

            case R.id.subItemMenu:
                Intent iMenu = new Intent(getApplicationContext(), FoodMenu.class);
                User userMenu= (User) getIntent().getSerializableExtra("user");
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

            case R.id.subItemViewMessages:
                Intent iMessages = new Intent(getApplicationContext(), viewMessagesUser.class);
                User userMessages = (User) getIntent().getSerializableExtra("user");
                iMessages.putExtra("user", userMessages);
                startActivity(iMessages);
                return true;

            case R.id.subItemAccount:

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
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


}