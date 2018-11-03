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

    fun saveItem(item: Item?) {

        val items = fetchItems().toMutableList()
        val item = Item(item?.imageUri, item?.name, item?.gender, item?.zip, item?.details, item?.id, item?.email, item?.breed)

        items.add(item)

        val editor = sharedPreferences.edit()

        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, Item::class.java)
        val jsonAdapter = moshi.adapter<List<Item>>(listType)
        val jsonString = jsonAdapter.toJson(items)

        editor.putString(Constants.ITEM_PREF_KEY, jsonString)

        editor.apply()

    }

    fun fetchItems(): List<Item> {

        val jsonString = sharedPreferences.getString(Constants.ITEM_PREF_KEY, null)

        //if null, this means no previous scores, so create an empty array list
        if(jsonString == null) {
            return arrayListOf<Item>()
        }
        else {
            //existing scores, so convert the scores JSON string into Score objects, using Moshi
            val listType = Types.newParameterizedType(List::class.java, Item::class.java)
            val moshi = Moshi.Builder()
                    .build()
            val jsonAdapter = moshi.adapter<List<Item>>(listType)

            var items:List<Item>? = emptyList<Item>()
            try {
                items = jsonAdapter.fromJson(jsonString)
            } catch (e: IOException) {
                Log.e(ContentValues.TAG, e.message)
            }

            if(items != null) {
                return items
            }
            else {
                return emptyList<Item>()
            }
        }
    }

    fun fetchZip(): String? {

        val zip = sharedPreferences.getString(Constants.ZIP_PREF_KEY, null)

        return zip

    }

    fun deleteItem(index: Int) {

        val items = fetchItems().toMutableList()

        items.removeAt(index)

        val editor = sharedPreferences.edit()

        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, Item::class.java)
        val jsonAdapter = moshi.adapter<List<Item>>(listType)
        val jsonString = jsonAdapter.toJson(items)

        editor.putString(Constants.ITEM_PREF_KEY, jsonString)

        editor.apply()

    }

}


