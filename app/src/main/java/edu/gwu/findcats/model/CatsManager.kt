package edu.gwu.findcats.model

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Picasso
import edu.gwu.findcats.DataProvider.Companion.catList
import edu.gwu.findcats.Item
import edu.gwu.findcats.R
import edu.gwu.findcats.model.generatedCats.CatsResponse
import edu.gwu.findcats.model.generatedCats.PetItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class CatsManager(private val context: Context) {

    private val TAG = "CatsManager"
    var catsSearchCompletionListener: CatsSearchCompletionListener? = null

    interface CatsSearchCompletionListener {
        fun catsNotLoaded()
        fun catsLoaded(cats: List<Item>)
    }

    interface ApiEndpointInterface {
        @GET("pet.find")
        fun getCatsResponse(@Query("key") key: String, @Query("format") format: String, @Query("animal") animal: String, @Query("location") location: String): Call<CatsResponse>
    }

    fun searchCats(query: String) {
        var catsList = mutableListOf<Item>()
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.CATS_SEARCH_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val apiEndpoint = retrofit.create(ApiEndpointInterface::class.java)

        apiEndpoint.getCatsResponse("b24cc971e2505ef5196d5bf9af6c3802", "json", "cat", query).enqueue(object : Callback<CatsResponse> {
            override fun onFailure(call: Call<CatsResponse>, t: Throwable) {
                Log.d(TAG, "API call failed!")
                catsSearchCompletionListener?.catsNotLoaded()
            }

            override fun onResponse(call: Call<CatsResponse>, response: Response<CatsResponse>) {
                val orientation = context.resources.configuration.orientation
                val catsResponseBody = response.body()

                if (catsResponseBody != null) {
                    val cats = catsResponseBody.petfinder?.pets?.pet
                    if (cats != null) {
                        for (item in cats) {
                            val photos = item?.media?.photos?.photo
                            val names = item?.name
                            val sexs = item?.sex
                            val breeds = item?.breeds?.breed
                            val zips = item?.contact?.zip
                            val descriptions = item?.description
                            val ids = item?.id
                            if (photos != null && names != null && sexs != null && breeds != null && zips != null && descriptions != null && ids != null) {
                                val uri = photos[2]?.T
                                val name = names?.T
                                val sex = sexs?.T
                                val breed = breeds?.T
                                val zip = zips?.T
                                val description = descriptions?.T
                                val id = ids?.T
                                if (uri != null && name != null && sex != null && breed != null && zip != null && description != null && id != null) {
                                    catsList.add(
                                            Item(imageUri = uri,
                                                    name = name,
                                                    gender = sex,
                                                    breed = breed,
                                                    zip = zip,
                                                    details = description,
                                                    id = id
                                            )
                                    )
                                }

                            }

                        }

                        catsSearchCompletionListener?.catsLoaded(catsList)

                    } else {
                        catsSearchCompletionListener?.catsNotLoaded()
                    }

                } else {
                    catsSearchCompletionListener?.catsNotLoaded()
                }
            }
        })
    }
}