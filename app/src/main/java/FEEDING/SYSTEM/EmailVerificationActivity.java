package FEEDING.SYSTEM;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EmailVerificationActivity extends AppCompatActivity {
    TextView goToLogin;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        //initialize ng variables dito pare re assign kanalang sa baba
        goToLogin = findViewById(R.id.loginredirect_button);
        database = FirebaseDatabase.getInstance();


        //methods dito pare
        onbackedpressed();



        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(EmailVerificationActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
    private void onbackedpressed() {
        // Create a callback for back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle back button press
                AlertDialog.Builder builder = new AlertDialog.Builder(EmailVerificationActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("All credential you entered will be lost");
                builder.setCancelable(true);

                // postive button obvious naman diba hahahaha
                builder.setPositiveButton(
                        "Yes",
                        (dialog, id) -> {
                            // Finish the activity when "Yes" is clicked
                            deleteOnCanceledAccountInAuth();

                          //  deleteOnCanceledAccountInRealTime();
                            Intent intent = new Intent(EmailVerificationActivity.this,LoginActivity.class);
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

    private void deleteOnCanceledAccountInAuth(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(EmailVerificationActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                            }else{

                            }
                        }
                    });
        }

    }

    private void onReplacement(){
        Map<String ,Object> map = new HashMap<>();

    }


}
