package io.github.garrettsummerfi3ld.callout

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

class AlertSmsFragment : DialogFragment() {
    fun onCreateDialog(savedInstanceState: Bundle, alertFlag: Int): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(R.string.app_name).setMessage(R.string.sms_perm_dialog)
                .setPositiveButton(R.string.ok_dialog) { dialog, id ->
                    // User accepted the dialog
                }
                .setNegativeButton(R.string.cancel_dialog) { dialog, id ->
                    // User cancelled the dialog
                }
                .setIcon(R.drawable.ic_priority_high_black_24dp)
        // Create the AlertDialog object and return it
        return builder.create()
    }
}