package com.cpjandmatcomps.travelmantics.AdminActivities

import android.content.Intent
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cpjandmatcomps.travelmantics.R
import com.cpjandmatcomps.travelmantics.TravelDeal
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_admin_deals.*

class AdminDealsActivity : AppCompatActivity() {
    private lateinit var adminDealAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var db: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    val deals: MutableList<TravelDeal> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_deals)
        viewManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adminDealAdapter = AdminDealAdapter()
        rvAdminDeals.adapter = adminDealAdapter
        rvAdminDeals.layoutManager = viewManager
    }

    fun add_deal(view: View){
        val intent = Intent(this, AdminDealActivity::class.java)
        startActivity(intent)
    }
}