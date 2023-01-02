package com.example.gastemphum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {
TextView donthave_acc_login , forget_password ;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    Button login ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

    // Initialization
    mEmailView = findViewById(R.id.emailTextView);
    mPasswordView = findViewById(R.id.passwordTextView);
    progressDialog = new ProgressDialog(this);
    mAuth = FirebaseAuth.getInstance();
    donthave_acc_login = (TextView) findViewById(R.id.dont_have_acc);
    login = (Button) findViewById(R.id.loginButton) ;
        donthave_acc_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dont_have_intent = new Intent(SignIn.this , SignUp.class);
                startActivity(dont_have_intent);
                finish();
            }
        });
    // Attempting logging in
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        }
    });

    // Login on login button click.
    findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            attemptLogin();
        }
    });

    // go to register activity.


    // User forgot password.
    findViewById(R.id.forgetPasswordBtn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createResetPasswordDialog();
        }
    });
}

    private void createResetPasswordDialog() {
        // Create dialog with input EditText.
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Reset password");

        // Set dialog content to email_alert_dialog.
        dialog.setContentView(R.layout.email_alert_dialog);
        final EditText editText = dialog.findViewById(R.id.emailEditText);
        editText.setHint("E-mail");
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.email_icon, 0);

        // Implement positive and negative buttons listeners.
        dialog.findViewById(R.id.doneDialogBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail(editText.getText().toString());
                dialog.cancel();
            }
        });
        dialog.findViewById(R.id.cancelDialogBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void sendPasswordResetEmail(String email) {
        if (!isEmailValid(email)) {
            // The given E-mail is not in the correct format (OR NULL).
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Sending reset email...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    // E-mail sent successfully
                    Toast.makeText(SignIn.this, "Reset email sent", Toast.LENGTH_SHORT).show();
                } else {
                    // Something went wrong, E-mail failed to be sent.
                    Toast.makeText(SignIn.this, "Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void attemptLogin() {

        // Remove prev errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

       /* if (email.equals("super_admin@gmail.com") && password.equals("Admin")) {
            startActivity(new Intent(this, Sign_Up_Admins.class));
            finish();
        }
        else if (email.equals("super_user@gmail.com") && password.equals("user")) {
            startActivity(new Intent(this, SignUp_Normal_Users.class));
            finish();
        } */

        // Password checks
        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.empty_error_message));
            focusView = mPasswordView;
            cancel = true;
        }

        // E-mail checks
        if(TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.empty_error_message));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.email_logic_error));
            focusView = mEmailView;
            cancel = true;
        }

        // If there was an error, request focus of first error
        if(cancel) {
            focusView.requestFocus();
        } else {
            // Attempt login
            signInWithEmailAndPassword(email, password);
        }
    }

    private void signInWithEmailAndPassword(String email, String password) {
        // Attempting to sign in.
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            // Successfully signing in, Check if the email verified.

                            // The email is verified, signing in and returning to the home activity.
                            Toast.makeText(getApplicationContext(), "Successfully logged in , Welcome To Home Page!", Toast.LENGTH_LONG).show();
                            saveUserInfo();
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                            finish();

                        } else {
                            // The task is not successful
                            if (!checkForConnection()) {
                                // No internet connection.
                                Toast.makeText(SignIn.this, "No internet access", Toast.LENGTH_SHORT).show();
                            } else {
                                // Wrong E-mail or password.
                                Toast.makeText(getApplicationContext(), "Wrong E-mail or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void saveUserInfo() {
        AppPereference.init(this);
        final SharedPreferences.Editor editor = AppPereference.getPrefs().edit();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("NormalUsers")
                .child(mAuth.getCurrentUser().getUid());

        // Listener to get user data from database
        // and save it into sharedPreference.
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editor.putString("user_firstName", dataSnapshot.child("firstName").getValue(String.class));
                editor.putString("user_lastName", dataSnapshot.child("lastName").getValue(String.class));
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean checkForConnection() {
        // Returning true if there is an internet connection, false if not.
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    private boolean isEmailValid(String email) {
        // Check if the email is in a valid email format.
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false).setMessage("Do you want to exit from the app ? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setTitle("Exit the app");
        alertDialog.show();
    }
}