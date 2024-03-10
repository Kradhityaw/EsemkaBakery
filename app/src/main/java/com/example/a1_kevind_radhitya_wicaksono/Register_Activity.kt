package com.example.a1_kevind_radhitya_wicaksono

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.a1_kevind_radhitya_wicaksono.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Register_Activity : AppCompatActivity() {
    lateinit var bind : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.registerSignin.setOnClickListener {
            startActivity(Intent(this@Register_Activity, MainActivity::class.java))
            finish()
        }

        bind.registerButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val conn = URL("http://10.0.2.2:5000/api/Auth/Register").openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")

                val jsons = JSONObject().apply {
                    put("passwordConfirmation", bind.registerConfirmpass.text)
                    put("password", bind.registerPassword.text)
                    put("email", bind.registerEmail.text)
                    put("username", bind.registerUsername.text)
                    put("firstName", bind.registerFirstname.text)
                    put("lastName", bind.registerLastname.text)
                }

                conn.outputStream.write(jsons.toString().toByteArray())

                if (conn.responseCode in 200..299) {
                    startActivity(Intent(this@Register_Activity, MainActivity::class.java))
                    finish()
                }
                else {
                    val error = conn.errorStream.bufferedReader().readText()
                    runOnUiThread {
                        try {
                            Toast.makeText(this@Register_Activity, "${JSONObject(error).getString("message")}", Toast.LENGTH_SHORT).show()
                        }
                        catch (e : Exception) {
                            Toast.makeText(this@Register_Activity, "Pastikan Semua Data Terisi!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}