package edu.gwu.findcats.model.generatedCats


import com.squareup.moshi.Json


data class Petfinder(@Json(name = "pets") val pets: Pets?)