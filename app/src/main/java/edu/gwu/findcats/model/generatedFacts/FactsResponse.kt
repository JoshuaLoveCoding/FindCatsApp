package edu.gwu.findcats.model.generatedFacts

import com.squareup.moshi.Json

data class FactsResponse(

	@Json(name="fact")
	val fact: String? = null,

	@Json(name="length")
	val length: Int? = null
)