package edu.gwu.findcats

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import com.squareup.picasso.Picasso
import edu.gwu.findcats.R.id.titleTextView
import kotlinx.android.synthetic.main.activity_pet_details.*
import kotlinx.android.synthetic.main.layout_list_item.view.*
import android.view.Menu
import android.view.MenuItem

class PetDetailsActivity : AppCompatActivity() {
    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_details)
        setSupportActionBar(menu_toolbar)
        item = intent.getParcelableExtra(getString(R.string.bundle_extra_item))
        populateDetails(item)

    }


    private fun populateDetails(item: Item?) {
        Picasso.get().load(item?.imageUri).into(catImage)
        nametextView.text = "Name: " + item?.name
        genderTextView.text = "Gender: " + item?.gender

        zipTextView.text = "Zip: " + item?.zip
        detailsTextView.text = item?.details
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
        /* Create the Intent */
        val emailIntent = Intent()
        emailIntent.action = Intent.ACTION_SEND

        /* Fill it with Data */
        val title = getString(R.string.title_message, item?.name)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, item?.email)
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title)

        /* Send it off to the Activity-Chooser */
        startActivity(Intent.createChooser(emailIntent, resources.getText(R.string.email)))
    }
}


