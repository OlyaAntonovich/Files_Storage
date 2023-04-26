package com.example.files_storage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.files_storage.data.Item
import com.example.files_storage.databinding.ItemBinding
import com.example.files_storage.R.drawable
import com.example.files_storage.service.WriteOpenFile

class Adapter(
    private val listItems: MutableList<Item>,
    private val context: Context
) : RecyclerView.Adapter<Adapter.ItemViewHolder>() {

    inner class ItemViewHolder(
        private val binding: ItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {

            val imAv = when (item.name.first()) {
                in 'a'..'c' -> drawable.ic_boat_fish
                in 'd'..'f' -> drawable.ic_sea_horse
                in 'g'..'j' -> drawable.ic_many_fish
                in 'k'..'m' -> drawable.ic_three_whales
                in 'n'..'p' -> drawable.ic_turtle
                in 'q'..'s' -> drawable.ic_two_fish
                in 't'..'w' -> drawable.ic_whale_birds
                else -> drawable.ic_fish_boat
            }

            binding.textName.text = item.name
            binding.textSurname.text = item.surname
            binding.textPhone.text = item.phone
            binding.textAge.text = item.age
            binding.textDob.text = item.dob
            binding.imageAvatar.setImageResource(imAv)

            binding.btnDelete.setOnClickListener {
                val position = adapterPosition
                val list = WriteOpenFile(context).fileToTakeOut()
                list.removeAt(position)
                notifyItemRemoved(position)
                WriteOpenFile(context).fileWriteTo(list)
                setNewList(list)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemBinding.inflate(layoutInflater, parent, false)

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = listItems[position]
        holder.bind(item)
    }

    fun setNewList(newList: MutableList<Item>) {

        listItems.clear()
        listItems.addAll(newList)

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listItems.size

}
