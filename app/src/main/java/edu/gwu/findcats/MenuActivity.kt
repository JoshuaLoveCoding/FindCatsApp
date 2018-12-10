package edu.gwu.findcats

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.location.Address
import android.view.View
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import edu.gwu.findcats.model.FactsManager
import kotlinx.android.synthetic.main.activity_menu.*
import org.jetbrains.anko.toast
import android.location.Location
import android.location.Geocoder
import android.support.v7.app.AlertDialog
import java.util.*

class MenuActivity : AppCompatActivity(), FactsManager.FactsSearchCompletionListener, LocationDetector.LocationListener {
    private lateinit var factsManager: FactsManager
    private val LOCATION_PERMISSION_REQUEST_CODE = 7
    private lateinit var locationDetector: LocationDetector
    private lateinit var persistenceManager: PersistenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        factsManager = FactsManager(this,factTextView)
        factsManager.factsSearchCompletionListener = this
        persistenceManager = PersistenceManager(this)
        locationDetector = LocationDetector(this)
        locationDetector.locationListener = this
        factsManager.searchFacts()
        requestPermissionsIfNecessary()
    }

    private fun requestPermissionsIfNecessary() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if(grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                toast(R.string.permissions_granted)
                locationDetector.detectLocation()
                showInfo()
            }
            else {
                toast(R.string.permissions_denied)
            }
        }
    }

    fun showInfo() {
        val dialogTitle = getString(R.string.about_title)
        val dialogMessage = getString(R.string.about_message)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }

    fun addClicked(view: View) {
        val intent = Intent(this, PetsActivity::class.java)
        startActivity(intent)
    }

    fun addClickedFav(view: View) {
        val intent2 = Intent(this, FavouriteActivity::class.java)
        startActivity(intent2)
    }


    override fun factsNotLoaded() {
        toast(R.string.factNotLoaded)
    }

    override fun locationFound(location: Location) {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())
        addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val postalCode = addresses[0].getPostalCode()
        persistenceManager.saveZip(postalCode)
    }

    override fun locationNotFound(reason: LocationDetector.FailureReason) {
        when (reason) {
            LocationDetector.FailureReason.TIMEOUT -> toast(getString(R.string.location_not_found))
            LocationDetector.FailureReason.NO_PERMISSION -> toast(getString(R.string.no_location_permission))
        }
    }
}