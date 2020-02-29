package com.aliindustries.groceryshoppinglist.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aliindustries.groceryshoppinglist.Account;
import com.aliindustries.groceryshoppinglist.DatabaseHelper;
import com.aliindustries.groceryshoppinglist.MainActivity;
import com.aliindustries.groceryshoppinglist.R;
import com.aliindustries.groceryshoppinglist.addFirebaseList;
import com.aliindustries.groceryshoppinglist.createaccount;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.aliindustries.groceryshoppinglist.MainActivity.justAlphaChars;
import static com.aliindustries.groceryshoppinglist.createaccount.Hash;

public class loginscreen extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
     EditText usernameEditText;
     EditText passwordEditText;
     Button loginButton;
    private long mLastClickTime;
    ProgressBar loadingProgressBar;
    Button materialButton;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleSignInClient;
    SignInButton signInButton;
    String emailvar = "";
    String emailwithstop = "";
    DatabaseReference mref;
    DatabaseHelper myDb;
    private int count2 = 0;
    ArrayList<addFirebaseList> addFirebaseLists = new ArrayList<>();


    String signinmethod = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        firebaseAuth = FirebaseAuth.getInstance();
        materialButton = findViewById(R.id.createaccount);
        signInButton = findViewById(R.id.signInButton2);
        mref = FirebaseDatabase.getInstance().getReference();
        myDb = DatabaseHelper.getInstance(loginscreen.this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.ic_launcher_background));
        }


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
                    try {
                        String emaileditextstring = usernameEditText.getText().toString().trim();
                        emaileditextstring = emaileditextstring.replace(".", "");
                        String signedinemailaddress = "";
                        signinmethod = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().toString().trim();

                        if (!signinmethod.trim().equals("password")) {
                            signedinemailaddress = emaileditextstring;
                        } else {
                            signedinemailaddress = emaileditextstring + "10125signincode";
                        }

                        count2 = (int) myDb.getAllCount();
                        Cursor cursor2 = myDb.getAllData();
                        if (cursor2.moveToFirst()) {
                            do {
                                int data0 = cursor2.getInt(cursor2.getColumnIndex("ID"));
                                final String data1 = cursor2.getString(cursor2.getColumnIndex("TITLE"));
                                String data2 = cursor2.getString(cursor2.getColumnIndex("ITEM"));
                                int data3 = cursor2.getInt(cursor2.getColumnIndex("ISCHECKED"));
                                int data4 = cursor2.getInt(cursor2.getColumnIndex("QUANTITY"));
                                double data5 = cursor2.getDouble(cursor2.getColumnIndex("PRICE"));
                                String data6 = cursor2.getString(cursor2.getColumnIndex("WEEK"));
                                String data7 = cursor2.getString(cursor2.getColumnIndex("MONTH"));
                                int data8 = cursor2.getInt(cursor2.getColumnIndex("YEAR"));
                                long data9 = cursor2.getLong(cursor2.getColumnIndex("DATEINMS"));
                                String data10 = cursor2.getString(cursor2.getColumnIndex("DATECREATED"));


                                if (count2 > 0 && addFirebaseLists.size() <= count2) {
                                    addFirebaseList addFirebaseList = new addFirebaseList(data0, data1, data2, data3, data4, data5,data6,data7,data8,data9,data10);

                                    System.out.println("this is all data: " + data0);
                                    addFirebaseLists.add(addFirebaseList);
                                }

                                mref = FirebaseDatabase.getInstance().getReference(signedinemailaddress);
                                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (addFirebaseLists.size() > 0) {
                                            for (int i = 0; i < addFirebaseLists.size(); i++) {
                                                int identifier1 = addFirebaseLists.get(i).getID();
                                                String thedata1 = addFirebaseLists.get(i).getTitle();

                                                if (snapshot.child(identifier1 + thedata1).child(identifier1 + thedata1 + "item").hasChild(Integer.toString(addFirebaseLists.get(i).getID()))) {
                                                } else {
                                                    int identifier2 = addFirebaseLists.get(i).getID();
                                                    mref.child(identifier2 + thedata1).child(identifier2 + thedata1 + "item").setValue(addFirebaseLists.get(i));
                                                }
                                            }
                                        }


                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                // do what ever you want here
                            } while (cursor2.moveToNext());
                        }
                        cursor2.close();

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }


                    new fromFirebaseToSQLite().execute();


                    Intent i = new Intent(loginscreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

                }
            }
        };




        materialButton.setOnClickListener(new View.OnClickListener() {
            private long mLastClickTime = 0;

            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent i = new Intent(loginscreen.this, createaccount.class);
                startActivity(new Intent(i));
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {

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
                    passwordEditText.setError("Length must be at least 5 characters");
                    loginButton.setEnabled(false);
                }
                else {
                    if(usernameEditText.getText().toString().trim().length() > 0) {
                        loginButton.setEnabled(true);
                    }
                    else {
                        loginButton.setEnabled(false);

                    }
                }

            }
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() < 1) {
                    usernameEditText.setError("Enter email address");
                    loginButton.setEnabled(false);
                }
                else {
                    if(passwordEditText.getText().toString().trim().length() >= 5) {
                        loginButton.setEnabled(true);
                    }
                    else  {
                        loginButton.setEnabled(false);

                    }
                }

            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = new GoogleApiClient.Builder(loginscreen.this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                Toast.makeText(loginscreen.this, "Error occurred!", Toast.LENGTH_LONG).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });


        check();
    }
    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(mAuthListener);

    }
    private void signIn() {
        try {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
            mGoogleSignInClient.clearDefaultAccountAndReconnect();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        catch (Exception e){
            Toast.makeText(loginscreen.this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    public void check() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("emailaddress", usernameEditText.getText().toString().trim());
                    editor.putString("pword", passwordEditText.getText().toString().trim());
                    editor.apply();
                    String email_Address = usernameEditText.getText().toString().trim();
                    firebaseAuth.signInWithEmailAndPassword(email_Address,
                            passwordEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                                                return;
                                            }
                                            mLastClickTime = SystemClock.elapsedRealtime();
                                            String email = usernameEditText.getText().toString().trim();
                                            String email2 = usernameEditText.getText().toString().trim();
                                            email2 = email2.replaceAll("\\s+","");
                                            email = email.replace(".", "");
                                            email = email.replace(" ", "");

                                            emailvar = email;
                                            emailwithstop = email2;

                                        } else {
                                            FirebaseAuth.getInstance().signOut();
                                            Toast.makeText(loginscreen.this, "Please verify your email address"
                                                    , Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(loginscreen.this, task.getException().getMessage()
                                                , Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }
                catch (Exception e ) {
                    Toast.makeText(loginscreen.this, e.toString()
                            , Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.
                        class);
                firebaseAuthWithGoogle(account);

                String fullname = account.getDisplayName().toString();
                final String firstname = fullname.substring(0,fullname.indexOf(" "));
                final String lastnames = fullname.substring(fullname.indexOf(" ")+1);
                final String email = account.getEmail();
                String email2 = account.getEmail();

                email2 = email2.replace(".", "");
                email2 = email2.replace(" ", "");

                emailvar = email2;
                emailwithstop = email;


                final String uniqueid = mref.push().getKey();

                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(emailvar).exists()) {

                        }
                        else {
                            Account myaccount = new Account(uniqueid,email,Hash(passwordEditText.getText().toString().trim()));
                            mref.child(emailvar).setValue(myaccount);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("hel", "Google sign in failed", e);
                // ...
            }
            catch (Exception e ) {

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(findViewById(android.R.id.content), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
    }



    public class fromFirebaseToSQLite extends AsyncTask<Integer, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            String res = "";
            try {
                res = "success";
                if (firebaseAuth.getCurrentUser() != null) {
                    String emailfirebasenode = firebaseAuth.getCurrentUser().getEmail();
                    emailfirebasenode = emailfirebasenode.replace(".", "");
                    signinmethod = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().toString().trim();
                    if (firebaseAuth.getCurrentUser() != null) {
                        if (!signinmethod.trim().equals("password")) {

                        } else {
                            emailfirebasenode = emailfirebasenode + "10125signincode";
                        }
                        mref = FirebaseDatabase.getInstance().getReference(emailfirebasenode);

                        mref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                ArrayList<String> titlekey = new ArrayList<String>();
                                ArrayList<String> itemkey2 = new ArrayList<String>();

                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    String uid = ds.getKey();
                                    titlekey.add(uid);
                                }

                                for(int i = 0; i< titlekey.size();i++) {

                                    String sh = snapshot.child(titlekey.get(i)).child(titlekey.get(i)+"item").getKey();
                                    itemkey2.add(sh);

                                }

                                for (int i = 0; i < titlekey.size(); i++) {
                                        Object obj1 = snapshot.child(titlekey.get(i)).child(itemkey2.get(i)).child("item").getValue();
                                        Object obj2 = snapshot.child(titlekey.get(i)).child(itemkey2.get(i)).child("isChecked").getValue();
                                        Object obj3 = snapshot.child(titlekey.get(i)).child(itemkey2.get(i)).child("price").getValue();
                                        Object obj4 = snapshot.child(titlekey.get(i)).child(itemkey2.get(i)).child("qty").getValue();
                                        Object obj5 = snapshot.child(titlekey.get(i)).child(itemkey2.get(i)).child("title").getValue();

                                    Object obj6 = snapshot.child(titlekey.get(i)).child(itemkey2.get(i)).child("week").getValue();
                                    Object obj7 = snapshot.child(titlekey.get(i)).child(itemkey2.get(i)).child("month").getValue();
                                    Object obj8 = snapshot.child(titlekey.get(i)).child(itemkey2.get(i)).child("year").getValue();
                                    Object obj9 = snapshot.child(titlekey.get(i)).child(itemkey2.get(i)).child("dateinms").getValue();
                                    Object obj10 = snapshot.child(titlekey.get(i)).child(itemkey2.get(i)).child("datecreated").getValue();

                                    String item = "";
                                        int ischecked = 0;
                                        double price = 0;
                                        int quantity = 0;
                                        String title = "";

                                    String week = "";
                                    String month = "";
                                    int yr = 0;
                                    long datems = 0;
                                    String createdate = "";

                                    System.out.println("this is: " + obj1 + " " + obj2  +" " + obj3);

                                        try {
                                            item = obj1.toString().trim();
                                            ischecked = Integer.parseInt(obj2.toString().trim());
                                            price = Double.parseDouble(obj3.toString().trim());
                                            quantity = Integer.parseInt(obj4.toString().trim());
                                            title = obj5.toString().trim();
                                            week = obj6.toString().trim();
                                            month = obj7.toString().trim();
                                            yr = Integer.parseInt(obj8.toString().trim());
                                            datems = Long.parseLong(obj9.toString().trim());
                                            createdate = obj10.toString().trim();

                                            if (!myDb.itemExists(title, item)) {

                                                Boolean a = myDb.insertData(title, item, ischecked, quantity, price,week,month,yr,datems,createdate);
                                                if (a == true) {
                                                    Toast.makeText(loginscreen.this,"Grocery lists loaded!",Toast.LENGTH_LONG).show();

                                                }
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
            catch (Exception e) {
                res = "failure";
                e.printStackTrace();
            }


            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(loginscreen.this,MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);



    }
}
