package com.cpjandmatcomps.travelmantics.AdminActivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cpjandmatcomps.travelmantics.MainActivity
import com.cpjandmatcomps.travelmantics.R
import com.cpjandmatcomps.travelmantics.TravelDeal
import com.firebase.ui.auth.AuthUI
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_admin_dash_board.*

class AdminDashBoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dash_board)
        checkDeals()
        checkClicks()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate = MenuInflater(this)
        inflate.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sign_out->{
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener{
                        Toast.makeText(this, "Thank you for checking travels deals, Call Again", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }

    }

    fun view_deals(view: View){
        val intent = Intent(this, AdminDealsActivity::class.java)
        startActivity(intent)
    }

    val db = FirebaseDatabase.getInstance()

    private fun checkDeals(){
        val ref = db.reference.child("Travel Deals")
        val deals: MutableList<TravelDeal> = ArrayList()
        val listener = object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val deal= dataSnapshot.getValue(TravelDeal::class.java)
                if (deal != null){
                    deals.add(deal)
                    tvTotalDeals.text = deals.size.toString()
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val deal= dataSnapshot.getValue(TravelDeal::class.java)
                if (deal != null){
                    deals.add(deal)
                    tvTotalDeals.text = deals.size.toString()
                }
            }

        }
        ref.addChildEventListener(listener)
    }

    private fun checkClicks(){
        val clickref = db.reference.child("Clicked Deals")
        val clicks = arrayListOf<String>()
        val listener = object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val click= dataSnapshot.value.toString()
                if (click.isNotEmpty()){
                    clicks.add(click)
                    tvTotalViews.text = clicks.size.toString()
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val click= dataSnapshot.getValue().toString()
                if (click.isNotEmpty()){
                    clicks.add(click)
                    tvTotalViews.text = clicks.size.toString()
                }
            }

        }
        clickref.addChildEventListener(listener)
    }

}