package com.cpjandmatcomps.travelmantics

import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import java.io.Serializable

class TravelDeal: Serializable {

    var id: String? = null
    var title: String? = null
    var description: String? = null
    var price: String? = null
    var imageUrl: String? = null
    var imageName: String? = null

    constructor() {}

    constructor(title: String, description: String, price: String, imageUrl: String, imageName: String) {
        this.id = id
        this.title = title
        this.description = description
        this.price = price
        this.imageUrl = imageUrl
        this.imageName = imageName
    }
}