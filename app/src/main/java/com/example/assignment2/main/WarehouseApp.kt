package com.example.assignment2.main

import android.app.Application
import android.net.Uri
import com.example.assignment2.models.WarehouseModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class WarehouseApp : Application(), AnkoLogger {

    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference



    override fun onCreate() {
        super.onCreate()
        info("Donation App started")

    }
}

