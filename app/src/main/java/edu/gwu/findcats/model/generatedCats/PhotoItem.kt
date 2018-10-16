package edu.gwu.findcats.model.generatedCats

import com.squareup.moshi.Json

data class PhotoItem(

	@Json(name="t")
	val T: String? = null,

	@Json(name="@size")
	val size: String? = null,

	@Json(name="@id")
	val id: String? = null
)