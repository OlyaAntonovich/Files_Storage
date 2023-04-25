package com.example.files_storage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.files_storage.data.Item
import com.example.files_storage.databinding.ItemBinding
import com.example.files_storage.R.drawable
import com.example.files_storage.fragments.FragmentFirst

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
                val list = takeListFromFile()
                list.removeAt(position)
                notifyItemRemoved(position)
                fileWriteTo(list)
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

    fun takeListFromFile(): MutableList<Item>{

        var listItems = mutableListOf<Item>()

        context.openFileInput(FragmentFirst.FILE_NAME).bufferedReader().useLines { lines ->

            lines.forEach {
                var list = it.split("/") as MutableList<String>

                val item = Item(
                    list[0],
                    list[1],
                    list[2],
                    list[3],
                    list[4]
                )
                listItems.add(item)
            }
        }
        return listItems

    }

    fun fileWriteTo(list: MutableList<Item>) {

        val filename = FragmentFirst.FILE_NAME

        val listRows = mutableListOf<String>()

        var row = ""

        list.forEach {
            row = (it.name + "/" + it.surname + "/" +
                    it.phone + "/" + it.age + "/" +
                    it.dob)
            listRows.add(row)
        }

        context.openFileOutput(filename, Context.MODE_PRIVATE).use {

            it.write(listRows.joinToString("\n").toByteArray())

        }

    }


}
