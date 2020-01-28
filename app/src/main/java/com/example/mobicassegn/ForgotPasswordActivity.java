package com.example.mobicassegn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mEmailReset;
    private Button mResetPassword;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mEmailReset = (EditText) findViewById(R.id.forgotEmail);
        mResetPassword = (Button) findViewById(R.id.resetpassword);
        firebaseAuth = FirebaseAuth.getInstance();

        mResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = mEmailReset.getText().toString().trim();
                if (useremail.equals("")){
                    Toast.makeText(ForgotPasswordActivity.this,"Email send successful" ,Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ForgotPasswordActivity.this,"Email send successful",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this,LogInActivity.class));
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this, "Email not send ,Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }


}