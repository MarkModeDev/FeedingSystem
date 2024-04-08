package FEEDING.SYSTEM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ACTIVITY_LOGS extends AppCompatActivity {

    TextView date,but;
    RecyclerView history;
    ArrayList<LogsActivityModel> list;
    LogsAdapter Adapter;
    String user;
    FirebaseAuth auth;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        //variables
        history = findViewById(R.id.logrcv);
        date = findViewById(R.id.datev);
        but = findViewById(R.id.dtbut);

        //methods
        spinnerNaMalupet();


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendate();
            }
        });
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getlogs();
            }
        });



    }
    private void spinnerNaMalupet(){

        spinner = findViewById(R.id.logs_dateSpinner);
        String [] menu ={"Logged In","Logged Out","Feed History"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,menu);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position){
                    case 1:
                        // dito mo ilagay yung condition mo kapag napindot index 1 yung "Logged in"
                        break;
                    case 2:
                        // dito mo ilagay yung condition mo kapag napindot index 2 yung "Logged Out"

                        break;
                    case 3:
                        // dito mo ilagay yung condition mo kapag napindot index 3 yung "Feed History"

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                Toast.makeText(ACTIVITY_LOGS.this, "Success", Toast.LENGTH_SHORT).show();
            }
        },year, month,dayOfMonth);
        datePickerDialog.show();

    }

    private void getlogs(){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference refre = FirebaseDatabase.getInstance().getReference().child("UserLogs").child(user).child(date.getText().toString()).child("UserLogIn");
        history.setHasFixedSize(true);
        history.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        Adapter = new LogsAdapter(this,list);
        history.setAdapter(Adapter);

        refre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    LogsActivityModel dm =dataSnapshot.getValue(LogsActivityModel.class);
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