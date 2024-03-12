package com.example.a1_kevind_radhitya_wicaksono

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.a1_kevind_radhitya_wicaksono.databinding.CardCartBinding

class Card_CartActivity : AppCompatActivity() {
    lateinit var bind : CardCartBinding
    lateinit var numberEt : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        bind = CardCartBinding.inflate(layoutInflater)
        setContentView(bind.root)


//        numberEt = bind.cartQty
//
//        bind.cartIncrement.setOnClickListener {
//            Increment()
//        }
//
//        bind.cartDecrement.setOnClickListener {
//            Decrement()
//        }

    }

    fun Increment() {
        val currentValue = numberEt.text.toString().toInt()
        numberEt.setText((currentValue + 1).toString())
    }

    fun Decrement() {
        val currentValue = numberEt.text.toString().toInt()
        if (currentValue > 0) {
            numberEt.setText((currentValue - 1).toString())
        }
    }
}