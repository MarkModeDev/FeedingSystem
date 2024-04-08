package FEEDING.SYSTEM;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity {

    TextView createAccount, loginButton, registerButton;
    EditText loginEmail, loginPassword;
    ProgressDialog progressDialog;
    DatabaseReference usersRef;


    public static final String SHARED_PREF = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //dito mona lahat pare i declare lahat ng variables mag reassign kanalang para mabilis naten
        //makuha ang error's
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        createAccount = findViewById(R.id.createaccount_textview);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        // Example usage
        String email = loginEmail.getText().toString();
        checkifUserExist(email);

        //dito na yung mga methods
        onbackedpressed();
        checkBox();


        createAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ChooseRegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                //restriction for email and password
                if (email.length() == 0 || password.length() == 0) {
                    progressDialog.hide();
                    loginEmail.setError("Enter email");
                    loginPassword.setError("Enter password");
                    loginEmail.setFocusable(true);
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.hide();
                            if (task.isSuccessful()) {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {

                                    //ito yung pang remember ng user para laging naka sign in yung pare
                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("name","true");
                                    editor.apply();

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("Email", loginEmail.getText().toString());
                                    map.put("Password", loginPassword.getText().toString());
                                    map.put("Phonenumber", "");
                                    map.put("Username", "");

                                    FirebaseDatabase.getInstance().getReference().child("Users").push()
                                            .setValue(map)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // wala naman ilalagay dito kaya walang laman
                                                }
                                            });

                                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                    loginEmail.setText("");
                                    loginPassword.setText("");
                                } else if(!task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "linked sent was expired", Toast.LENGTH_SHORT).show();
                                    deleteOnCanceledAccountInAuth();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                }
                                {
                                 //   Toast.makeText(LoginActivity.this, "or else Create an account", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Create an account first", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private void checkBox(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String checked = sharedPreferences.getString("name","");

        if(checked.equals("true")){
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            loginEmail.setText("");
            loginPassword.setText("");
            finish();

        }

    }

    private void onbackedpressed() {
        // Create a callback for back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle back button press

                // Create an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Are you sure you want to close the application?");
                builder.setCancelable(true);

                // postive button obvious naman diba hahahaha
                builder.setPositiveButton(
                        "Yes",
                        (dialog, id) -> {
                            // Finish the activity when "Yes" is clicked
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

    private void deleteOnCanceledAccountInAuth(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                            }else{

                            }
                        }
                    });
        }

    }

    private void checkifUserExist(final String email){


    }
}