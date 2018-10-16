package edu.gwu.findcats.model.generatedCats

import com.squareup.moshi.Json

data class Petfinder(

	@Json(name="pets")
	val pets: Pets? = null,

	@Json(name="@xmlns:xsi")
	val xmlnsXsi: String? = null,

	@Json(name="@xsi:noNamespaceSchemaLocation")
	val xsiNoNamespaceSchemaLocation: String? = null,

	@Json(name="lastOffset")
	val lastOffset: LastOffset? = null,

	@Json(name="header")
	val header: Header? = null
)