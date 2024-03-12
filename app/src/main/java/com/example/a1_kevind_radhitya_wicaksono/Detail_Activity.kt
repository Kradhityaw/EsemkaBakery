package com.example.a1_kevind_radhitya_wicaksono

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a1_kevind_radhitya_wicaksono.databinding.ActivityDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

class Detail_Activity : AppCompatActivity() {
    lateinit var bind : ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.detail2Toolbar.setNavigationOnClickListener {
            startActivity(Intent(this@Detail_Activity, Search_Activity::class.java))
            finish()
        }

        var id = intent.getStringExtra("id")

        GlobalScope.launch(Dispatchers.IO) {
            val connDetails = URL("http://10.0.2.2:5000/api/Cake/$id").openStream().bufferedReader().readText()
            val jsonsDetail = JSONObject(connDetails)

            runOnUiThread {
                bind.detail2Desc.text = jsonsDetail.getString("description")
                bind.detail2Harga.text = "$${jsonsDetail.getString("price")}"
                bind.detail2Nama.text = jsonsDetail.getString("name")
            }

            GlobalScope.launch(Dispatchers.IO) {
                val check = BitmapFactory.decodeStream(URL("${jsonsDetail.getString("imageURL")}").openStream())
                runOnUiThread {
                    bind.detail2Gambar.setImageBitmap(check)
                }
            }
        }
    }
}