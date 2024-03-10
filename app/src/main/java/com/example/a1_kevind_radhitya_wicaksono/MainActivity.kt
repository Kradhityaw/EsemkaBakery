package com.example.a1_kevind_radhitya_wicaksono

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.a1_kevind_radhitya_wicaksono.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var bind : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.loginSignup.setOnClickListener {
            startActivity(Intent(this@MainActivity, Register_Activity::class.java))
            finish()
        }

        bind.loginUsername.setText("john_doe")
        bind.loginPassword.setText("password")

        bind.loginButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val conn = URL("http://10.0.2.2:5000/api/Auth/Login").openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")

                val jsons = JSONObject().apply {
                    put("usernameOrEmail", bind.loginUsername.text)
                    put("password", bind.loginPassword.text)
                }

                conn.outputStream.write(jsons.toString().toByteArray())

                if (conn.responseCode in 200..299) {
                    startActivity(Intent(this@MainActivity, Home_Activity::class.java))
                    finish()
                }
                else {
                    val error = conn.errorStream.bufferedReader().readText()
                    runOnUiThread {
                        try {
                            Toast.makeText(this@MainActivity, "${JSONObject(error).getString("message")}", Toast.LENGTH_SHORT).show()
                        }
                        catch (e : Exception) {
                            Toast.makeText(this@MainActivity, "Pastikan Semua Data Terisi!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}