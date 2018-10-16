package edu.gwu.findcats.model.generatedCats

import com.squareup.moshi.Json

data class CatsResponse(

	@Json(name="petfinder")
	val petfinder: Petfinder? = null,

	@Json(name="@version")
	val version: String? = null,

	@Json(name="@encoding")
	val encoding: String? = null
)