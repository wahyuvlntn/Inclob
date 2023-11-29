package com.example.inclob.data

import com.google.gson.annotations.SerializedName

data class Provinsi(

	@field:SerializedName("Provinsi")
	val provinsi: List<ProvinsiItem?>? = null
)

data class ProvinsiItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
