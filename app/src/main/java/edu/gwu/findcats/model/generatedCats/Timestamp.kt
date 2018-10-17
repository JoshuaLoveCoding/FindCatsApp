package edu.gwu.findcats.model.generatedCats


import com.squareup.moshi.Json


data class Timestamp(

	@field:Json(name="\$t")
	val T: String? = null
)