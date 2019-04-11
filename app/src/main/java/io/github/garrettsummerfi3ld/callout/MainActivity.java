package io.github.garrettsummerfi3ld.callout;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // Global vars
    private static final int REQUEST_READ_CONTACTS = 79;
    SmsManager smsManager = SmsManager.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // On run, there is a check for permissions
        // Run through Contact permissions first, then check for SMS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            } else {
                requestSMSPermission();
            }
        } else {
            requestContactPermission();
        }
    }

    // Request Reading Contact Permission
    protected void requestContactPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
    }

    // Request Sending SMS Permission
    protected void requestSMSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                } else {

                }
                return;
            }
        }
    }

    public void getContacts() {

        String phoneNumber = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        StringBuffer output = new StringBuffer();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        // Loop for every contact in the phone  
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                // Local vars
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    output.append("\n First Name:" + name);
                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);
                    }
                    phoneCursor.close();
                }
                output.append("\n");
            }
        }
    }

    // Triggered by pressing the callout button in the main activity
    public void calloutMessage(View view) {
        // Try catch with SMS
        try {
            // SMS Handler
            smsManager.sendTextMessage(String.valueOf(R.string.callout_title), null, String.valueOf(R.string.callout_message_debug), null, null);
            Log.i("calloutMessage", "Send message to number: '" + calloutDebugPhoneInput + "'!");

            // Confirmation toast and logging
            Toast.makeText(this, "Calling out to your contacts!", Toast.LENGTH_LONG).show();
            Log.i("calloutMessage", "Triggered toast message");
        } catch (Exception ex) {
            // Failure toast, logging, and printing stacktrace of the error
            Toast.makeText(this, "SMS Failed! Check logcat!", Toast.LENGTH_LONG).show();
            Log.e("calloutMessage", "Error! Printing stacktrace...");
            ex.printStackTrace();
        }
    }
}
