package edu.gwu.findcats.model

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.TextView
import edu.gwu.findcats.model.generatedFacts.FactsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

class FactsManager(private val context: Context, private val textView: TextView) {

    private val TAG = "FactsManager"
    var factsSearchCompletionListener: FactsSearchCompletionListener? = null

    interface FactsSearchCompletionListener {
        fun factsLoaded(cFact: String)
        fun factsNotLoaded()
    }

    interface ApiEndpointInterface {
        @GET("fact")
        fun getFactsResponse(): Call<FactsResponse>
    }

    fun searchFacts() {
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.CATFACTS_SEARCH_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val apiEndpoint = retrofit.create(ApiEndpointInterface::class.java)

        apiEndpoint.getFactsResponse().enqueue(object : Callback<FactsResponse> {
            override fun onFailure(call: Call<FactsResponse>, t: Throwable) {
                Log.d(TAG, "API call failed!")
                factsSearchCompletionListener?.factsNotLoaded()
            }

            override fun onResponse(call: Call<FactsResponse>, response: Response<FactsResponse>) {
                val factsResponseBody = response.body()

                if (factsResponseBody != null) {
                    val fact = factsResponseBody.fact

                    if (fact != null) {
                        factsSearchCompletionListener?.factsLoaded(fact)
                        return
                    }

                } else {
                    factsSearchCompletionListener?.factsNotLoaded()
                }
            }
        })
    }


}


