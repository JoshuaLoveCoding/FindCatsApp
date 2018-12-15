package edu.gwu.findcats

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Pet(
        var imageUri: String?,
        var name: String?,
        var gender: String?,
        var zip: String?,
        var details: String?,
        var id: String?,
        var email: String?,
        var breed: String?
) : Parcelable
