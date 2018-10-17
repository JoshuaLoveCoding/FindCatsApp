package edu.gwu.findcats.model

import android.util.Log
import edu.gwu.findcats.Item
import edu.gwu.findcats.model.generatedCats.PetFinderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class PetSearchManager {

    private val TAG = "PetSearchManager"
    var petSearchCompletionListener: PetSearchCompletionListener? = null

    interface PetSearchCompletionListener {
        fun petsLoaded(cats: List<Item>)
        fun petsNotLoaded()
    }

    interface ApiEndpointInterface {
        @GET("pet.find")
        fun findPets(@Query("key") key: String, @Query("format") format: String, @Query("animal") animal: String, @Query("location") location: Int): Call<PetFinderResponse>
    }

    fun searchPets(query: Int) {
        var catsList = mutableListOf<Item>()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.petfinder.com")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val apiEndpoint = retrofit.create(ApiEndpointInterface::class.java)

        apiEndpoint.findPets("0b1452a0af588b352bf732936fa8219b", "json", "cat", query).enqueue(object: Callback<PetFinderResponse> {
            override fun onFailure(call: Call<PetFinderResponse>, t: Throwable) {
                Log.d(TAG, "API call failed!")
                petSearchCompletionListener?.petsNotLoaded()
            }

            override fun onResponse(call: Call<PetFinderResponse>, response: Response<PetFinderResponse>) {
                val petsResponseBody = response.body()

                if (petsResponseBody != null) {
                    val cats = petsResponseBody.petfinder?.pets?.pet
                    if (cats != null) {
                        for (item in cats) {
                            val photos = item?.media?.photos?.photo
                            val names = item?.name
                            val sexs = item?.sex
                            val zips = item?.contact?.zip
                            val descriptions = item?.description
                            val ids = item?.id
                            if (photos != null && names != null && sexs != null && zips != null && descriptions != null && ids != null) {
                                val uri = photos[2]?.T
                                val name = names?.T
                                val sex = sexs?.T
                                val zip = zips?.T
                                val description = descriptions?.T
                                val id = ids?.T
                                if (uri != null && name != null && sex != null && zip != null && description != null && id != null) {
                                    catsList.add(
                                            Item(imageUri = uri,
                                                    name = name,
                                                    gender = sex,
                                                    zip = zip,
                                                    details = description,
                                                    id = id
                                            )
                                    )
                                }

                            }

                        }

                        petSearchCompletionListener?.petsLoaded(catsList)
                        return
                    }

                    petSearchCompletionListener?.petsNotLoaded()
                }
            }
        })
    }
}