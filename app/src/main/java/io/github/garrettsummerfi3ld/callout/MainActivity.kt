package io.github.garrettsummerfi3ld.callout

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    internal var smsManager = SmsManager.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * This is to request permissions for the phone for the app to work, without these permissions, the app will not
         * function as normal
         * @param READ_CONTACTS reads contacts for sending callouts to
         * @param SEND_SMS sends SMS for sending callouts to the contacts
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                getContacts()
            } else {
                requestSMSPermission()
            }
        } else {
            requestContactPermission()
        }
    }

    // Request Reading Contact Permission
    protected fun requestContactPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS)
        }
    }

    // Request Sending SMS Permission
    protected fun requestSMSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), REQUEST_SEND_SMS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_READ_CONTACTS -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else {

                }
                return
            }
        }
    }

    fun getContacts() {

        var phoneNumber: String? = null

        val CONTENT_URI = ContactsContract.Contacts.CONTENT_URI
        val _ID = ContactsContract.Contacts._ID
        val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
        val HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER

        val PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        val NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER

        val output = StringBuffer()

        val contentResolver = contentResolver

        val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)

        // Loop for every contact in the phone
        if (cursor!!.count > 0) {

            while (cursor.moveToNext()) {
                // Local vars
                val contact_id = cursor.getString(cursor.getColumnIndex(_ID))
                val name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME))
                val hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)))
                if (hasPhoneNumber > 0) {
                    output.append("\n First Name:$name")
                    // Query and loop for every phone number of the contact
                    val phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,
                            "$Phone_CONTACT_ID = ?", arrayOf(contact_id), null)
                    while (phoneCursor!!.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER))
                        output.append("\n Phone number:" + phoneNumber!!)
                    }
                    phoneCursor.close()
                }
                output.append("\n")
            }
        }
    }

    // Triggered by pressing the callout button in the main activity
    fun calloutMessage(view: View) {
        // Try catch with SMS
        try {
            // SMS Handler
            smsManager.sendTextMessage(R.string.callout_title.toString(),
                    null, R.string.callout_message_debug.toString(), null,
                    null)
            val calloutDebugPhoneInput: String? = null
            Log.i("calloutMessage", "Send message to number: '$calloutDebugPhoneInput'!")

            // Confirmation toast and logging
            Toast.makeText(this, "Calling out to your contacts!", Toast.LENGTH_LONG).show()
            Log.i("calloutMessage", "Triggered toast message")
        } catch (ex: Exception) {
            // Failure toast, logging, and printing stacktrace of the error
            Toast.makeText(this, "SMS Failed! Check logcat!", Toast.LENGTH_LONG).show()
            Log.e("calloutMessage", "Error! Printing stacktrace...")
            ex.printStackTrace()
        }

    }

    companion object {
        // Global vars
        private val REQUEST_READ_CONTACTS = 79
        private val REQUEST_SEND_SMS = 79
    }

    class MainActivity : ContactLoaderFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentVew(R.layout.fragment_contact_loader)
        }
    }

}
