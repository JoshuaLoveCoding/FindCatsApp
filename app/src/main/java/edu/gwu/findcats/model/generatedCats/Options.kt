package edu.gwu.findcats.model.generatedCats


import com.squareup.moshi.Json


data class Options(

	@field:Json(name="option")
	val option: List<OptionItem?>? = null
)