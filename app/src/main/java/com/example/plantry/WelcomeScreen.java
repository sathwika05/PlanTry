package com.example.plantry;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class WelcomeScreen extends AppCompatActivity{
    // variables
    AppCompatButton buyGroceries, managePantry, manageHousehold, findRecipes, logOut;
    TextView displayNameLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // hooks to all xml elements in activity_welcome.xml
        displayNameLabel = findViewById(R.id.lbl_displayName);
        buyGroceries = findViewById(R.id.buy_groceries);
        buyGroceries.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        });
        managePantry = findViewById(R.id.manage_pantry);
        managePantry.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Overview.class);
            startActivity(intent);
        });
        manageHousehold = findViewById(R.id.manage_household);
        manageHousehold.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
            startActivity(intent);
        });
        findRecipes = findViewById(R.id.find_recipe);
        logOut = findViewById(R.id.log_out);
        logOut.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        getDisplayName();
    }

    @Override
    public void onBackPressed() {
    }

    private void getDisplayName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();

            // Firebase auth update delay, thus using get intent to get display name temporarily for new signed up user.
            if(name == null){
                displayNameLabel.setText(getIntent().getStringExtra("displayName"));
            }else{
                displayNameLabel.setText(name);

            }
        }
    }
}
