package com.example.lw1.List

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lw1.Item
import com.example.lw1.R
import com.example.lw1.databinding.NoteItemBinding

class NoteAdapter(val listener: Listener): RecyclerView.Adapter<NoteAdapter.NoteHolder>() {
    val NoteList = ArrayList<ItemList>()
    class NoteHolder(item: View): RecyclerView.ViewHolder(item) {

        val binding = NoteItemBinding.bind(item)
        fun bind(itmlst: ItemList, listener: Listener) = with(binding){
            tvHD.text = itmlst.head
            tvDat.text = itmlst.datetime
            IvType.setImageResource(itmlst.type)
            itemView.setOnClickListener{
                listener.onClick(itmlst)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteHolder(view)
    }

    override fun getItemCount(): Int {
        return NoteList.size
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(NoteList[position], listener)
    }

    fun addNote(note: ItemList){
        NoteList.add(note)
        notifyDataSetChanged()
    }

    interface Listener{
        fun onClick(note: ItemList)
    }
}