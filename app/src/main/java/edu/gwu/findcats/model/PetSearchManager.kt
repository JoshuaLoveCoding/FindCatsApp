package edu.gwu.findcats.model

import edu.gwu.findcats.Pet
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import edu.gwu.findcats.model.generatedCats.PetFinderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.reflect.Type
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class PetSearchManager {

    private val TAG = "PetSearchManager"
    var petSearchCompletionListener: PetSearchCompletionListener? = null

    interface PetSearchCompletionListener {
        fun petsLoaded(cats: List<Pet>)
        fun petsNotLoaded()
    }

    interface ApiEndpointInterface {
        @GET("pet.find")
        fun findPets(@Query("key") key: String, @Query("format") format: String, @Query("animal") animal: String, @Query("location") location: String): Call<PetFinderResponse>
    }//add all parameters needed in the url

    fun searchPets(query: String) {
        val moshi = Moshi.Builder()
                .add(ObjectAsListJsonAdapterFactory())
                .add(KotlinJsonAdapterFactory())
                .build()
        var catsList = mutableListOf<Pet>()//initiate the result list
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.CATS_SEARCH_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

        val apiEndpoint = retrofit.create(ApiEndpointInterface::class.java)

        apiEndpoint.findPets("***", "json", "cat", query).enqueue(object: Callback<PetFinderResponse> {
            override fun onFailure(call: Call<PetFinderResponse>, t: Throwable) {
                Log.d(TAG, "API call failed!")
                petSearchCompletionListener?.petsNotLoaded()
            }

            override fun onResponse(call: Call<PetFinderResponse>, response: Response<PetFinderResponse>) {
                val petsResponseBody = response.body()

                petsResponseBody?.petfinder?.pets?.pet?.let {
                    for (pet in it) {
                        catsList.add(
                                Pet(imageUri = pet.media.photos.photo[2].t,
                                        name = pet.name.t,
                                        gender = pet.sex.t,
                                        zip = pet.contact.zip.t,
                                        details = pet.description.t,
                                        id = pet.id.t,
                                        email = pet.contact.email.t,
                                        breed = pet.breeds.breed[0].t
                                )
                        )
                    }
                    petSearchCompletionListener?.petsLoaded(catsList)
                    return
                }

                petSearchCompletionListener?.petsNotLoaded()

            }
        })
    }
    /**
     *
     * The adapter Wraps an encountered Objects in a List whenever a List<T> was expected but an
     * Object was encountered in the JSON
     */

    class ObjectAsListJsonAdapterFactory : JsonAdapter.Factory {
        override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
            if (!List::class.java.isAssignableFrom(Types.getRawType(type))) {
                return null
            }
            val listDelegate = moshi.nextAdapter<List<Any>>(this, type, annotations)
            val innerType = Types.collectionElementType(type, List::class.java)
            val objectDelegate = moshi.adapter<Any>(innerType, annotations)
            return ListJsonAdapter(listDelegate, objectDelegate) as JsonAdapter<Any>
        }

        inner class ListJsonAdapter<T>(private val listDelegate: JsonAdapter<List<T>>,
                                       private val objectDelegate: JsonAdapter<T>) : JsonAdapter<List<T>>() {

            override fun fromJson(reader: JsonReader): List<T>? {
                if (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
                    objectDelegate.fromJson(reader)?.let { return arrayListOf(it) } ?: return null
                } else {
                    return listDelegate.fromJson(reader)
                }
            }

            override fun toJson(writer: JsonWriter, value: List<T>?) {
                listDelegate.toJson(writer, value)
            }
        }
    }
}