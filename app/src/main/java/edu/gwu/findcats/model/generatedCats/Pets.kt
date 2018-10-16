package edu.gwu.findcats.model.generatedCats


import com.squareup.moshi.Json


data class Pets(

	@field:Json(name="pet")
	val pet: List<PetItem?>? = null
)