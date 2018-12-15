package edu.gwu.findcats

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_list_pet.view.*

// 1
class PetsAdapter(private val pets: List<Pet>, private val clickListener: OnPetClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 2
    class ViewHolder(petView: View) : RecyclerView.ViewHolder(petView) {
        var imageView: ImageView? = null
        fun bind(pet: Pet, listener: OnPetClickListener) = with(itemView) {
            imageView = itemView.findViewById(R.id.petImage)
            var url: String? = pet?.imageUri
            Picasso.with(context).load(url).into(imageView)
            petName.text = pet.name
            setOnClickListener {
                listener.onPetClick(pet, it)
            }
        }
    }

    // 3
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.layout_list_pet, parent, false)
        return ViewHolder(view)
    }

    // 4
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(pets[position], clickListener)
    }

    // 5
    override fun getItemCount(): Int {
        return pets.size
    }

    interface OnPetClickListener {
        fun onPetClick(pet: Pet, petView: View)
    }
}

