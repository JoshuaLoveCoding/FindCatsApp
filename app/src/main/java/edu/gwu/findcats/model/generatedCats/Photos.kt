package edu.gwu.findcats.model.generatedCats


import com.squareup.moshi.Json


data class Photos(@Json(name = "photo") val photo: List<PhotoPet>)