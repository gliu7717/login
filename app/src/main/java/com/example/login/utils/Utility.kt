package com.example.login.utils
import android.app.AlertDialog
import android.content.Context

object Utility {

    fun showAlert(
        context: Context,
        title: String = "",
        message: String = "",
        onYes: Runnable? = null,
        onNo: Runnable? = null
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(android.R.string.yes) { dialog, which ->
            onYes?.run()
        }

        alertDialogBuilder.setNegativeButton(android.R.string.no) { dialog, which ->
            onNo?.run()
        }
        alertDialogBuilder.show()
    }
}
