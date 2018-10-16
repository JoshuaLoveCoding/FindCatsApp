package edu.gwu.findcats.model.generatedCats


import com.squareup.moshi.Json


data class Breeds(

	@field:Json(name="breed")
	val breed: Breed? = null
)