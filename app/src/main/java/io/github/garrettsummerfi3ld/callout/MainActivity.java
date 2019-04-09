package io.github.garrettsummerfi3ld.callout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // Global vars
    EditText  debugPhoneNumber;
    Button  calloutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Pulls values from the textbox in the main activity
        debugPhoneNumber = (EditText) findViewById(R.id.debugPhoneNumberInput);
        calloutButton = (Button) findViewById(R.id.calloutButton);
    }


    // Triggered by pressing the callout button in the main activity
    public void calloutMessage(View view) {
        // Local vars
        String calloutMessage = "This is a test of the Callout system on the users phone, if you have received this, please ignore.";
        String calloutDebugPhoneInput = debugPhoneNumber.getText().toString();

        // Try catch with SMS
        try {
            // SMS Handler
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(calloutDebugPhoneInput, null, calloutMessage, null, null);
            Log.i("calloutMessage", "Send message to number: '" + calloutDebugPhoneInput + "'!");

            // Confirmation toast and logging
            Toast.makeText(this, "[DEBUG] Calling out to your contacts!", Toast.LENGTH_LONG).show();
            Log.i("calloutMessage", "Triggered toast message");
        } catch (Exception ex) {
            // Failure toast, logging, and printing stacktrace of the error
            Toast.makeText(this, "[DEBUG] SMS Failed! Check logcat!", Toast.LENGTH_LONG).show();
            Log.e("calloutMessage", "Error! Printing stacktrace...");
            ex.printStackTrace();
        }
    }
}
