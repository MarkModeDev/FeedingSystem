package FEEDING.SYSTEM;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class PROFILE extends AppCompatActivity {

    private EditText userPhonenumber, userName, userEmail, userPassword;
    private TextView emailBtn;
    DatabaseReference userRef;
    FirebaseUser user;
    private FirebaseAuth mAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        onbackedpressed();

        //initialize mo boi dito para maayos man
        final EditText inputMobile = findViewById(R.id.usermobilenumber_textview);
        final ProgressBar progressBar = findViewById(R.id.progressbar);
        TextView buttonSendOTP = findViewById(R.id.mobile_verificationBtn);

       /* userName = findViewById(R.id.username_edit_profile);
        userEmail = findViewById(R.id.email_edit_profile);
        userPassword = findViewById(R.id.password_edit_profile);
        user = mAuth.getCurrentUser();


        //methods dito
        //   showUserData();*/



        buttonSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(PROFILE.this, "Enter mobilenumber", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                buttonSendOTP.setVisibility(View.INVISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+63" + inputMobile.getText().toString(), 60, TimeUnit.SECONDS, PROFILE.this,

                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                buttonSendOTP.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                buttonSendOTP.setVisibility(View.VISIBLE);
                                Toast.makeText(PROFILE.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                buttonSendOTP.setVisibility(View.VISIBLE);


                                Intent intent = new Intent(getApplicationContext(), DashboardOtpVerification.class);
                                intent.putExtra("mobile", inputMobile.getText().toString());
                                intent.putExtra("verificationId", verificationId);
                                startActivity(intent);
                            }
                        }
                );


            }
        });



    }


    private void onbackedpressed() {
        // Create a callback for back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle back button press
                Intent intent = new Intent(PROFILE.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        };

        // Add callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);

    }
}