package edu.gwu.findcats

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import edu.gwu.findcats.R.id.factTextView
import edu.gwu.findcats.model.Constants.CATFACTS_SEARCH_URL
import edu.gwu.findcats.model.FactsManager
import kotlinx.android.synthetic.main.activity_menu.*
import org.jetbrains.anko.longToast
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.*

class MenuActivity : AppCompatActivity(), FactsManager.FactsSearchCompletionListener {
    private lateinit var factsManager: FactsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        factsManager = FactsManager(this,factTextView)
        factsManager.factsSearchCompletionListener = this
        factsManager.searchFacts()
    }



    fun addClicked(view: View) {
        val intent = Intent(this, PetsActivity::class.java)
        startActivity(intent)
    }

    fun addClickedFav(view: View) {
        val intent2 = Intent(this, FavouriteActivity::class.java)
        startActivity(intent2)
    }


    private fun nextTurn() {
        factsManager.searchFacts()
    }


    override fun factsNotLoaded() {
        nextTurn()
    }

}
