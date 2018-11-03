package edu.gwu.findcats.model.generatedCats


import com.squareup.moshi.Json


data class Contact(

		@Json(name = "email") val email: StringWrapper,
		@Json(name = "zip") val zip: StringWrapper
)