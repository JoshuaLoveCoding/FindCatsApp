package edu.gwu.findcats

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Item(
        var imageId: Int,
        var name: String,
        var gender: String,
        var breed: String,
        var zip: String,
        var details: String
) : Parcelable
