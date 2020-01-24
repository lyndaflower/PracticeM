package com.example.mobicassegn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText mNameEdit;
    private EditText mEmailEdit;
    private EditText mPasswordText;

    private EditText mComfirmPassword;
    private Button mSignUp;
    private TextView mLogIn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mAuthenticationProgressDialog;
    private String mName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNameEdit = (EditText) findViewById(R.id.nameText);
        mEmailEdit = (EditText) findViewById(R.id.emailText);
        mPasswordText = (EditText) findViewById(R.id.passwordText);
        mComfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        mSignUp = (Button) findViewById(R.id.createUser);
        mLogIn = (TextView) findViewById(R.id.login);


        mLogIn.setOnClickListener(this);
        mSignUp.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        createAuthenticationProgressDialog();

        signUpAuthStateListener();
    }



    @Override
    public void onClick(View v) {
        if (v == mLogIn) {
            Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if (v == mSignUp) {
            signUp();
        }
    }

    private void signUp() {

        final String name = mNameEdit.getText().toString().trim();
        final String email = mEmailEdit.getText().toString().trim();
        final String password = mPasswordText.getText().toString().trim();
        final String comfirmPassword = mComfirmPassword.getText().toString().trim();
        mName = mNameEdit.getText().toString().trim();

        boolean validEmail = isValidEmail(email);
        boolean validName = validName(name);
        boolean validPassword = validPassword(password, comfirmPassword);
//        boolean validName1= validName(mName);
        if (!validEmail || !validName || !validPassword) return;

        mAuthenticationProgressDialog.show();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        mAuthenticationProgressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Log.d(TAG, "Authentication successful");
                            createFirebaseUserProfile(task.getResult().getUser());
                        } else {
                            Toast.makeText(RegisterActivity.this, "Your account saved successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUpAuthStateListener() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private boolean isValidEmail(String email) {
        boolean isAwesomeEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isAwesomeEmail) {
            mEmailEdit.setError("Please enter a valid email address");
            return false;
        }
        return isAwesomeEmail;
    }

    private boolean validName(String name) {
        if (name.equals("")){
            mNameEdit.setError("Please enter your name ");
            return false;
        }
        return true;
    }

    private boolean validPassword(String password , String comfirmPassword) {
        if (password.length() <6) {
            mPasswordText.setError("Please create a password with at least 6 characters");
            return false;
        } else if (!password.equals(comfirmPassword)) {
            mPasswordText.setError("Password do not match");
            return false;
        }
        return true;
    }

    private void createAuthenticationProgressDialog() {
        mAuthenticationProgressDialog =new ProgressDialog(this);
        mAuthenticationProgressDialog.setTitle("Loading ......");
        mAuthenticationProgressDialog.setMessage("Sign up to Lancome paris ...");
        mAuthenticationProgressDialog.setCancelable(false);

    }

    private void createFirebaseUserProfile(final FirebaseUser user) {

        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName)
                .build();

        user.updateProfile(addProfileName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, user.getDisplayName());
                        }
                    }

                });
    }

}