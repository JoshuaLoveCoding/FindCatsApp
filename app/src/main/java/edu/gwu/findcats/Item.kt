package edu.gwu.findcats

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Item(
        var imageUri: String,
        var name: String,
        var gender: String,
        var breed: String,
        var zip: String,
        var details: String,
        var id: String
) : Parcelable
