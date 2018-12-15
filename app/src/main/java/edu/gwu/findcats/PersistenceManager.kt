package edu.gwu.findcats

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import edu.gwu.findcats.model.Constants
import java.io.IOException

class PersistenceManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun saveZip(zip: String?) {

        if (zip != null) {

            val editor = sharedPreferences.edit()

            editor.putString(Constants.ZIP_PREF_KEY, zip)

            editor.apply()
        }

    }

    fun savePet(pet: Pet?) {

        val pets = fetchPets().toMutableList()
        val pet = Pet(pet?.imageUri, pet?.name, pet?.gender, pet?.zip, pet?.details, pet?.id, pet?.email, pet?.breed)

        pets.add(pet)

        val editor = sharedPreferences.edit()

        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, Pet::class.java)
        val jsonAdapter = moshi.adapter<List<Pet>>(listType)
        val jsonString = jsonAdapter.toJson(pets)

        editor.putString(Constants.PET_PREF_KEY, jsonString)

        editor.apply()

    }

    fun fetchPets(): List<Pet> {

        val jsonString = sharedPreferences.getString(Constants.PET_PREF_KEY, null)

        //if null, this means no previous scores, so create an empty array list
        if(jsonString == null) {
            return arrayListOf<Pet>()
        }
        else {
            //existing scores, so convert the scores JSON string into Score objects, using Moshi
            val listType = Types.newParameterizedType(List::class.java, Pet::class.java)
            val moshi = Moshi.Builder()
                    .build()
            val jsonAdapter = moshi.adapter<List<Pet>>(listType)

            var pets:List<Pet>? = emptyList<Pet>()
            try {
                pets = jsonAdapter.fromJson(jsonString)
            } catch (e: IOException) {
                Log.e(ContentValues.TAG, e.message)
            }

            if(pets != null) {
                return pets
            }
            else {
                return emptyList<Pet>()
            }
        }
    }

    fun fetchZip(): String? {

        val zip = sharedPreferences.getString(Constants.ZIP_PREF_KEY, null)

        return zip

    }

    fun deletePet(index: Int) {

        val pets = fetchPets().toMutableList()

        pets.removeAt(index)

        val editor = sharedPreferences.edit()

        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, Pet::class.java)
        val jsonAdapter = moshi.adapter<List<Pet>>(listType)
        val jsonString = jsonAdapter.toJson(pets)

        editor.putString(Constants.PET_PREF_KEY, jsonString)

        editor.apply()

    }

}


