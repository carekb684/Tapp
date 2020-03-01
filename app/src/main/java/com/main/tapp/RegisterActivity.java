package com.main.tapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegisterBtn;
    private TextView mLoginText;
    private ProgressBar mProgressBar;
    private FirebaseAuth mFireAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        initViews();
        mFireAuth = FirebaseAuth.getInstance();

        if (mFireAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verifyInputs(mEmail) && !verifyInputs(mName) && !verifyInputs(mPassword)) {
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                mFireAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "User successfully created.", Toast.LENGTH_SHORT);
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT);
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

        mLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

    }

    public static boolean verifyInputs(EditText input) {
        String str = input.getText().toString();
        if (TextUtils.isEmpty(str) && str.length() < 6) {
            input.setError("Please fill in this field with a minimum of 6 characters.");
            return false;
        }
        return true;
    }

    private void initViews() {
        mName = findViewById(R.id.register_name);
        mEmail = findViewById(R.id.register_email);
        mPassword = findViewById(R.id.register_pass);
        mRegisterBtn = findViewById(R.id.register_account_btn);
        mLoginText = findViewById(R.id.register_already_text);
        mProgressBar = findViewById(R.id.register_account_progressBar);
    }
}
