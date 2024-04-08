package FEEDING.SYSTEM;



import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EmailSendActivity extends AppCompatActivity {

    EditText userEmail, userPassword;
    TextView sendButton;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_send);

        //dito lang mga variables pare re assign nalang sa baba
        userEmail = findViewById(R.id.verification_email);
        userPassword = findViewById(R.id.password_verification);
        sendButton = findViewById(R.id.sendemail_button);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");

        //methods naman dito pare
     //   onbackedpressed();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();

                //pagpasok ng data sa authentication
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (firebaseAuth != null) {
                                        //Toast.makeText(EmailSendActivity.this, "Check your email"+ "/n to verify your account", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EmailSendActivity.this, EmailVerificationActivity.class);
                                        startActivity(intent);
                                        finish();


                                    } else {

                                    }
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                        } else {

                        }

                    }
                });

            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

}