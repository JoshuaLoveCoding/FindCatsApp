package edu.gwu.findcats.model.generatedCats

import com.squareup.moshi.Json

data class Breeds(

	@Json(name="breed")
	val breed: Breed? = null
)