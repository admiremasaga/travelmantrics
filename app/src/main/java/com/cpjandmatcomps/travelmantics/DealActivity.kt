package com.cpjandmatcomps.travelmantics

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_deal.*

class DealActivity : AppCompatActivity() {
    var deal = TravelDeal()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)

        if (intent.hasExtra("deal")) {
            if (intent.getSerializableExtra("deal") != null) {
                deal = intent.getSerializableExtra("deal") as TravelDeal
                title = deal.title
                tvUserDealTitle.text = deal.title
                tvUserDealPrice.text = deal.price
                tvUserDealDescription.text = deal.description
                if (deal.imageUrl != null)
                    showImg(deal.imageUrl!!)
            }
        }
    }
    fun showImg(url: String){
        if(url.isNotEmpty()){
            val width = Resources.getSystem().displayMetrics.widthPixels
            Picasso.get()
                .load(url)
                .resize(width, width*2/3)
                .centerCrop()
                .into(ivUserDealImg)
        }
    }
}