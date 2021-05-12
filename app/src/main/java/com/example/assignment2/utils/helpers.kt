package com.example.assignment2.utils

import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.assignment2.R


fun createLoader(activity: FragmentActivity) : AlertDialog {
    val loaderBuilder = AlertDialog.Builder(activity)
        .setCancelable(true) // 'false' if you want user to wait
        .setView(R.layout.loading)
    var loader = loaderBuilder.create()
    loader.setTitle(R.string.app_name)
    loader.setIcon(R.mipmap.ic_launcher_homer_round)

    return loader
}

fun showLoader(loader: AlertDialog, message: String) {
    if (!loader.isShowing()) {
        loader.setTitle(message)
        loader.show()
    }
}

fun hideLoader(loader: AlertDialog) {
    if (loader.isShowing())
        loader.dismiss()
}

fun serviceUnavailableMessage(activity: FragmentActivity) {
    Toast.makeText(activity,
        "Stock Service Unavailable. Try again later",
        Toast.LENGTH_LONG
    ).show()
}

fun serviceAvailableMessage(activity: FragmentActivity) {
    Toast.makeText(activity,
        "Stock Contacted Successfully",
        Toast.LENGTH_LONG
    ).show()
}

