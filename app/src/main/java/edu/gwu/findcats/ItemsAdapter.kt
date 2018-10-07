package edu.gwu.findcats

import android.content.ClipData
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.gwu.findcats.R
import edu.gwu.findcats.Item
import kotlinx.android.synthetic.main.layout_list_item.view.*

// 1
class ItemsAdapter(private val items: List<Item>, private val clickListener: OnItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 2
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Item, listener: OnItemClickListener) = with(itemView) {
            itemName.text = item.name
            itemImage.setImageResource(item.imageId)
            setOnClickListener {
                listener.onItemClick(item, it)
            }
        }
    }

    // 3
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.layout_list_item, parent, false)
        return ViewHolder(view)
    }

    // 4
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position], clickListener)
    }

    // 5
    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClickListener {
        fun onItemClick(item: Item, itemView: View)
    }
}