package edu.gwu.findcats.model.generatedFacts

import com.squareup.moshi.Json

data class FactsResponse(

	@field:Json(name="fact")
	val fact: String? = null,

	@field:Json(name="length")
	val length: Int? = null
)