package edu.gwu.findcats.model.generatedCats


import com.squareup.moshi.Json


data class Photos(

	@field:Json(name="photo")
	val photo: List<PhotoItem?>? = null
)