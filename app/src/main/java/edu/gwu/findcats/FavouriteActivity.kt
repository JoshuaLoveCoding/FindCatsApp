package edu.gwu.findcats

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import kotlinx.android.synthetic.main.activity_favourite.*

class FavouriteActivity : AppCompatActivity(),ItemsAdapter.OnItemClickListener {
    private lateinit var persistenceManager: PersistenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        setSupportActionBar(favorite_toolbar)

        val myfToolbar = findViewById(R.id.favorite_toolbar) as Toolbar
        setSupportActionBar(myfToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myfToolbar.setNavigationOnClickListener({ view -> onBackPressed() })
    }

    override fun onResume() {
        super.onResume()
        persistenceManager = PersistenceManager(this)
        val items = persistenceManager.fetchItems()
        populateItemList(items)
    }

    private fun populateItemList(items : List<Item>) {
        itemsRecyclerView_2.adapter = ItemsAdapter(items, this)
    }

    override fun onItemClick(item: Item, itemView: View) {
        val detailsIntent = Intent(this, PetDetailsActivity::class.java)
        detailsIntent.putExtra(getString(R.string.bundle_extra_item), item)
        startActivity(detailsIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return true
    }

}