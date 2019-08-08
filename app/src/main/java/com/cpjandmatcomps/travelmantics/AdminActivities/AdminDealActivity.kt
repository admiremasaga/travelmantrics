package com.cpjandmatcomps.travelmantics.AdminActivities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cpjandmatcomps.travelmantics.R
import com.google.firebase.database.FirebaseDatabase
import com.cpjandmatcomps.travelmantics.TravelDeal
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_admin_deal.*

class AdminDealActivity : AppCompatActivity() {
    val RC_UPLOAD_IMG = 538
    var imgUrl = ""
    var deal = TravelDeal()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_deal)
        if(intent.hasExtra("deal")){
            if(intent.getSerializableExtra("deal") != null){
                deal = intent.getSerializableExtra("deal") as TravelDeal
                etDealTitle.setText(deal.title)
                edDealPrice.setText(deal.price)
                edDealDescription.setText(deal.description)
                if (deal.imageUrl != null)
                    showImg(deal.imageUrl!!)
            }

        }
    }

    fun upload_img(view: View){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/jpeg")
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RC_UPLOAD_IMG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_UPLOAD_IMG && resultCode == RESULT_OK){

            if (data?.data != null) {
                val uploaded_img = data.data.toString()
                showImg(uploaded_img).toString()
            }
            val img_uri = data?.data
            val db = FirebaseStorage.getInstance()
            val myref = db.reference.child("Travel Deals Images")
            val storageRef = myref.child(img_uri!!.lastPathSegment)
            storageRef.putFile(img_uri).addOnSuccessListener{
                imgUrl = storageRef.downloadUrl.toString()
                val picname = it.storage.path
                deal.imageName = picname
                showImg(imgUrl)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.admin_deal_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.save_deal ->{
                if(etDealTitle.text!!.isNotEmpty() && edDealDescription.text!!.isNotEmpty() && edDealPrice.text!!.isNotEmpty()) {
                    saveDeal()
                    backtoList()
                }
                else
                    Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.delete_deal ->{
                deleteDeal()
                backtoList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val db = FirebaseDatabase.getInstance()
    private val myref = db.reference.child("Travel Deals")

    private fun saveDeal(){
        deal.title = etDealTitle.text.toString()
        deal.price = edDealPrice.text.toString()
        deal.description = edDealDescription.text.toString()

        if (dealexists()){
            myref.push().setValue(deal)
        }
        else{
            myref.child(deal.id!!).setValue(deal)
        }
    }

    private fun deleteDeal(){
        if(dealexists()){
           myref.child(deal.id!!).removeValue()
            if (deal.imageName != null && deal.imageName!!.isNotEmpty()){
                val picref = myref.child(deal.imageName!!)
                picref.removeValue().addOnSuccessListener {
                    Toast.makeText(this, deal.imageName +"has been successfully deleted", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this, "Failed to delete "+deal.imageName+ " because "+ it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun dealexists(): Boolean{
        return deal.id != null
    }

    fun backtoList(){
        val intent = Intent(this, AdminDashBoardActivity::class.java)
        startActivity(intent)
    }

    fun showImg(url: String){
        if(url.isNotEmpty()){
            val width = Resources.getSystem().displayMetrics.widthPixels
            Picasso.get()
                .load(url)
                .resize(width, width*2/3)
                .centerCrop()
                .into(ivDealImg)
        }
    }
}