package com.aliindustries.groceryshoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aliindustries.groceryshoppinglist.ui.login.loginscreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class createaccount extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText confirmpassword;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    Button button;
    private final static String salt="DGE$5SGr@3VsHYUMas2323E4d57vfBfFSTRU@!DSH(*%FDSdfg13sgfsg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myRef = FirebaseDatabase.getInstance().getReference();
        email = findViewById(R.id.emailaddress);
        password = findViewById(R.id.pword);
        confirmpassword = findViewById(R.id.confirmpword);
        button = findViewById(R.id.button3);
        firebaseAuth = FirebaseAuth.getInstance();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.ic_launcher_background));
        }

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    email.setError("Enter email address");
                    button.setEnabled(false);
                }
                else {
                    if(password.getText().toString().trim().length() >= 5 && confirmpassword.getText().toString().trim().length() >= 5 && confirmpassword.getText().toString().trim().equals(password.getText().toString().trim())) {
                        button.setEnabled(true);
                    }
                    else  {
                        button.setEnabled(false);
                    } }
            }
        });

        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 5) {
                    password.setError("Length must be at least 5 characters");
                    button.setEnabled(false);
                }
                else {
                    if(email.getText().toString().trim().length() > 0 && confirmpassword.getText().toString().trim().length() >= 5 && confirmpassword.getText().toString().trim().equals(password.getText().toString().trim())) {
                        button.setEnabled(true);
                    }
                    else {
                        button.setEnabled(false);

                    }
                }

            }
        });

        confirmpassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() < 5) {
                    confirmpassword.setError("Length must be at least 5 characters");
                    button.setEnabled(false);
                }
                else if(!confirmpassword.getText().toString().trim().equals(password.getText().toString().trim())) {
                    confirmpassword.setError("Please confirm password correctly");
                    button.setEnabled(false);
                }
                else {
                    if(email.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() >= 5 && confirmpassword.getText().toString().trim().equals(password.getText().toString().trim())) {
                        button.setEnabled(true);
                    }
                    else {
                        button.setEnabled(false);

                    }
                }

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_account();
            }
        });


    }

    public static String Hash(String message) {
        String md5 = "";
        if(null == message)
            return null;

        message = message+salt;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(message.getBytes(), 0, message.length());
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }


    public void create_account() {

        firebaseAuth.createUserWithEmailAndPassword(email .getText().toString().trim(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            boolean isInserted = false;

                                            if(task.isSuccessful()){
                                                Toast.makeText(createaccount.this, "Registered successfully. Please check your email for verification",
                                                        Toast.LENGTH_LONG).show();
                                                String id = myRef.push().getKey();
                                                String emailaddress = email.getText().toString().trim();
                                                String hashpassword = Hash(password.getText().toString().trim());
                                                String hashconfpassword = Hash(confirmpassword.getText().toString().trim());
                                                String emailaddress2 = emailaddress.replace(".", "") + "10125signincode";

                                                Account account = new Account(id,emailaddress2,hashpassword);
                                                myRef.child(emailaddress2).setValue(account);

                                                startActivity(new Intent(createaccount.this,loginscreen.class));
                                                finish();
                                                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                                            }else{
                                                Toast.makeText(createaccount.this,  task.getException().getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                        }

                        else {

                            Toast.makeText(createaccount.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}
