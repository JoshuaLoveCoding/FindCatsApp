package edu.gwu.findcats

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    fun addClicked(view: View) {
        val intent = Intent(this, PetsActivity::class.java)
        startActivity(intent)
    }

    fun addClickedFav(view: View) {
        val intent2 = Intent(this, FavouriteActivity::class.java)
        startActivity(intent2)
    }
}
