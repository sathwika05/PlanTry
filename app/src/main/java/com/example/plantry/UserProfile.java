package com.example.plantry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {
    TextView displayNameHead, displayNameLbl, emailLbl, ownerLbl, householdKey;
    AppCompatButton changePw, logOut, contactUs, cancelReset, cancelJoin, joinHouseholdBtn;
    ImageView addHousehold, editHousehold, joinHousehold;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextInputLayout userEmail, householdKeyInput;
    private AppCompatButton understand, resetPassword;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference householdDB = database.getReference().child("household");
    private DatabaseReference userDB = database.getReference().child("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        addHousehold = findViewById(R.id.add_household);
        addHousehold.setOnClickListener(view ->{
            addHousehold();
        });
        editHousehold = findViewById(R.id.edit_household);
        joinHousehold = findViewById(R.id.join_household);
        joinHousehold.setOnClickListener(view ->{
            joinHousehold();
        });
        displayNameHead = findViewById(R.id.displayName);
        displayNameLbl = findViewById(R.id.dash_displayName);
        emailLbl = findViewById(R.id.dash_email);
        ownerLbl = findViewById(R.id.dash_location);
        changePw = findViewById(R.id.change_pw);
        changePw.setOnClickListener(view ->{
            showResetPwPopup();
        });
        logOut = findViewById(R.id.log_out);
        logOut.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
        contactUs = findViewById(R.id.contact_us);
        contactUs.setOnClickListener(view ->{
            String mailto = "mailto:plantryapps@gmail.com" +
                            "?cc=" +
                            "&subject=" + Uri.encode("Plantry Support") +
                            "&body=" + Uri.encode("");
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse(mailto));

            try{
                startActivity(emailIntent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(UserProfile.this, "Error opening mail app", Toast.LENGTH_SHORT).show();
            }
        });

        displayUserData();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this.getApplicationContext(), WelcomeScreen.class);
        startActivity(intent);
    }

    private void displayUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                userDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child("email").getValue().equals(profile.getEmail())){
                                String owner = dataSnapshot.child("householdOwner").getValue().toString();
                                ownerLbl.setText(owner);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                // Name, email address
                String displayName = profile.getDisplayName();
                String email = profile.getEmail();

                displayNameHead.setText(displayName + "'s");
                displayNameLbl.setText(displayName);
                emailLbl.setText(email);
            }
        }
    }

    // add new household member by displaying unique household code for other member to join a household
    private void addHousehold(){
        dialogBuilder = new AlertDialog.Builder((this));
        final View addHouseholdPopupView = getLayoutInflater().inflate(R.layout.popup_addmember, null);
        householdKey = addHouseholdPopupView.findViewById(R.id.householdKey);
        understand = addHouseholdPopupView.findViewById(R.id.understand_btn);

        dialogBuilder.setView(addHouseholdPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                householdDB.orderByChild("ownerUid").equalTo(profile.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot childSnapshot:snapshot.getChildren()){
                            String key = childSnapshot.getKey();

                            if(key == null){
                                householdKey.setText("Please get the key from household owner");
                            }else{
                                householdKey.setText(key);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                understand.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    // join a household
    private void joinHousehold(){
        dialogBuilder = new AlertDialog.Builder((this));
        final View joinHouseholdPopupView = getLayoutInflater().inflate(R.layout.popup_joinhousehold, null);
        householdKeyInput = joinHouseholdPopupView.findViewById(R.id.household_key_input);
        joinHouseholdBtn = joinHouseholdPopupView.findViewById(R.id.join_household_btn);
        cancelJoin = joinHouseholdPopupView.findViewById(R.id.cancel_btn);

        dialogBuilder.setView(joinHouseholdPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        joinHouseholdBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    for (UserInfo profile : user.getProviderData()){
                        householdDB.child(householdKeyInput.getEditText().getText().toString()).child("ownerEmail").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try{
                                    if(snapshot.getValue() != null){
                                        try{
                                            // get value of the key, where user email equal to profile email, update the household owner to new owner email
                                            String ownerEmail = snapshot.getValue().toString();
                                            userDB.orderByChild("email").equalTo(profile.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        for(DataSnapshot data : snapshot.getChildren()){
                                                            String key = data.getKey();
                                                            String email = data.child("email").getValue().toString();
                                                            String uid = data.child("uid").getValue().toString();
                                                            String username = data.child("username").getValue().toString();
                                                            userDB.child(key).child("email").setValue(email);
                                                            userDB.child(key).child("uid").setValue(uid);
                                                            userDB.child(key).child("username").setValue(username);
                                                            userDB.child(key).child("householdOwner").setValue(ownerEmail);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }else{
                                        Log.e("TAG", "Invalid key");
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        // delete old household
                        householdDB.orderByChild("ownerEmail").equalTo(profile.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    for(DataSnapshot data : snapshot.getChildren()){
                                        data.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                // close popup
                dialog.dismiss();
            }
        });

        cancelJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // TODO: edit household member
    private void editHousehold(){

    }

    private void showResetPwPopup(){
        dialogBuilder = new AlertDialog.Builder((this));
        final View resetPwPopupView = getLayoutInflater().inflate(R.layout.popup_resetpassword, null);
        userEmail = resetPwPopupView.findViewById(R.id.email);
        resetPassword = resetPwPopupView.findViewById(R.id.resetPassword_btn);
        cancelReset = resetPwPopupView.findViewById(R.id.cancel_btn);

        dialogBuilder.setView(resetPwPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        resetPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                resetPw(userEmail.getEditText().getText().toString());
                dialog.dismiss();
            }
        });

        cancelReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // reset password function
    private void resetPw(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(this.toString(), "Email sent.");
                            Toast.makeText(UserProfile.this, "Password reset email sent. Please check your inbox.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}