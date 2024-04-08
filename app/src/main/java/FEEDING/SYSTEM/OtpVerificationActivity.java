package FEEDING.SYSTEM;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends AppCompatActivity {

    private EditText otp1,otp2,otp3,otp4,otp5,otp6;
    private String verificationId;
    TextView resendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        otp1 = findViewById(R.id.code1);
        otp2 = findViewById(R.id.code2);
        otp3 = findViewById(R.id.code3);
        otp4 = findViewById(R.id.code4);
        otp5 = findViewById(R.id.code5);
        otp6 = findViewById(R.id.code6);

        final ProgressBar progressBar = findViewById(R.id.progressbar);
        final TextView verificationButton = findViewById(R.id.validate_button);
        resendOTP = findViewById(R.id.textResendOTP);

        verificationId = getIntent().getStringExtra("verificationId");

        // ito naman para sa continues input ng user sa OTP EditText
        setUpOTPinputs();
      //  displayMobileNumber();
        TextView textMobile = findViewById(R.id.user_mobilenumber);
        textMobile.setText(String.format(
                // pang display lang ito ng mobile number dun sa textview
                "+63%s",getIntent().getStringExtra("mobile")
        ));

        verificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otp1.getText().toString().trim().isEmpty()
                        || otp2.getText().toString().trim().isEmpty()
                        || otp3.getText().toString().trim().isEmpty()
                        || otp4.getText().toString().trim().isEmpty()
                        || otp5.getText().toString().trim().isEmpty()
                        || otp6.getText().toString().trim().isEmpty()){
                    Toast.makeText(OtpVerificationActivity.this, "Please enter Valid code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = otp1.getText().toString() +
                        otp2.getText().toString() +
                        otp3.getText().toString() +
                        otp4.getText().toString() +
                        otp5.getText().toString() +
                        otp6.getText().toString();

                if(verificationId != null){
                    progressBar.setVisibility(View.VISIBLE);
                    verificationButton.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId,code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            verificationButton.setVisibility(View.VISIBLE);
                            if(task.isSuccessful()){

                                // ito yung pag pasok ng data sa realtime
                                Map<String, Object> map = new HashMap<>();
                                map.put("Email ", "");
                                map.put("Password ","");
                                map.put("Phonenumber ",textMobile.getText().toString());


                                FirebaseDatabase.getInstance().getReference().child("Users").push()
                                        .setValue(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                // wala naman ilalagay dito kaya walang laman
                                            }
                                        });
                                Intent intent = new Intent(OtpVerificationActivity.this, DashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(OtpVerificationActivity.this, "Verification code invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    resendOTP.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+63" + getIntent().getStringExtra("mobile"),60, TimeUnit.SECONDS,OtpVerificationActivity.this,

                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            Toast.makeText(OtpVerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            verificationId = newVerificationId;
                                            Toast.makeText(OtpVerificationActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                        }
                    });
                }
            }
        });

    }
    private void setUpOTPinputs(){
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    otp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    otp3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    otp4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    otp5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    otp6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void displayMobileNumber(){

    }
    private void onbackedpressed() {
        // Create a callback for back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle back button press
                AlertDialog.Builder builder = new AlertDialog.Builder(OtpVerificationActivity.this);
                builder.setMessage("Are you sure you want to cancel?");
                builder.setCancelable(true);

                // postive button obvious naman diba hahahaha
                builder.setPositiveButton(
                        "Cancel",
                        (dialog, id) -> {
                            Intent intent = new Intent(OtpVerificationActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });

                // Add negative button
                builder.setNegativeButton(
                        "No",
                        (dialog, id) -> {
                            // Dismiss the dialog when "No" is clicked
                            dialog.cancel();
                        });

                // Create and show the AlertDialog
                AlertDialog alert = builder.create();
                alert.show();

            }
        };

        // Add callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }


}