package com.example.univerlabs_new_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startHandler(v : View) {
       val lighter = supportFragmentManager.findFragmentById(R.id.traficLighter)
       if (lighter != null) if(lighter.isInLayout) (lighter as TraficLighter).runLighter()
    }
}
