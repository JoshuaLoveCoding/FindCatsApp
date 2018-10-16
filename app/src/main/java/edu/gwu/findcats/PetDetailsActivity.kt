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

class PetDetailsActivity : AppCompatActivity() {
    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_details)
        item = DataProvider.catList[0]
        populateDetails(item)
    }


    private fun populateDetails(item: Item?) {
        Picasso.get().load(item?.imageUri).into(catImage, object: com.squareup.picasso.Callback {
            override fun onSuccess() {
                item
            }

            override fun onError(e: java.lang.Exception?) {

            }
        })
        nametextView.text = "Name: " + item?.name
        genderTextView.text = "Gender: " + item?.gender
        breedTextView.text = "Breed: " + item?.breed
        zipTextView.text = "Zip: " + item?.zip
        detailsTextView.text = item?.details
    }
}


