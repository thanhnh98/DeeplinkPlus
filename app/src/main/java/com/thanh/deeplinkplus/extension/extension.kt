package com.thanh.deeplinkplus.extension

import android.app.Activity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.thanh.deeplinkplus.common.resources.Resources

fun Activity.showMessage(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun View.onClick(f: () -> Unit){
    setOnClickListener {
        f.invoke()
    }
}

fun EditText.text(): String{
    return text.toString()
}

fun String.isSafe():Boolean{
    return !isNullOrEmpty() && !isNullOrBlank()
}