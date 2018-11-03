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
    private var item: Item? = null
    var imageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_details)
        setSupportActionBar(menu_toolbar)
        item = intent.getParcelableExtra(getString(R.string.bundle_extra_item))
        imageView = findViewById(R.id.catImage)
        populateDetails(item)
        persistenceManager = PersistenceManager(this)

        val mToolbar = findViewById(R.id.menu_toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mToolbar.setNavigationOnClickListener({ view -> onBackPressed() })

    }


    private fun populateDetails(item: Item?) {
        var url: String? = item?.imageUri
        Picasso.with(this).load(url).into(imageView)
        nametextView.text = getString(R.string.hint_catname, item?.name)
        genderTextView.text = getString(R.string.hint_gender, item?.gender)
        breedTextView.text = getString(R.string.hint_breed, item?.breed)
        zipTextView.text = getString(R.string.hint_zip, item?.zip)
        detailsTextView.text = item?.details
        detailsTextView.setMovementMethod(ScrollingMovementMethod())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        return true
    }
    fun shareButtonPressed(baritem: MenuItem) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        val shareText = getString(R.string.share_message, item?.name, item?.email, item?.name, item?.imageUri)
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.share)))
    }
    fun emailButtonPressed(emailitem: MenuItem) {
        // Create the Intent
        val emailIntent = Intent()
        emailIntent.action = Intent.ACTION_SEND

        // Fill it with Data
        val title = getString(R.string.title_message, item?.name)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, item?.email)
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title)

        // Send it off to the Activity-Chooser
        startActivity(Intent.createChooser(emailIntent, resources.getText(R.string.email)))
    }

    fun favoriteButtonPressed(favitem: MenuItem) {
        val items = persistenceManager.fetchItems().toMutableList()
        var index = 0
        val len = items.size
        for (a in items) {
            if (a.id == item?.id) {
                persistenceManager.deleteItem(index)
                toast(R.string.delete)
                break
            }
            index++
        }
        val items2 = persistenceManager.fetchItems().toMutableList()
        val len2 = items2.size
        if (len2 == len) {
            persistenceManager.saveItem(item)
            toast(R.string.save)
        }
    }


}



