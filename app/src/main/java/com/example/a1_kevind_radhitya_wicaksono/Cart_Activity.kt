package com.example.a1_kevind_radhitya_wicaksono

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1_kevind_radhitya_wicaksono.databinding.ActivityCartBinding
import com.example.a1_kevind_radhitya_wicaksono.databinding.CardCartBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.URL

class Cart_Activity : AppCompatActivity() {
    lateinit var bind : ActivityCartBinding
    lateinit var numberEt : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityCartBinding.inflate(layoutInflater)
        setContentView(bind.root)

        GlobalScope.launch(Dispatchers.IO) {
            val conn = URL("http://10.0.2.2:5000/api/Cake").openStream().bufferedReader().readText()
            val jsons = JSONArray(conn)

            runOnUiThread {
                val adapter = object : RecyclerView.Adapter<CardHolder>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
                        val inflate = CardCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                        return CardHolder(inflate)
                    }

                    override fun getItemCount(): Int {
                        return jsons.length()
                    }

                    override fun onBindViewHolder(holder: CardHolder, position: Int) {
                        val data = jsons.getJSONObject(position)
                        holder.binding.cartName.text = data.getString("name")
                        holder.binding.cartPrice.text = "$${data.getString("price")}"

                        numberEt = holder.binding.cartQty

                        holder.binding.cartIncrement.setOnClickListener {
                            holder.binding.cartQty.setText((holder.binding.cartQty.text.toString().toInt() + 1).toString())

                            val total = holder.binding.cartQty.text.toString().toDouble() * data.getDouble("price")

                            if (data.getString("name") == holder.binding.cartName.text) {
                                bind.cartTotalPrice.setText("0")
                                var sum = bind.cartTotalPrice.text.toString().toDouble() + total
                                bind.cartTotalPrice.setText(sum.toString())
                            }
                        }

                        holder.binding.cartDecrement.setOnClickListener {
                            if (holder.binding.cartQty.text.toString().toInt() > 0) {
                                holder.binding.cartQty.setText((holder.binding.cartQty.text.toString().toInt() - 1).toString())
                                val total = holder.binding.cartQty.text.toString().toDouble() * data.getDouble("price")
                                try {
                                    bind.cartTotalPrice.setText(total.toString().substring(0,4))
                                }
                                catch (e : Exception) {
                                    bind.cartTotalPrice.setText("0")
                                }
                            }
                        }

                        GlobalScope.launch(Dispatchers.IO) {
                            val image = BitmapFactory.decodeStream(URL("${data.getString("imageURL")}").openStream())

                            runOnUiThread {
                                holder.binding.cartImage.setImageBitmap(image)
                            }
                        }
                    }
                }
                bind.cartRv.adapter = adapter
                bind.cartRv.layoutManager = LinearLayoutManager(this@Cart_Activity)
            }
        }
    }

    class CardHolder(val binding: CardCartBinding) : RecyclerView.ViewHolder(binding.root)

//    fun Increment() {
//        val currentValue = numberEt.text.toString().toInt()
//        numberEt.setText((currentValue + 1).toString())
//    }
//
//    fun Decrement() {
//        val currentValue = numberEt.text.toString().toInt()
//        if (currentValue > 0) {
//            numberEt.setText((currentValue - 1).toString())
//        }
//    }
}