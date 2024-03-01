package com.example.cameraxapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FolderAdapter(private val folderList: List<Folder>) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_recycler_view, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = folderList[position]
        // Bind data to views in ViewHolder
        holder.bind(folder)
    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize views
        private val folderNameTextView: TextView = itemView.findViewById(R.id.folderNameTextView)
        private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        private val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)

        fun bind(folder: Folder) {
            // Bind data to views
            folderNameTextView.text = folder.folderName
            emailTextView.text = folder.email
            numberTextView.text = folder.number.toString()
            // Set onClickListener for any views if needed
        }
    }
}
