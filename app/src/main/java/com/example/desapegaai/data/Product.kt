package com.example.desapegaai.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Product(
    @DocumentId
    val id: String? = null,
    val userId: String? = "",
    val imgUrl: String? = null,
    val name: String? = "",
    val description: String? = "",
    val value: Double = 0.0,
    val geohash: String? = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    @ServerTimestamp
    val createdAt: Date? = null,
    val inactivatedAt: Date? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        Date(parcel.readLong()),
        Date(parcel.readLong()),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userId)
        parcel.writeString(imgUrl)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeDouble(value)
        parcel.writeString(geohash)
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
        createdAt?.let { parcel.writeLong(it.time) }
        inactivatedAt?.let { parcel.writeLong(it.time) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
