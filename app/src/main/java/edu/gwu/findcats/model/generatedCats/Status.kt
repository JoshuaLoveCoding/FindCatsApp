package edu.gwu.findcats.model.generatedCats


import com.squareup.moshi.Json


data class Status(

	@field:Json(name="code")
	val code: Code? = null,

	@field:Json(name="message")
	val message: Message? = null,

	@field:Json(name="$\t")
	val T: String? = null
)