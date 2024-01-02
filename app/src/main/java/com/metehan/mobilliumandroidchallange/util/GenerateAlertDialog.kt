package com.metehan.mobilliumandroidchallange.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.metehan.mobilliumandroidchallange.R
import java.util.Timer
import java.util.TimerTask

object GenerateAlertDialog {
    fun showAlertDialogFragment(context: Context, imageSource: Int, message: String, delayTime: Int){
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.alert_dialog_view, null)
        builder.setView(view)
        val alertImage = view.findViewById<ImageView>(R.id.alertImage)
        alertImage.setImageResource(imageSource)
        val alertText = view.findViewById<TextView>(R.id.alertText)
        alertText.text = message
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                alertDialog.dismiss()
                timer.cancel()
            }
        }, delayTime.toLong())
    }
}