package edu.gwu.findcats

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pet_details.*
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.support.v7.widget.Toolbar
import android.text.method.ScrollingMovementMethod
import org.jetbrains.anko.toast

class PetDetailsActivity : AppCompatActivity() {
    private lateinit var persistenceManager: PersistenceManager
    private var pet: Pet? = null
    var imageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_details)
        setSupportActionBar(menu_toolbar)
        pet = intent.getParcelableExtra(getString(R.string.bundle_extra_pet))
        imageView = findViewById(R.id.catImage)
        populateDetails(pet)
        persistenceManager = PersistenceManager(this)

        val mToolbar = findViewById(R.id.menu_toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mToolbar.setNavigationOnClickListener({ view -> onBackPressed() })

    }


    private fun populateDetails(pet: Pet?) {
        var url: String? = pet?.imageUri
        Picasso.with(this).load(url).into(imageView)
        nametextView.text = getString(R.string.hint_catname, pet?.name)
        genderTextView.text = getString(R.string.hint_gender, pet?.gender)
        breedTextView.text = getString(R.string.hint_breed, pet?.breed)
        zipTextView.text = getString(R.string.hint_zip, pet?.zip)
        detailsTextView.text = pet?.details
        detailsTextView.setMovementMethod(ScrollingMovementMethod())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        return true
    }
    fun shareButtonPressed(barpet: MenuItem) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        val shareText = getString(R.string.share_message, pet?.name, pet?.email, pet?.name, pet?.imageUri)
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.share)))
    }
    fun emailButtonPressed(emailpet: MenuItem) {
        // Create the Intent
        val emailIntent = Intent()
        emailIntent.action = Intent.ACTION_SEND

        // Fill it with Data
        val title = getString(R.string.title_message, pet?.name)
        emailIntent.type = "text/plain"
        val aEmailList = arrayOf(pet?.email)
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList)
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title)

        // Send it off to the Activity-Chooser
        startActivity(Intent.createChooser(emailIntent, resources.getText(R.string.email)))
    }

    fun favoriteButtonPressed(favpet: MenuItem) {
        val pets = persistenceManager.fetchPets().toMutableList()
        var index = 0
        val len = pets.size
        for (a in pets) {
            if (a.id == pet?.id) {
                persistenceManager.deletePet(index)
                toast(R.string.delete)
                break
            }
            index++
        }
        val pets2 = persistenceManager.fetchPets().toMutableList()
        val len2 = pets2.size
        if (len2 == len) {
            persistenceManager.savePet(pet)
            toast(R.string.save)
        }
    }


}



