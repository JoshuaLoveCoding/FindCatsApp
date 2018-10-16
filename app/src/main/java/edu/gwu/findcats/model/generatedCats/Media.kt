package edu.gwu.findcats.model.generatedCats


import com.squareup.moshi.Json


data class Media(

	@field:Json(name="photos")
	val photos: Photos? = null
)