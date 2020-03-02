package com.main.tapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    public static final String USER_FULLNAME = "fullName";
    public static final String USER_EMAIL = "email";
    public static final String USER_COLLECTION = "users";

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegisterBtn;
    private TextView mLoginText;
    private ProgressBar mProgressBar;

    private FirebaseAuth mFireAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        initViews();
        mFireAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        if (mFireAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verifyInputs(mEmail) && !verifyName(mName) && !verifyInputs(mPassword)) {
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);

                final String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                final String fullName = mName.getText().toString();
                mFireAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "User successfully created.", Toast.LENGTH_SHORT);
                            final String userId = mFireAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = mFirestore.collection(
                                    USER_COLLECTION).document(userId);
                            Map<String, Object> user = new HashMap<>();
                            user.put(USER_FULLNAME, fullName);
                            user.put(USER_EMAIL , email);
                            documentReference.set(user).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: user profile creation failure" +
                                            " for " + userId);
                                }
                            });

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

    private boolean verifyName(EditText nameET) {
        if (verifyInputs(nameET)) {
            String fullName = nameET.getText().toString();
            if (fullName.split(" ").length < 2) {
                nameET.setError("Please enter atleast your first and last name.");
                return false;
            } else {
                return true;
            }
        }
        return false;
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
