package com.example.cameraxapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cameraxapp.adapter.NoteAdapter
import com.example.cameraxapp.databinding.FragmentHomeBinding
import com.example.cameraxapp.db.NoteEntity
import com.example.cameraxapp.viewmodel.DatabaseViewmodel
import org.koin.android.ext.android.inject
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DatabaseViewmodel by inject()
    private lateinit var recyclerView: RecyclerView
    private lateinit var folderRecyclerView: RecyclerView
    private val noteAdapter by lazy { NoteAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileIcon.setOnClickListener {
            val i = Intent(requireActivity(), RegistrationActivity::class.java)
            startActivity(i)
        }

        binding.relativeCross.setOnClickListener {
            binding.registerCard.visibility = View.GONE
        }

        folderRecyclerView = binding.folderRecyclerView
        folderRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        val folderList = listOf(
            Folder("Folder 1", "example1@gmail.com", 10),
            Folder("Folder 2", "example2@gmail.com", 20),
            Folder("Folder 3", "example3@gmail.com", 30)
            // Add more folders as needed
        )

        val adapter = FolderAdapter(folderList)
        folderRecyclerView.adapter = adapter

        recyclerView = binding.recycleview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = noteAdapter
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        viewModel.getAllNote()
        viewModel.noteList.observe(viewLifecycleOwner) { noteList ->
            if (noteList.isNotEmpty()) {
                showEmpty(false)
                noteAdapter.differ.submitList(noteList)
            } else {
                showEmpty(true)
            }
        }
    }

    private fun showEmpty(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recycleview.visibility = View.GONE
            binding.tvEmptyText.visibility = View.VISIBLE
        } else {
            binding.recycleview.visibility = View.VISIBLE
            binding.tvEmptyText.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onItemClick(note: NoteEntity?, fileUriString: String) {
        val fileUri = Uri.parse(fileUriString)
        val file = File(fileUri.path!!)

        openFileFromUri(fileUri)
    }

     fun onItemFavoriteClick(note: NoteEntity) {
        // Update the favorite status in the database
        viewModel.saveNote(note)
        // If you are using LiveData to observe changes, the RecyclerView will update automatically
    }
    private fun uriToFile(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = cursor?.getString(columnIndex ?: -1)
        cursor?.close()
        return filePath?.let { File(it) }
    }
    fun onShareContent(content:String){
        val file = uriToFile(content.toUri())
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, file)
        startActivity(Intent.createChooser(intent, "Share via"))
    }


//    private fun openFile(file: File) {
//        val mimeType = getMimeType(file)
//        val uri = FileProvider.getUriForFile(requireContext(), "com.example.cameraxapp.provider", file)
//
//        // Create an intent to view the file
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.setDataAndType(uri, mimeType)
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        // Check if there's an activity available to handle this intent
//        if (intent.resolveActivity(requireActivity().packageManager) != null) {
//            // Start the activity to view the file
//            startActivity(intent)
//        } else {
//            // Handle the case where no activity is available to handle the intent
//            // Display a toast or show an error message
//        }
//    }

  private  fun openFileFromUri(uri: Uri) {
     try {
         val intent = Intent(Intent.ACTION_VIEW)

         // Use the document URI directly
         intent.setDataAndType(uri, requireContext().contentResolver.getType(uri))
         intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

         if (intent.resolveActivity(requireActivity().packageManager) != null) {
             startActivity(intent)
         }
     } catch (exception:Exception){
         Toast.makeText(requireContext(),"uri:$uri",Toast.LENGTH_SHORT).show()
     }

    }

    private fun getMimeType(file: File): String {
        val extension = file.extension.toLowerCase()

        // Determine the MIME type based on the file extension
        return when (extension) {
            "pdf" -> "application/pdf"
            "doc", "docx" -> "application/msword"
            "xls", "xlsx" -> "application/vnd.ms-excel"
            "ppt", "pptx" -> "application/vnd.ms-powerpoint"
            "txt" -> "text/plain"
            "png" -> "image/png"
            "jpg", "jpeg" -> "image/jpeg"
            "gif" -> "image/gif"
            else -> "*/*" // fallback MIME type
        }
    }
}