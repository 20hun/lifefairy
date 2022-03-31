package com.danny.lifefairy.mainrv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.danny.lifefairy.R
import com.danny.lifefairy.form.SpaceId

class RVMainAdapter(val items : MutableList<SpaceId>) : RecyclerView.Adapter<RVMainAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVMainAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mianrv_item, parent, false)

        return ViewHolder(view)
    }

    interface ItemClick {
        fun onClick(view : View, position: Int)
    }
    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: RVMainAdapter.ViewHolder, position: Int) {

        if(itemClick != null) {
            holder.itemView.setOnClickListener { v->
                itemClick?.onClick(v, position)
            }
        }

        holder.bindItems(items[position].name, items[position].emoji)
    }

    // 전체 리사이클러뷰의 개수
    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(name : String, emoji : String) {
            val rv_name = itemView.findViewById<TextView>(R.id.rvSpaceTextName)
            val rv_emoji = itemView.findViewById<TextView>(R.id.rvSpaceTextEmoji)
            rv_name.text = name
            rv_emoji.text = emoji

        }
    }
}