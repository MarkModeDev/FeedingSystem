package FEEDING.SYSTEM;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HISTORY extends AppCompatActivity {
    TextView date,sld;
    RecyclerView history;
    ArrayList<DataModel> list;
    DataAdapter Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        date = findViewById(R.id.datepick);
        history = findViewById(R.id.historyrecyle);
        sld = findViewById(R.id.sd);

        //variables
        Calendar calendar1 = Calendar.getInstance();



        CharSequence selectedData = android.text.format.DateFormat.format("MMMM dd, yyyy", calendar1);
        date.setText(selectedData);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendate();
            }
        });
        sld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdatahistory();
            }
        });
        getdatahistory();
        //methods
        onbackedpressed();
    }

    private void opendate(){
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR,year);
                calendar1.set(Calendar.MONTH,month);
                calendar1.set(Calendar.DATE,dayOfMonth);
                CharSequence selectedData = android.text.format.DateFormat.format("MMMM dd, yyyy", calendar1);
                date.setText(selectedData);
                Toast.makeText(HISTORY.this, "Success", Toast.LENGTH_SHORT).show();
            }
        },year, month,dayOfMonth);
        datePickerDialog.show();

    }
    private void onbackedpressed() {
        // Create a callback for back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle back button press
                Intent intent = new Intent(HISTORY.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        };

        // Add callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
    private void getdatahistory(){
        DatabaseReference refre = FirebaseDatabase.getInstance().getReference().child("DataHistory").child(date.getText().toString());

        history.setHasFixedSize(true);
        history.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        Adapter = new DataAdapter(this, list);
        history.setAdapter(Adapter);

        refre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    DataModel dm =dataSnapshot.getValue(DataModel.class);
                    list.add(dm);
                }
                Adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}