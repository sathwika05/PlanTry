package com.example.plantry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button callSignUp,btnLogin, btnResetPw;
    TextInputLayout email, password;
    FirebaseAuth mAuth;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextInputLayout userEmail;
    private AppCompatButton cancel, resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        callSignUp = findViewById(R.id.signup_screen);
        btnLogin = findViewById(R.id.login_go_btn);
        email = findViewById(R.id.log_username);
        password = findViewById(R.id.log_password);
        mAuth= FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(view-> loginUser());
        btnResetPw = findViewById(R.id.resetPassword_btn);
        btnResetPw.setOnClickListener(view ->{
            showResetPwPopup();
        });
        callSignUp.setOnClickListener(view -> startActivity(new Intent(Login.this,SignUp.class)));
    }

    @Override
    public void onBackPressed() {
    }

    private void loginUser(){
        String lemail=email.getEditText().getText().toString();
        String lpassword=password.getEditText().getText().toString();
        if (TextUtils.isEmpty(lemail)){
            email.setError("Email cannot be empty");
            email.requestFocus();
        }else if(TextUtils.isEmpty(lpassword)){
            password.setError("Password cannot be empty");
            password.requestFocus();
        }else{

            mAuth.signInWithEmailAndPassword(lemail,lpassword).addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               Toast.makeText(Login.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
               startActivity(new Intent(Login.this,WelcomeScreen.class));
           }else{
                    Toast.makeText(Login.this, "Login Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // forget password button
    private void showResetPwPopup(){
        dialogBuilder = new AlertDialog.Builder((this));
        final View resetPwPopupView = getLayoutInflater().inflate(R.layout.popup_resetpassword, null);
        userEmail = resetPwPopupView.findViewById(R.id.email);
        resetPassword = resetPwPopupView.findViewById(R.id.resetPassword_btn);
        cancel = resetPwPopupView.findViewById(R.id.cancel_btn);

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

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void resetPw(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(this.toString(), "Email sent.");
                            Toast.makeText(Login.this, "Password reset email sent. Please check your inbox.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}

