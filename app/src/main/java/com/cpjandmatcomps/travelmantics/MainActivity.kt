package com.cpjandmatcomps.travelmantics


import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cpjandmatcomps.travelmantics.AdminActivities.AdminDashBoardActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var dealAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    val RC_SIGN_IN = 57

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadDeals()
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null){
            signIn()
        }
        else{
            checkUser()
        }
    }

    fun signIn(){
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

// Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.ic_launcher)
                .build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                //val user = FirebaseAuth.getInstance().currentUser
                checkUser()
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, "Failed to Sign In because of ${response?.error?.errorCode}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadDeals()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate = MenuInflater(this)
        inflate.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sign_out->{
                val user = FirebaseAuth.getInstance().currentUser?.displayName
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener{
                        Toast.makeText(this, "Thank you $user for checking travels deals, Call Again", Toast.LENGTH_LONG).show()
                        signIn()
                        finish()
                    }
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }

    }

    fun checkUser(){
        val user = FirebaseAuth.getInstance().currentUser
        val type = user?.uid
        if (type == "eDIxY1lsT2Wr8WBkjm5DowIeO2O2"){
            startActivity(Intent(this, AdminDashBoardActivity::class.java))
        }
        else{
            loadDeals()
        }
    }

    fun loadDeals(){
        viewManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        dealAdapter = DealAdapter()
        rvDeals.adapter = dealAdapter
        rvDeals.layoutManager = viewManager
    }
}