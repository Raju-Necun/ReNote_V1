package com.example.cameraxapp

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.cameraxapp.databinding.FragmentImportBinding
import com.example.cameraxapp.db.NoteEntity
import com.example.cameraxapp.viewmodel.DatabaseViewmodel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImportFragment : Fragment() {

    private var _binding: FragmentImportBinding? = null
    private val binding get() = _binding!!

    private val noteEntity by lazy { NoteEntity() }

    private val viewmodel: DatabaseViewmodel by inject()
    private var fileUri: Uri? = null
    private var noteTitle = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//      return inflater.inflate(R.layout.fragment_import, container, false)
        _binding = FragmentImportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            browse.setOnClickListener {
                showFilePicker()
            }

            btnFolder.setOnClickListener {
                val folderFragment = parentFragmentManager.beginTransaction()
                folderFragment.replace(R.id.frameLayout,foldersFragment())
                folderFragment.addToBackStack(null)
                folderFragment.commit()
            }

            btnSave.setOnClickListener {
                noteTitle = binding.etFileName.text.toString().trim { it <= ' ' }

                if (noteTitle.isEmpty()) {
                    Snackbar.make(it, "File name cannot be empty", Snackbar.LENGTH_SHORT).show()
                } else {
                    noteEntity.noteId = 0
                    noteEntity.noteTitle = noteTitle

                    val currentTimeMillis = System.currentTimeMillis()
                    val fileName = getFileName(fileUri) ?: "unKnownFile"
                    val fileUri:Uri = Uri.parse(fileUri.toString())
                    val fileType = getMimeType(requireContext(),fileUri)

                    noteEntity.fileUri = fileUri.toString()
                    noteEntity.fileName = fileName
                    noteEntity.timeStamp = currentTimeMillis
                    noteEntity.fileType = fileType.toString()

                    viewmodel.saveNote(noteEntity)

                    binding.etFileName.setText("")
                    Toast.makeText(
                        requireContext(), noteEntity.noteTitle + " saved", Toast.LENGTH_SHORT
                    ).show()
                    binding.imgSelected.setImageResource(0)
                }
            }
        }

    }

    private fun showFilePicker() {

        // Check for camera permission
        if (ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.CAMERA), 100
            )
            return
        }

        // Intent for picking a file from the gallery
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }

        val chooserIntent = Intent.createChooser(galleryIntent, "Select a file")

        // Intent for capturing an image from the camera
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fileUri = FileProvider.getUriForFile(
            requireContext(), "${requireContext().packageName}.provider", createImageFile()
        )
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        startActivityForResult(chooserIntent, 111)

//        startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
//            val uri: Uri? = data?.data
//
//            data?.data?.let { uri ->
//                fileUri = uri
//            }
////
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            // Handle result from both camera and gallery/file picker
            val uri: Uri? =
                data?.data ?: fileUri // Use fileUri if data is null, indicating camera usage

            uri?.let {
                // Here you can update your UI or handle the selected file URI
                fileUri = uri

                binding.apply {
                    imgSelected.setPadding(0, 0, 0, 0)
                    imgSelected.setImageURI(uri)
                }

                // For demonstration, show the URI in a Toast
                Toast.makeText(requireContext(), "File Selected: $uri", Toast.LENGTH_SHORT).show()

                // Assume you have a method in your ViewModel to save the URI
//                noteEntity.fileUri = uri.toString()
//                viewmodel.saveNote(noteEntity)
            }
        }
    }

    private fun getFileName(fileUri: Uri?): String? {
        fileUri ?: return null// If fileUri is null, return null
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

   private fun getMimeType(context: Context, uri: Uri): String? {
        return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            // If the URI is a content URI
            val contentResolver = context.contentResolver
            contentResolver.getType(uri)
        } else {
            // If the URI is a file URI
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase(Locale.ROOT))
        }
    }

}