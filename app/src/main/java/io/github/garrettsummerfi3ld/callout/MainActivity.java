package io.github.garrettsummerfi3ld.callout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Local vars
    private static final int CALLOUT_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private final String calloutMessage = "This is a test of the Callout system on the users phone, if you have received this, please ignore.";

    // Triggered by pressing the callout button in the main activity
    public void calloutMessage(View view) {

        // Confirmation toast
        Toast.makeText(this, "[DEBUG] Calling out to your contacts!", Toast.LENGTH_LONG).show();
        Log.i("calloutMessage","Triggered toast message");
    }
}
