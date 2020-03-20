package com.example.univerlabs_new_1

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

fun Int.notDef (def : Int ,f : () -> Unit) {
    if(this != def) f()
}

class MainActivity : AppCompatActivity() {

    private val COLOR_KEY = "com.example.univerlabs_new_1.MainActivity.COLOR_KEY"
    private val STOMB_COLOR = -1
    private var selected_color = STOMB_COLOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val color = savedInstanceState.getInt(COLOR_KEY, STOMB_COLOR)
        color.notDef(STOMB_COLOR) {
            selected_color = color
            layoutid.setBackgroundColor(color)
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        selected_color.notDef(STOMB_COLOR) {
            outState.putInt(COLOR_KEY, selected_color)
        }
        super.onSaveInstanceState(outState)
    }
    
    fun btnRedHandler(v : View) {
        layoutid.setBackgroundColor(Color.RED)
        selected_color = Color.RED
    }

    fun btnYelHandler(v: View) {
        layoutid.setBackgroundColor(Color.YELLOW)
        selected_color = Color.YELLOW
    }

    fun btnGreenHandler(v: View) {
        layoutid.setBackgroundColor(Color.GREEN)
        selected_color = Color.GREEN
    }
}
