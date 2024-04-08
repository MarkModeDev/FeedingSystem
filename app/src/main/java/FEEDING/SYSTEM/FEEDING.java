package FEEDING.SYSTEM;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class FEEDING extends AppCompatActivity {
    TextView feed,sensor,tm,sttt,stat,save;
    EditText fish;
    LinearLayout p,st,feedbut;
    ImageView ad, ms;
    FirebaseDatabase database;
    DatabaseReference databaseReference,databaseReference1,databaseReference2,databaseReference3;
    int timerHour, timerMinute;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeding);


        //varibles
        feed = findViewById(R.id.feeding_btn_feed);
        sensor = findViewById(R.id.sensor_btn_feed);
        p = findViewById(R.id.pick);
        st = findViewById(R.id.settime);
        sttt = findViewById(R.id.stt);
        tm = findViewById(R.id.timee);
        fish = findViewById(R.id.noff);
        stat = findViewById(R.id.feedStatus_textview);
        ad = findViewById(R.id.add);
        ms = findViewById(R.id.minus);
        feedbut = findViewById(R.id.fdd);
        save = findViewById(R.id.save_number_fish);


        status();

       p.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Calendar calendar = Calendar.getInstance();
               int hours = calendar.get(Calendar.HOUR_OF_DAY);
               int min = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(FEEDING.this, androidx.appcompat.R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY,i);
                        c.set(Calendar.MINUTE,i1);
                        c.setTimeZone(TimeZone.getDefault());
                        SimpleDateFormat format  = new SimpleDateFormat("hh:mm a");
                        String time = format.format(c.getTime());
                        tm.setText(time);
                    }
                },hours,min, false);
                timePickerDialog.show();

           }
       });
       st.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               databaseReference = FirebaseDatabase.getInstance().getReference("feed");

               try {
                   // Parse the input time string
                   SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
                   Date date = inputFormat.parse(tm.getText().toString());

                   // Format the parsed time to military time format
                   SimpleDateFormat outputFormat = new SimpleDateFormat("HHmm");
                   String militaryTime = outputFormat.format(date);
                   Map<String, Object> map = new HashMap<>();
                   map.put("1",militaryTime);
                   databaseReference.child("feedTimer").updateChildren(map);
                   System.out.println("Military Time: " + militaryTime);
               } catch (ParseException e) {
                   System.out.println("Error parsing time: " + e.getMessage());
               }

           }
       });
       save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Integer fs = Integer.valueOf(fish.getText().toString());

              
           }
       });

        autofeed();
        //methods
        onbackedpressed();

        /* para sa papunta sa dashboard layout*/
        sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Fishnum(){
        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String data =  fish.getText().toString();
                Integer add = Integer.valueOf(data);
                Integer f = add + 1;
                String Final = f.toString();
                fish.setText(Final);
                fish.setTextColor(Color.BLACK);

            }
        });
        ms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String data =  fish.getText().toString();
                Integer add = Integer.valueOf(data);
                Integer f = add - 1;


                String Final = f.toString();
                fish.setText(Final);
                fish.setTextColor(Color.BLACK);
                if (f < 0){
                    fish.setText("0");
                    fish.setTextColor(Color.BLACK);
                }
            }
        });
        String data =  fish.getText().toString();
        Integer f = Integer.valueOf(data);
        if (f < 0){
            fish.setText("0");
            fish.setTextColor(Color.BLACK);
            Toast.makeText(this, "eror", Toast.LENGTH_SHORT).show();
        }

    }
    private void feedHis(){
        Map<String, Object> maps = new HashMap<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String emm = firebaseAuth.getCurrentUser().getEmail();
        String uid =  firebaseAuth.getCurrentUser().getUid();
        maps.put("Email", emm);
        maps.put("Status", "UserHasFeeded the Fish");
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
        FirebaseDatabase.getInstance().getReference().child("UserLogs").child(uid).child((String) selectedData).child("FeedHistory").child(Uid)
                .setValue(maps)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // wala naman ilalagay dito kaya walang laman
                    }
                });
    }
    private void status(){
        databaseReference = FirebaseDatabase.getInstance().getReference("feed").child("feedstatus");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Integer data = snapshot.getValue(Integer.class);
                    String dt = Integer.toString(data);
                    if(data == 0 ){
                        String stats = "Empty";
                        stat.setText(stats);
                        stat.setTextColor(Color.RED);
                    }
                    if(data == 1 ){
                        String stats = "Warning";
                        stat.setText(stats);
                        stat.setTextColor(Color.YELLOW);
                    }
                    if(data == 2 ) {
                        String stats = "FishFood is enough";
                        stat.setText(stats);
                        stat.setTextColor(Color.GREEN);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        databaseReference1 = FirebaseDatabase.getInstance().getReference("feed").child("feedNoFish");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Integer data = snapshot.getValue(Integer.class);
                    fish.setText(data.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void autofeed(){
        databaseReference = FirebaseDatabase.getInstance().getReference("feed").child("feedNow");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Integer data = snapshot.getValue(Integer.class);
                    if(data > 0 ){
                        feedHis();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void feed(){
        databaseReference = FirebaseDatabase.getInstance().getReference("feed").child("feedstatus");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Integer data = snapshot.getValue(Integer.class);
                    if(data == 0){

                        Toast.makeText(FEEDING.this, "Your fish Food is Empty", Toast.LENGTH_SHORT).show();
                    }else{
                        feedna();
                        feedHis();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void feedna(){
                databaseReference3 = FirebaseDatabase.getInstance().getReference("feed").child("feedNow");
                databaseReference3.setValue(1);
              //   Map<String, Object> map = new HashMap<>();
             //    map.put("feedNow",10);
//
              //       databaseReference3.child("feedNow").updateChildren(map);
                Toast.makeText(FEEDING.this, "Success Feeding", Toast.LENGTH_SHORT).show();
    }
    private void onbackedpressed() {
        // Create a callback for back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle back button press
                Intent intent = new Intent(FEEDING.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        };
        // Add callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

}