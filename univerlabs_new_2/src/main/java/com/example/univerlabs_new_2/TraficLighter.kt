package com.example.univerlabs_new_2

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_trafic_lighter.*
import java.util.concurrent.TimeUnit
import kotlin.Exception

enum class Colors {
    YELLOW { override val drawable = R.drawable.yellow },
    GREEN { override val drawable = R.drawable.green },
    RED { override val drawable = R.drawable.red },
    OFF { override val drawable = R.drawable.off_light };

    abstract val drawable: Int
}

class TraficLighter : Fragment() {

    private var stateColor = Colors.OFF

    private var task : LightAlg? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        updateColor(stateColor)
        super.onActivityCreated(savedInstanceState)
    }


    fun runLighter () {
        try {
            task?.let { it.cancel(true) }
            task = LightAlg (this)
            task?.let { it.execute (* arrayOf (txtRed, txtYellow, txtGreen)
                    .map { t -> t.getSec().matchPositive () }
                    .toTypedArray () ) }
        } catch (e : Exception) {
            Toast.makeText(context, "error: " + e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trafic_lighter, container, false)
    }

    private fun TextView.getSec(): Long {
        val str = this.text.toString()
        if(str.isNotEmpty()) {
            return str.toLong()
        } else {
            throw Exception("Type number in empty field, please")
        }
    }

    private fun Long.matchPositive(): Long {
        if(this > 0) {
            return this
        } else {
            throw Exception("type value above zero, please")
        }
    }

    fun updateColor(color : Colors) {
        stateColor = color
        imageView.setImageResource(stateColor.drawable)
    }

}


class LightAlg(private val activity : TraficLighter) : AsyncTask<Long, Colors, Void>() {

    override fun doInBackground(vararg seconds: Long?): Void? {
        val (secRed, secYellow, secGreen) = seconds

        if(secRed == null || secYellow == null || secGreen == null)
            throw Exception("fun doInBackground have a nullPtr exception at seconds vararg")

        blink()
        light(secRed, Colors.RED)
        blink()
        light(secYellow, Colors.YELLOW)
        blink()
        light(secGreen, Colors.GREEN)
        blink()


        return null
    }

    private fun blink(color: Colors = Colors.YELLOW, period : Long = 500) {
        for (i in 1 .. 3) {
            publishProgress(color)
            TimeUnit.MILLISECONDS.sleep(period)
            publishProgress(Colors.OFF)
            TimeUnit.MILLISECONDS.sleep(period)
        }
    }

    private fun light(sec : Long, color : Colors) {
        publishProgress(color)
        TimeUnit.SECONDS.sleep(sec)
    }

    override fun onProgressUpdate(vararg values: Colors) {
        activity.updateColor(values[0])
        super.onProgressUpdate(*values)
    }
}