package com.cpjandmatcomps.travelmantics
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cpjandmatcomps.travelmantics.AdminActivities.AdminDealActivity
import com.cpjandmatcomps.travelmantics.R
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dealrow.view.*

class DealAdapter(): RecyclerView.Adapter<DealAdapter.DealViewHolder>() {
    val deals: MutableList<TravelDeal> = ArrayList()
    init{
        val db = FirebaseDatabase.getInstance()
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
                    deal.id = dataSnapshot.key
                    deals.add(deal)
                    notifyItemInserted(deals.size - 1)
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val deal= dataSnapshot.getValue(TravelDeal::class.java)
                if (deal != null){
                    deal.id = dataSnapshot.key
                    deals.add(deal)
                    notifyItemRemoved(deals.size - 1)
                }
            }
        }
        val ref = db.reference.child("Travel Deals")
        ref.addChildEventListener(listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): DealViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.dealrow, parent, false)
        return DealViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return deals.size
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        val deal = deals.get(position)
        holder.bind(deal)
    }

    inner class DealViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val pos = adapterPosition
            val clickedDeal = deals[pos]
            recordClick(clickedDeal.id)
            val intent = Intent(v?.context, DealActivity::class.java)
            intent.putExtra("deal", clickedDeal)
            v?.context?.startActivity(intent)
        }

        fun recordClick(id: String?){
            val recorddb = FirebaseDatabase.getInstance()
            val recordref = recorddb.reference.child("Clicked Deals")
            recordref.push().setValue(id)
        }

        val dealtitle = itemView.tvDealTitle
        val dealdescription = itemView.tvDealDescription
        val dealprice = itemView.tvPrice
        val imgThumb = itemView.ivDeal_thumb

        fun bind(deal: TravelDeal){
            dealtitle.text = deal.title
            dealdescription.text = deal.description
            dealprice.text = deal.price
            showThumbImg(deal.imageUrl)
        }

        fun showThumbImg(imgUrl: String?){
            if (imgUrl != null && !imgUrl.isEmpty()){
                Picasso.get()
                    .load(imgUrl)
                    .resize(160, 160)
                    .centerCrop()
                    .into(imgThumb)
            }
        }
    }
}