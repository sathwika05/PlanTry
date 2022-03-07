package com.example.plantry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUp_2 extends AppCompatActivity {
    //Variables
    TextInputLayout regDispName;
    AppCompatButton regBtn, regToLoginBtn;
    FirebaseAuth mAuth;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference householdDB = database.getReference().child("household");
    private DatabaseReference userDB = database.getReference().child("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_2);

        //Hooks to all xml elements in activity_sign_up.xml
        regDispName = findViewById(R.id.username);
        regBtn = findViewById(R.id.reg_btn);
        regBtn.setOnClickListener(view -> registerUser());
        regToLoginBtn = findViewById(R.id.reg_login_btn);
        regToLoginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });
        mAuth = FirebaseAuth.getInstance();
    }

    private Boolean validateUsername() {
        String val = regDispName.getEditText().getText().toString();
        String noWhiteSpace="\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            regDispName.setError("Field cannot be empty");
            return false;
        }
        else if (val.length() >= 15) {
            regDispName.setError("Username is too long");
            return false;
        }
        else if(!val.matches(noWhiteSpace)) {
            regDispName.setError("Username must be 4-15 characters & white spaces are not allowed");
            return false;
        }
        else
        {
            regDispName.setError(null);
            regDispName.setErrorEnabled(false);
            return true;
        }
    }

    // obtain username, register, and prompt user to login
    public void registerUser() {
        // get all the values in String
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        String displayName = regDispName.getEditText().getText().toString();
        String TAG = "";

        if (!validateUsername()){
            return;
        }

        // prompt user to login after successful registration
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success");

                FirebaseUser user = mAuth.getCurrentUser();

                // Sign in success, update UI with the signed-in user's information
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                            }
                        });

                // Create a new householdDB (unique key) with uid
                // new household will be created upon each registration
   //           householdDB.push().child("ownerUid").setValue(user.getUid());
                Household newHousehold = new Household();
                newHousehold.setOwnerUid(user.getUid());
                newHousehold.setOwnerEmail(user.getEmail());
                householdDB.push().setValue(newHousehold);

                UserHelperClass newUser = new UserHelperClass(displayName, email, user.getUid(), email);
                userDB.push().setValue(newUser);

                Toast.makeText(SignUp_2.this, "Registration successful. Enjoy PlanTry!", Toast.LENGTH_LONG).show();

                Intent intent1 = new Intent(SignUp_2.this,WelcomeScreen.class);
                intent1.putExtra("displayName", displayName);
                startActivity(intent1);
            }else{
                Toast.makeText(SignUp_2.this, "Registration Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}