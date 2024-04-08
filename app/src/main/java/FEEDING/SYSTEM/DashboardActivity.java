package FEEDING.SYSTEM;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {
    private Spinner spinner;
    TextView feed,sensor;
    ImageView imageView;
    public static final String SHARED_PREF = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variables
        feed = findViewById(R.id.feeding_btn);
        sensor = findViewById(R.id.sensor_btn);
        imageView = findViewById(R.id.info_dashboard);


        //methods
        onbackedpressed();

        /* para sa papunta sa feeding layout*/
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FEEDING.class);
                startActivity(intent);
                finish();
            }
        });

        spinner = findViewById(R.id.name);

        String [] menu = {"Username","Profile","Schedule","History"," Logout"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,menu);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i){
                    case 1:
                        Intent intent_1 = new Intent(getApplicationContext(),PROFILE.class);
                        startActivity(intent_1);
                        finish();
                        break;
                    case 2:
                        Intent intent_2 = new Intent(getApplicationContext(),SCHEDULE.class);
                        startActivity(intent_2);
                        finish();
                        break;
                    case 3:
                        Intent intent_3 = new Intent(getApplicationContext(),HISTORY.class);
                        startActivity(intent_3);
                        finish();
                        break;
                    case 4:
                        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setCancelable(true);

                        // postive button obvious naman diba hahahaha
                        builder.setPositiveButton(
                                "Logout",
                                (dialog, id) -> {

                                    //para sa remember me
                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("name","");
                                    editor.apply();

                                    // Finish the activity when "Yes" is clicked
                                    Intent intent = new Intent(DashboardActivity.this,LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                });

                        // Add negative button
                        builder.setNegativeButton(
                                "Cancel",
                                (dialog, id) -> {
                                    // Dismiss the dialog when "No" is clicked
                                    dialog.cancel();
                                });

                        // Create and show the AlertDialog
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void onbackedpressed() {
        // Create a callback for back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle back button press

                // Create an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setMessage("Are you sure you want to close the application?");
                builder.setCancelable(true);

                // postive button obvious naman diba hahahaha
                builder.setPositiveButton(
                        "Yes",
                        (dialog, id) -> {
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

   /* private void showSuccessDialog() {
        LinearLayout successConstrainLayout = findViewById(R.id.succes_constraintlayout);
        View view = LayoutInflater.from(DASHBOARD.this).inflate(R.layout.info,successConstrainLayout);
        Button done = findViewById(R.id.done_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(DASHBOARD.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });







    }*/
}