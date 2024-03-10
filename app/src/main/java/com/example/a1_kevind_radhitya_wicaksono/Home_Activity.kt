package com.example.a1_kevind_radhitya_wicaksono

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1_kevind_radhitya_wicaksono.databinding.ActivityHomeBinding
import com.example.a1_kevind_radhitya_wicaksono.databinding.ItemCard1Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Home_Activity : AppCompatActivity() {
    lateinit var bind : ActivityHomeBinding
    var id = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.homeToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@Home_Activity, Search_Activity::class.java))
        }

        GlobalScope.launch(Dispatchers.IO) {
            val conn = URL("http://10.0.2.2:5000/api/Cake").openStream().bufferedReader().readText()
            val data = JSONArray(conn)

            val connDetails = URL("http://10.0.2.2:5000/api/Cake/$id").openStream().bufferedReader().readText()
            val jsonsDetail = JSONObject(connDetails)

            GlobalScope.launch(Dispatchers.IO) {
                val check = BitmapFactory.decodeStream(URL("${jsonsDetail.getString("imageURL")}").openStream())
                runOnUiThread {
                    bind.detailImage.setImageBitmap(check)
                }
            }

            runOnUiThread {

                bind.detailName.text = jsonsDetail.getString("name")
                bind.detailPrice.text = "$${jsonsDetail.getString("price")}"
                bind.detailDesc.text = jsonsDetail.getString("description")

                val adapter = object : RecyclerView.Adapter<HorizontalRV>() {
                    override fun onCreateViewHolder(
                        parent: ViewGroup,
                        viewType: Int
                    ): HorizontalRV {
                        val infalate = ItemCard1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
                        return HorizontalRV(infalate)
                    }

                    override fun getItemCount(): Int {
                        return data.length()
                    }

                    override fun onBindViewHolder(holder: HorizontalRV, position: Int) {
                        val sample = data.getJSONObject(position)
                        holder.binding.cakeName.text = sample.getString("name")

                        holder.itemView.setOnClickListener {
                            id = sample.getInt("cakeID")
                            Log.d("oke", id.toString())
                        }

                        GlobalScope.launch(Dispatchers.IO) {
                            val check = BitmapFactory.decodeStream(URL("${sample.getString("imageURL")}").openStream())
                            runOnUiThread {
                                holder.binding.cakeImage.setImageBitmap(check)
                            }

                        }
                    }
                }
                bind.homeRv.adapter = adapter
                bind.homeRv.layoutManager = LinearLayoutManager(this@Home_Activity, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    class HorizontalRV(val binding : ItemCard1Binding) : RecyclerView.ViewHolder(binding.root)
}