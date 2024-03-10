package com.example.a1_kevind_radhitya_wicaksono

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1_kevind_radhitya_wicaksono.databinding.ActivitySearchBinding
import com.example.a1_kevind_radhitya_wicaksono.databinding.ItemCard1Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class Search_Activity : AppCompatActivity() {
    lateinit var bind : ActivitySearchBinding
    lateinit var cari : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.searchToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@Search_Activity, Home_Activity::class.java))
            finish()
        }

        cari = bind.searchPage.text.toString()

        bind.searchButton.setOnClickListener {
            cari = bind.searchPage.text.toString()
            GlobalScope.launch(Dispatchers.IO) {
                val conn = URL("http://10.0.2.2:5000/api/Cake?search=$cari").openStream().bufferedReader().readText()
                val data = JSONArray(conn)

                runOnUiThread {
                    val adapter = object : RecyclerView.Adapter<HorizontalRV>() {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): HorizontalRV {
                            val infalate = ItemCard1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
                            return HorizontalRV(infalate)
                        }

                        override fun onBindViewHolder(holder: HorizontalRV, position: Int) {
                            val sample = data.getJSONObject(position)
                            holder.binding.cakeName.text = sample.getString("name")

                            holder.itemView.setOnClickListener {
                                startActivity(Intent(this@Search_Activity, Detail_Activity::class.java).apply {
                                    putExtra("id", sample.getString("cakeID"))
                                })
                                finish()
                            }

                            GlobalScope.launch(Dispatchers.IO) {
                                val check = BitmapFactory.decodeStream(URL("${sample.getString("imageURL")}").openStream())
                                runOnUiThread {
                                    holder.binding.cakeImage.setImageBitmap(check)
                                }

                            }
                        }

                        override fun getItemCount(): Int {
                            return data.length()
                        }
                    }
                    bind.searchRv.adapter = adapter
                    bind.searchRv.layoutManager = GridLayoutManager(this@Search_Activity, 2)
                }
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            val conn = URL("http://10.0.2.2:5000/api/Cake?search=$cari").openStream().bufferedReader().readText()
            val data = JSONArray(conn)

            runOnUiThread {
                val adapter = object : RecyclerView.Adapter<HorizontalRV>() {
                    override fun onCreateViewHolder(
                        parent: ViewGroup,
                        viewType: Int
                    ): HorizontalRV {
                        val infalate = ItemCard1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
                        return HorizontalRV(infalate)
                    }

                    override fun onBindViewHolder(holder: HorizontalRV, position: Int) {
                        val sample = data.getJSONObject(position)
                        holder.binding.cakeName.text = sample.getString("name")

                        holder.itemView.setOnClickListener {
                            startActivity(Intent(this@Search_Activity, Detail_Activity::class.java).apply {
                                putExtra("id", sample.getString("cakeID"))
                            })
                            finish()
                        }

                        GlobalScope.launch(Dispatchers.IO) {
                            val check = BitmapFactory.decodeStream(URL("${sample.getString("imageURL")}").openStream())
                            runOnUiThread {
                                holder.binding.cakeImage.setImageBitmap(check)
                            }

                        }
                    }

                    override fun getItemCount(): Int {
                        return data.length()
                    }
                }
                bind.searchRv.adapter = adapter
                bind.searchRv.layoutManager = GridLayoutManager(this@Search_Activity, 2)
            }
        }
    }
    class HorizontalRV(val binding : ItemCard1Binding) : RecyclerView.ViewHolder(binding.root)
}