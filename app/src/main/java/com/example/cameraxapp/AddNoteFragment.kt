package com.example.cameraxapp

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.cameraxapp.databinding.FragmentAddNoteBinding
import com.example.cameraxapp.db.NoteEntity
import com.example.cameraxapp.viewmodel.DatabaseViewmodel
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.room.Room
import com.example.cameraxapp.db.NoteDao
import com.example.cameraxapp.db.NoteDatabase
import java.io.File

class AddNoteFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding
    private lateinit var database:NoteDatabase

    private val noteEntity by lazy { NoteEntity() }
    private val viewmodel: DatabaseViewmodel by inject()

    private var noteTitle = ""
    private var fileUri:Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(layoutInflater)
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.state = STATE_EXPANDED
        binding!!.apply {
            imgClose.setOnClickListener {
                dismiss()
            }

            uploadButton.setOnClickListener {
                showFilePicker()
            }

            btnSave.setOnClickListener {
                noteTitle = edtTitle.text.toString()


                if (noteTitle.isEmpty()) {
                    Snackbar.make(it, "Title and file Cannot be empty", Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    noteEntity.noteId = 0
                    noteEntity.noteTitle = noteTitle


                    val fileName = getFileName(fileUri)?: "unKnownFile"
                    noteEntity.fileUri = fileUri.toString()
                    noteEntity.fileName = fileName
                    viewmodel.saveNote(noteEntity)

                    edtTitle.setText("")


                }
                dismiss()


            }
        }
    }

    private fun getFileName(fileUri: Uri?): String? {
        fileUri ?: return null // If fileUri is null, return null
        var fileName: String? = null
        val contentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(fileUri, null, null, null, null)
        cursor?.use { c ->
            if (c.moveToFirst()) {
                fileName = c.getString(c.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return fileName
    }


    private fun showFilePicker() {

        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data

            data?.data?.let { uri ->
                fileUri = uri
            }
//
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
