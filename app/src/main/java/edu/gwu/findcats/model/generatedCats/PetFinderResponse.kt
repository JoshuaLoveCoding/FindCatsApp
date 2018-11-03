package edu.gwu.findcats.model.generatedCats


import com.squareup.moshi.Json


data class PetFinderResponse(@Json(name = "petfinder") val petfinder: Petfinder)