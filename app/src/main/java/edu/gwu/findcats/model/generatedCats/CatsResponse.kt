package edu.gwu.findcats.model.generatedCats


import com.squareup.moshi.Json


data class CatsResponse(

	@field:Json(name="petfinder")
	val petfinder: Petfinder? = null,

	@field:Json(name="@version")
	val version: String? = null,

	@field:Json(name="@encoding")
	val encoding: String? = null
)