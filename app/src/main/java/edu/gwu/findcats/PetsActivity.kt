package edu.gwu.findcats

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_pets.*
import edu.gwu.findcats.R
import edu.gwu.findcats.Item
import edu.gwu.findcats.ItemsAdapter
import edu.gwu.findcats.DataProvider
import edu.gwu.findcats.model.CatsManager
import edu.gwu.findcats.model.FactsManager
import kotlinx.android.synthetic.main.activity_menu.*
import org.jetbrains.anko.toast


class PetsActivity : AppCompatActivity(), ItemsAdapter.OnItemClickListener, CatsManager.CatsSearchCompletionListener {
    private lateinit var catsManager: CatsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets)

        catsManager = CatsManager(this)
        catsManager.catsSearchCompletionListener = this

        populateItemList()
    }

    private fun populateItemList() {
        catsManager.searchCats("22202")
    }

    override fun onItemClick(item: Item, itemView: View) {
        val detailsIntent = Intent(this, PetDetailsActivity::class.java)
        startActivity(detailsIntent)
    }

    //API results:
    override fun catsNotLoaded() {
        //toast() //show some error to the user
    }

    override fun catsLoaded(catsList: List<Item>) {
        if (catsList.isNotEmpty()) {
            itemsRecyclerView.layoutManager = LinearLayoutManager(this)
            itemsRecyclerView.adapter = ItemsAdapter(catsList, this)
        }
    }

}
