package edu.gwu.findcats

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import edu.gwu.findcats.model.PetSearchManager
import kotlinx.android.synthetic.main.activity_pets.*
import android.view.Menu
import android.view.MenuItem


class PetsActivity : AppCompatActivity(), ItemsAdapter.OnItemClickListener, PetSearchManager.PetSearchCompletionListener {
    private lateinit var petSearchManager: PetSearchManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets)
        setSupportActionBar(zip_toolbar)

        petSearchManager = PetSearchManager()
        petSearchManager.petSearchCompletionListener = this

        populateItemList()
    }

    private fun populateItemList() {
        petSearchManager.searchPets(22202)
    }

    override fun onItemClick(item: Item, itemView: View) {
        val detailsIntent = Intent(this, PetDetailsActivity::class.java)
        detailsIntent.putExtra(getString(R.string.bundle_extra_item), item)
        startActivity(detailsIntent)
    }

    override fun petsNotLoaded() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun petsLoaded(catsList: List<Item>) {
        if (catsList.isNotEmpty()) {
            itemsRecyclerView.layoutManager = LinearLayoutManager(this)
            itemsRecyclerView.adapter = ItemsAdapter(catsList, this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.zip_menu, menu)
        return true
    }
    fun shareButtonPressed(baritem: MenuItem) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.share)))
    }

}