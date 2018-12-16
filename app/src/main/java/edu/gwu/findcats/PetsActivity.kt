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
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_face.view.*
import java.util.*

class PetsActivity : AppCompatActivity(), PetsAdapter.OnPetClickListener, PetSearchManager.PetSearchCompletionListener, LocationDetector.LocationListener {

    private lateinit var petSearchManager: PetSearchManager
    private lateinit var persistenceManager: PersistenceManager
    private val LOCATION_PERMISSION_REQUEST_CODE = 7
    private lateinit var locationDetector: LocationDetector
    var zip1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets)
        setSupportActionBar(zip_toolbar)
        persistenceManager = PersistenceManager(this)

        petSearchManager = PetSearchManager()
        petSearchManager.petSearchCompletionListener = this

        locationDetector = LocationDetector(this)
        locationDetector.locationListener = this

        zip1 = persistenceManager.fetchZip()

        requestPermissionsIfNecessary()

        val myToolbar = findViewById(R.id.zip_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myToolbar.setNavigationOnClickListener({ view -> onBackPressed() })
    }

    private fun requestPermissionsIfNecessary() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        if(permission == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            populatePetList(zip1)
            toast(R.string.permissions_denied)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if(grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                locationDetector.detectLocation()
            }
        }
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

    override fun locationFound(location: Location) {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())
        addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val postalCode = addresses[0].getPostalCode()
        zip1 = postalCode
        populatePetList(zip1)
    }

    override fun locationNotFound(reason: LocationDetector.FailureReason) {
        populatePetList(zip1)
        when (reason) {
            LocationDetector.FailureReason.TIMEOUT -> toast(getString(R.string.location_not_found))
            LocationDetector.FailureReason.NO_PERMISSION -> toast(getString(R.string.no_location_permission))
        }
    }

}