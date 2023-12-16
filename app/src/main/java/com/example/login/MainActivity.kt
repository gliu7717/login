package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.login.activities.SignInActivity
import com.example.login.activities.SignUpActivity
import com.example.login.utils.MySharedPreference
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private val mySharedPreference: MySharedPreference = MySharedPreference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timer().schedule(1000) {
            val accessToken: String = mySharedPreference.getAccessToken(applicationContext)
            if (accessToken.isEmpty()) {
                startActivity(Intent(applicationContext, SignUpActivity::class.java))
            } else {
                startActivity(Intent(applicationContext, SignInActivity::class.java))
            }
            finish()
        }
    }

}