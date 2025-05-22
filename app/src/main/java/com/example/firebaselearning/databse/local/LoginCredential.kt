
package com.example.firebaselearning.databse.local

import android.content.Context

fun saveCredentials(context: Context, email: String, password: String) {
    val sharedPref = context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString("email", email)
        putString("password", password)
        apply()
    }
}



