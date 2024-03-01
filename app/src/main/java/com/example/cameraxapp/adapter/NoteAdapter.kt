package com.example.cameraxapp.adapter

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.RecyclerView
import com.example.cameraxapp.HomeFragment
import com.example.cameraxapp.R
import com.example.cameraxapp.databinding.DocsRecyclerViewBinding
import com.example.cameraxapp.db.NoteEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


interface OnItemClickListener {
    fun onItemClick(note: NoteEntity)
    fun onItemFavoriteClick(note: NoteEntity)

    fun onShareContent(content:String)
    fun onItemClick(
        note: NoteEntity,
        fileUriString: String
    ) // Updated to use NoteEntity as the data model class
}


class NoteAdapter(private val listener: HomeFragment) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    val arrayList = ArrayList<NoteEntity>()
    val selectList = ArrayList<NoteEntity>()
    private lateinit var binding: DocsRecyclerViewBinding
    private lateinit var context: Context
    var isEnable = false
    var isSelectAll = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DocsRecyclerViewBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: NoteAdapter.ViewHolder, position: Int) {
        val note = differ.currentList[position]
        holder.bind(note)
        holder.setIsRecyclable(false)

    }

    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            cursor?.let {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                it.moveToFirst()
                filePath = it.getString(columnIndex)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return filePath
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(
        val binding: DocsRecyclerViewBinding,
        private val listener: HomeFragment
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val note = differ.currentList[position]
                    val fileUriString = note.fileUri
                    listener.onItemClick(note, fileUriString)
                }
            }

//            binding.imgShare.setOnClickListener {
//                val position = adapterPosition
//                if (position!=RecyclerView.NO_POSITION){
//                    val note = differ.currentList[position]
//                    listener.onShareContent(note.fileUri)
//                }
//            }

            binding.imgFavourites.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val note = differ.currentList[position]
                    // Toggle the favorite status
                    note.isFavorite = !note.isFavorite
                    // Update the star icon
                    binding.imgFavourites.setImageResource(
                        if (note.isFavorite) R.drawable.ic_star_filled else R.drawable.img_star
                    )
                    // Notify the listener (e.g., fragment or activity) to handle the database update
                    listener.onItemFavoriteClick(note)
                }
            }
        }


        fun bind(item: NoteEntity) {
            binding.apply {
                txtStudentFullName.text = item.noteTitle

//                bus.setImageURI(item.fileUri.toUri())

                val timeStamp = item.timeStamp
                val date = Date(timeStamp)
                val format = SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.getDefault())
                val dateString = format.format(date)

                txtTimeStamp.text = dateString

                imgFavourites.setImageResource(
                    if (item.isFavorite) R.drawable.ic_star_filled else R.drawable.img_star
                )

            }
        }
    }


    private val differCallback = object : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem.noteId == newItem.noteId
        }

        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}
