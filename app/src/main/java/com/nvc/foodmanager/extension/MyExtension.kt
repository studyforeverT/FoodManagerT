package com.nvc.foodmanager.extension

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.nvc.foodmanager.R

@BindingAdapter("set_image_url")
fun setImage(img : ImageView, link : String){
    Glide.with(img.context)
        .load(link)
        .placeholder(R.drawable.ic_baseline_image_24)
        .into(img)
}

@BindingAdapter("set_bg_by_status")
fun setBackgroundByStatus(cardView: CardView, status: Int) {
    cardView.setCardBackgroundColor(
        cardView.resources.getColor(
            when (status) {
                0 -> {
                    android.R.color.holo_red_dark
                }
                1 -> {
                    android.R.color.holo_green_dark
                }
                else -> {
                    R.color.yellow
                }
            }
        )
    )
}

@BindingAdapter("set_bg_by_position")
fun setBackgroundByPosition(cardView : CardView, position : Int){
    cardView.setCardBackgroundColor(
        cardView.resources.getColor(if(position%2==1) R.color.orange else R.color.bg_cart)
    )
}

@BindingAdapter("set_text_color_by_position")
fun setTextColorByPosition(textView: TextView, position : Int){
    textView.setTextColor(
        textView.resources.getColor(if(position%2==1) R.color.white else R.color.black)
    )
}

