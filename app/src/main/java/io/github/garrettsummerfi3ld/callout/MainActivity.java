package io.github.garrettsummerfi3ld.callout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // Global vars
    EditText  debugPhoneNumber;
    SmsManager smsManager = SmsManager.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Pulls values from the textbox in the main activity
        debugPhoneNumber = findViewById(R.id.debugPhoneNumberInput);

        // Checks in package manager for permissions to  use SMS and contacts
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS) && (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)))) {

            } else {
                Object CALLOUT_REQUEST_READ_CONTACTS;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, new int[]);
            }
        }
    }

    // Triggered by pressing the callout button in the main activity
    public void calloutMessage(View view) {
        // Local vars
        String calloutMessage = "This is a test of the Callout system on the users phone, if you have received this, please ignore.";
        String calloutDebugPhoneInput = debugPhoneNumber.getText().toString();

        // Try catch with SMS
        try {
            // SMS Handler
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
