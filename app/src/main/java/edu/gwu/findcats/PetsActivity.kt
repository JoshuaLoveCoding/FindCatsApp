package edu.gwu.findcats

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import edu.gwu.findcats.model.PetSearchManager
import kotlinx.android.synthetic.main.activity_pets.*
import android.view.Menu
import android.view.MenuItem
import org.jetbrains.anko.toast
import android.app.AlertDialog
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_face.view.*

class PetsActivity : AppCompatActivity(), PetsAdapter.OnPetClickListener, PetSearchManager.PetSearchCompletionListener {
    private lateinit var petSearchManager: PetSearchManager
    private lateinit var persistenceManager: PersistenceManager
    var zip1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets)
        setSupportActionBar(zip_toolbar)
        persistenceManager = PersistenceManager(this)

        petSearchManager = PetSearchManager()
        petSearchManager.petSearchCompletionListener = this

        zip1 = persistenceManager.fetchZip()

        populatePetList(zip1)

        val myToolbar = findViewById(R.id.zip_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myToolbar.setNavigationOnClickListener({ view -> onBackPressed() })
    }


    private fun populatePetList(zip1: String?) {
        if (zip1 == null) {
            petSearchManager.searchPets("22202")
            showInfo()
        } else {
            petSearchManager.searchPets(zip1)
        }
    }

    override fun onPetClick(pet: Pet, petView: View) {
        val detailsIntent = Intent(this, PetDetailsActivity::class.java)
        detailsIntent.putExtra(getString(R.string.bundle_extra_pet), pet)
        startActivity(detailsIntent)
    }

    override fun petsNotLoaded() {
        toast(R.string.petsNotLoaded)
    }

    override fun petsLoaded(catsList: List<Pet>) {
        if (catsList.isNotEmpty()) {
            petsRecyclerView.adapter = PetsAdapter(catsList, this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.zip_menu, menu)
        return true
    }

    fun showInfo() {
        val dialogTitle = getString(R.string.about_title)
        val dialogMessage = getString(R.string.location_not_found)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }


    fun zipButtonPressed(barpet: MenuItem) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_face, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.btnOk.setOnClickListener {
            mAlertDialog.dismiss()
            val zip = mDialogView.editText.text.toString()
            petSearchManager.searchPets(zip)
            persistenceManager.saveZip(zip)
        }

        mDialogView.btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }

    }

}