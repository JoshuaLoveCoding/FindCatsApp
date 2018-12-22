package edu.gwu.findcats

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import kotlinx.android.synthetic.main.activity_favourite.*

class FavouriteActivity : AppCompatActivity(),PetsAdapter.OnPetClickListener {
    private lateinit var persistenceManager: PersistenceManager//initiate PersistenceManager to keep the data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        setSupportActionBar(favorite_toolbar)

        val myfToolbar = findViewById(R.id.favorite_toolbar) as Toolbar
        setSupportActionBar(myfToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myfToolbar.setNavigationOnClickListener({ view -> onBackPressed() })//show scrollable bar
    }

    override fun onResume() {
        super.onResume()
        persistenceManager = PersistenceManager(this)
        val pets = persistenceManager.fetchPets()//fetch favorites list
        populatePetList(pets)
    }

    private fun populatePetList(pets : List<Pet>) {
        petsRecyclerView_2.adapter = PetsAdapter(pets, this)//show data
    }

    override fun onPetClick(pet: Pet, petView: View) {
        val detailsIntent = Intent(this, PetDetailsActivity::class.java)
        detailsIntent.putExtra(getString(R.string.bundle_extra_pet), pet)
        startActivity(detailsIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return true
    }

}