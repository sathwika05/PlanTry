package com.example.plantry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    //Variables
    TextInputLayout regEmail, regPassword;
    AppCompatButton nextBtn, regToLoginBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Hooks to all xml elements in activity_sign_up.xml
        regEmail = findViewById(R.id.email);
        regPassword = findViewById(R.id.password);
        nextBtn = findViewById(R.id.next_btn);
        regToLoginBtn = findViewById(R.id.reg_login_btn);
        mAuth = FirebaseAuth.getInstance();
        nextBtn.setOnClickListener(view -> createUser());
        regToLoginBtn.setOnClickListener(view -> startActivity(new Intent(SignUp.this,Login.class)));
    }

    @Override
    public void onBackPressed() {
    }

    private void createUser(){
        String email=regEmail.getEditText().getText().toString();
        String password=regPassword.getEditText().getText().toString();
        if (TextUtils.isEmpty(email)){
            regEmail.setError("Email cannot be empty");
            regEmail.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            regPassword.setError("Password cannot be empty");
            regPassword.requestFocus();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), SignUp_2.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            startActivity(intent);
        }
    }
}

