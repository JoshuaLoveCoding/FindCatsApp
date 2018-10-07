package edu.gwu.findcats

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_pets.*
import edu.gwu.findcats.R
import edu.gwu.findcats.Item
import edu.gwu.findcats.ItemsAdapter
import edu.gwu.findcats.DataProvider
import kotlinx.android.synthetic.main.activity_menu.*


class PetsActivity : AppCompatActivity(),ItemsAdapter.OnItemClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets)
        populateItemList()
    }

    private fun populateItemList() {
        val items = DataProvider.catList
        if (items.isNotEmpty()) {
            itemsRecyclerView.adapter = ItemsAdapter(items, this)
        }
    }

    override fun onItemClick(item: Item, itemView: View) {
        val detailsIntent = Intent(this, PetDetailsActivity::class.java)
        startActivity(detailsIntent)
    }

}
