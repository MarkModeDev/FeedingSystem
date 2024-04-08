package FEEDING.SYSTEM;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

public class SCHEDULE extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //variables






        //methods
        onbackedpressed();



    }
    private void onbackedpressed() {
        // Create a callback for back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle back button press
                Intent intent = new Intent(SCHEDULE.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        };

        // Add callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

}