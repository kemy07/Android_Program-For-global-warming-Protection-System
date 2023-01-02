package com.example.gastemphum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
TextView email_sign_up , password_sign_up , name_sign_up ;
Button sign_upBtn ;
// لو الشرط اتحقق وان الباسورد بيساوى الباسورد اللى موجود ف الداتا بييز نفذ فتح صفحة الهوم بيج
    // لو لا -- لو غير كدة أقولة ان ف خطا فى تشجيل االدخول
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email_sign_up = (TextView) findViewById(R.id.email_sign_up);
        password_sign_up = (TextView) findViewById(R.id.password_sign_up);
        name_sign_up = (TextView) findViewById(R.id.full_name_sign_up);
        sign_upBtn = (Button) findViewById(R.id.button_sign_up);
        progressDialog = new ProgressDialog(this);
        AppPereference.init(getApplicationContext());

        // Attempt register on register button click.
        sign_upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                attemptRegister();

            }
        });

        // start LoginActivity on login button click.

    }

    private void attemptRegister() {

        // Remove previous errors
        email_sign_up.setError(null);
        password_sign_up.setError(null);
        name_sign_up.setError(null);

        View focusedView = null;
        boolean cancel = false;

        // get Views values into strings
        String emailValue = email_sign_up.getText().toString();
        String passwordValue = password_sign_up.getText().toString();
        String name = name_sign_up.getText().toString();



        if (TextUtils.isEmpty(emailValue)) {
            email_sign_up.setError(getString(R.string.empty_error_message));
            focusedView = email_sign_up;
            cancel = true;
        }
        if (TextUtils.isEmpty(passwordValue)) {
            password_sign_up.setError(getString(R.string.empty_error_message));
            focusedView = password_sign_up;
            cancel = true;
        }
        if (!isPasswordValid(name)) {
            name_sign_up.setError(getString(R.string.weak_password));
            focusedView = name_sign_up;
            cancel = true;
        }


        if (cancel) {
            // Request first error view focus.
            focusedView.requestFocus();
        } else {
            // Try to create new user.
            createUserWithEmailAndPassword(emailValue, passwordValue, name);
        }
    }

    private void createUserWithEmailAndPassword(final String emailValue, String passwordValue, final String name) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressDialog.setMessage("Registering...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        //Attempting to sign up.
        mAuth.createUserWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    // User successfully created.
                    loadUserInfo();
                    Toast.makeText(SignUp.this, "Successfully Sign Up", Toast.LENGTH_SHORT).show();

                } else {
                    // task failed
                    if (!checkForConnection()) {
                        // No internet connection.
                        Toast.makeText(SignUp.this, "No internet access", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        // Weak password
                        password_sign_up.setError(getString(R.string.weak_password));
                        password_sign_up.requestFocus();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        // Invalid email
                        email_sign_up.setError(getString(R.string.email_logic_error));
                        email_sign_up.requestFocus();
                    } catch(FirebaseAuthUserCollisionException e) {
                        // User is exist
                        email_sign_up.setError(getString(R.string.email_used));
                        email_sign_up.requestFocus();
                    } catch(Exception e) {
                        Log.e("Registering exception", e.getMessage());
                    }
                }
            }

            private void loadUserInfo() {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                // get reference of user's data in database.
                DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Users")
                        .child(firebaseUser.getUid());


                // Create new instance of User with given values.
                User userss = new User(emailValue, passwordValue, name);

                // Put the instance into database.
                mReference.setValue(userss);

                // Update user name.

            }
        });
    }

   /* private void verificationDialog(final String email) {
        // Building dialog for verifying E-mail address.
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false).setMessage("Is this your correct E-mail address?\n" + email
                + "\nWe will send you a verification email");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendEmailVerification();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    firebaseUser.delete();
                }
                dialog.cancel();
                Toast.makeText(SignUpStudent.this, "User not created", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setTitle("Verifying E-mail address");
        alertDialog.show();
    } */

   /* private void sendEmailVerification() {
        progressDialog.setMessage("Sending email...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    // Task is successful, start HomeActivity.
                    Toast.makeText(SignUpStudent.this, "Email verification sent successfully", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(SignUpStudent.this, Students_Services_Sections.class));
                    finish();
                } else {
                    // Task failed.
                    Toast.makeText(SignUpStudent.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    firebaseUser.delete();
                }
            }
        });
    } */

    private boolean checkForConnection() {
        // Returning true if there is an internet connection, false if not.
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    private boolean isEmailValid(String email) {
        // TODO: add your own logic
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        // TODO: add your own logic
        return password.length() > 5;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


   /* @Override
    public void onBackPressed() {
        Toast.makeText(SignUp_Normal_Users.this, "Admins Services Sections", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SignUp_Normal_Users.this, AdminPlatform.class));
        finish();
    } */




    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUp.this, SignIn.class));

        finish();
    }
}