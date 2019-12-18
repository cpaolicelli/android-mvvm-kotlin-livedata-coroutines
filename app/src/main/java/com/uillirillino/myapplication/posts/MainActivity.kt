package com.uillirillino.myapplication.posts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uillirillino.myapplication.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}
