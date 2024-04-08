package FEEDING.SYSTEM;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class DashboardActivity extends AppCompatActivity {
    private Spinner spinner;
    DonutProgress ammprog;
    TextView feed,sensor, ammonia, phlevel, temp,filter;
    ImageView imageView;
    FirebaseDatabase database;
    LinearLayout rec;
    DatabaseReference reference;
    DatabaseReference reference1;
    DatabaseReference reference2;
    DatabaseReference reference3;
    public static final String SHARED_PREF = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variables
        feed = findViewById(R.id.feeding_btn);
        sensor = findViewById(R.id.sensor_btn);
        imageView = findViewById(R.id.info_dashboard);
        ammonia = findViewById(R.id.amm);
        phlevel = findViewById(R.id.ph);
        temp = findViewById(R.id.tem);
        ammprog = findViewById(R.id.amonia_progress);
        rec = findViewById(R.id.rec);
        filter = findViewById(R.id.Filter_button);


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference3 = FirebaseDatabase.getInstance().getReference("Filter");
                reference3.setValue(1);
                Toast.makeText(DashboardActivity.this, "Filtering", Toast.LENGTH_SHORT).show();


            }
        });


        getdata();
       // colorbar();
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
                        Intent intent_2 = new Intent(getApplicationContext(),ACTIVITY_LOGS.class);
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
                                    logoutlogs();
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
    rec.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            History();
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
    private void getdata(){
        database = FirebaseDatabase.getInstance();
        //
        reference = database.getReference().child("Sensors").child("ammonia");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Integer data = snapshot.getValue(Integer.class);
                    String dt = Integer.toString(data);

                    ammonia.setText(dt);
                    if(data >= 0 && data <= 25){
                        ammprog.setFinishedStrokeColor(getResources().getColor(R.color.SAFE));
                        double doubleValue = Integer.valueOf(dt).doubleValue();
                        double percentageF = doubleValue/100;
                        double Final = percentageF * 100;
                      //  int dat = (int) Final;
                        Toast.makeText(DashboardActivity.this, "" + doubleValue, Toast.LENGTH_SHORT).show();
                        ammprog.setProgress((float) Final);
                                //(Color.rgb(255,0,0));
                    }
                    if(data >= 25 && data <= 60){
                        ammprog.setFinishedStrokeColor(getResources().getColor(R.color.TOXIC));
                        double doubleValue = Integer.valueOf(dt).doubleValue();
                        double percentageF = doubleValue/100;
                        double Final = percentageF * 100;
                        //  int dat = (int) Final;
                        Toast.makeText(DashboardActivity.this, "" + doubleValue, Toast.LENGTH_SHORT).show();
                        ammprog.setProgress((float) Final);
                        //(Color.rgb(255,0,0));
                    }
                    if(data >= 51 && data <= 1000){
                        ammprog.setFinishedStrokeColor(getResources().getColor(R.color.DEADLY));
                        double doubleValue = Integer.valueOf(dt).doubleValue();
                        double percentageF = doubleValue/100;
                        double Final = percentageF * 100;
                        //  int dat = (int) Final;
                        Toast.makeText(DashboardActivity.this, "" + doubleValue, Toast.LENGTH_SHORT).show();
                        ammprog.setProgress((float) Final);
                        //(Color.rgb(255,0,0));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //
        reference1 = database.getReference().child("Sensors").child("ph");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Double data = snapshot.getValue(Double.class);
                    String dt = Double.toString(data);

                    phlevel.setText(dt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //
        reference2 = database.getReference().child("Sensors").child("temperature");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Double data = snapshot.getValue(Double.class);
                    String dt = Double.toString(data);

                    temp.setText(dt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private void logoutlogs(){
        Map<String, Object> maps = new HashMap<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String emm = firebaseAuth.getCurrentUser().getEmail();
        String uid =  firebaseAuth.getCurrentUser().getUid();
        maps.put("Email", emm);
        maps.put("Status", "UserHasLoggedOut");
        Calendar calendar1 = Calendar.getInstance();
        CharSequence selectedData = android.text.format.DateFormat.format("MMMM dd, yyyy", calendar1);
        CharSequence dayname = android.text.format.DateFormat.format("EEEE", calendar1);
        String times = new SimpleDateFormat("hh:mm a ss", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a ", Locale.getDefault()).format(new Date());

        String Uid = UUID.randomUUID().toString();

        maps.put("Date", selectedData);
        maps.put("Time", time);
        maps.put("Uid", uid);
        //     database = FirebaseDatabase.getInstance();
        //    reference = database.getReference("UserLogs").child(uid).child((String) selectedData);
        //   reference.setValue(maps);
        FirebaseDatabase.getInstance().getReference().child("UserLogs").child(uid).child((String) selectedData).child("UserLogOut").child(Uid)
                .setValue(maps)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // wala naman ilalagay dito kaya walang laman
                    }
                });
    }
    private void History() {

        String amm = ammonia.getText().toString();
        String ph = phlevel.getText().toString();
        String tempe = temp.getText().toString();
        Calendar calendar1 = Calendar.getInstance();

        CharSequence selectedData = android.text.format.DateFormat.format("MMMM dd, yyyy", calendar1);
        CharSequence dayname = android.text.format.DateFormat.format("EEEE", calendar1);
        String times = new SimpleDateFormat("hh:mm a ss", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a ", Locale.getDefault()).format(new Date());
        String Uid = UUID.randomUUID().toString();
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("Uid",Uid);
        hashMap.put("Date",selectedData);
        hashMap.put("Time",time);
        hashMap.put("Ammonia",amm);
        hashMap.put("PhLevel",ph);
        hashMap.put("Temperature",tempe);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("DataHistory").child((String) selectedData).child(times);
        reference.setValue(hashMap);





    }







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
