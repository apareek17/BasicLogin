package com.garrettshorr.basiclogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.garrettshorr.basiclogin.R;

public class RestaurantActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextCuisine;
    private EditText editTextAddress;
    private RatingBar ratingBarRating;
    private SeekBar seekBarPrice;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_activity);

        wireWidgets();
        prefillFields();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewRestaurant();
            }
        });

    }

    private void prefillFields(){
        //check if there's a Restaurant in the intent
        //if so, fill all the fields with the restaurant
        Intent restaurantIntent = getIntent();
        Restaurant restaurant = restaurantIntent.getParcelableExtra(RestaurantListActivity.EXTRA_RESTAURANT);
        if(restaurant != null) {
            editTextName.setText((restaurant.getName()));
            editTextAddress.setText(restaurant.getAddress());
            editTextCuisine.setText(restaurant.getCuisine());
            ratingBarRating.setRating((float) restaurant.getRating());
            //seekBarPrice //TODO:need to finish for price
        }
        else {

        }
    }

    private void saveNewRestaurant() {
        String restaurantName = editTextName.getText().toString();
        String restaurantCuisine = editTextCuisine.getText().toString();
        String restaurantAddress = editTextAddress.getText().toString();
        float restaurantRating = ratingBarRating.getRating();
        int restaurantPrice = seekBarPrice.getProgress();
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantName);
        restaurant.setCuisine(restaurantCuisine);
        restaurant.setAddress(restaurantAddress);
        restaurant.setRating(restaurantRating);
        restaurant.setPrice(restaurantPrice);

        // save object synchronously
        //Restaurant savedRestaurant = Backendless.Persistence.save( restaurant );

        // save object asynchronously
        Backendless.Persistence.save( restaurant, new AsyncCallback<Restaurant>() {
            public void handleResponse(Restaurant response )
            {
                Toast.makeText(RestaurantActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
                // new Contact instance has been saved
            }

            public void handleFault( BackendlessFault fault )
            {
                Toast.makeText(RestaurantActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        });
    }

    private void wireWidgets() {
        editTextName = findViewById(R.id.editText_restaurant_name);
        editTextCuisine = findViewById(R.id.editText_restaurant_cuisine);
        editTextAddress = findViewById(R.id.editText_restaurant_address);
        ratingBarRating = findViewById(R.id.ratingBar_restaurant_rating);
        seekBarPrice = findViewById(R.id.seekBar_restaurant_price);
        buttonSave = findViewById(R.id.button_restaurant_save);
    }
}
