package com.example.cameraxapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.cameraxapp.adapter.OnItemClickListener
import com.example.cameraxapp.databinding.ActivityMainBinding
import com.example.cameraxapp.db.NoteEntity
import com.example.cameraxapp.viewmodel.DatabaseViewmodel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    var frameLayout: FrameLayout? = null
    var fragmentManager: FragmentManager? = null
    var fragmentTransaction: FragmentTransaction? = null

    private lateinit var btnProfile:ImageView
    private lateinit var uri: Uri
    private var _binding: ActivityMainBinding? = null
    private lateinit var navView:BottomNavigationView
    private val binding get() = _binding
    private val REQUEST_CODE_OPEN_DOCUMENT = 123
    private val viewModel: DatabaseViewmodel by inject()
    lateinit var recyclerView: RecyclerView
   // private val noteAdapter by lazy { NoteAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        btnProfile = findViewById(R.id.profileIcon)
        navView = findViewById(R.id.bottomNavigationView)

        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout,homeFragment ) // Ensure you have a FrameLayout in your layout where the fragment should be placed
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            //.addToBackStack(null) // Optional: Add transaction to the back stack
            .commit()

        navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navImport -> {
                    //recyclerView.visibility = View.GONE
                    // Replace Toast with Fragment transaction
                    val importFragment = ImportFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, importFragment) // Ensure you have a FrameLayout in your layout where the fragment should be placed
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        //.addToBackStack(null) // Optional: Add transaction to the back stack
                        .commit()
//                    recyclerView.visibility = View.GONE
                }

                R.id.navHome -> {
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,homeFragment ) // Ensure you have a FrameLayout in your layout where the fragment should be placed
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        //.addToBackStack(null) // Optional: Add transaction to the back stack
                        .commit()
                }
            }

            true
        }

        btnProfile.setOnClickListener {
            val i = Intent(this@MainActivity,RegistrationActivity::class.java)
            startActivity(i)
        }


       // binding!!.apply {
//            addfile.setOnClickListener {
//                AddNoteFragment().show(supportFragmentManager, AddNoteFragment().tag)
//            }

//            viewModel.getAllNote()
//            viewModel.noteList.observe(this@MainActivity) {
//                if (it.isNotEmpty()) {
//                    showEmpty(true)
//                    noteAdapter.differ.submitList(it)
//                    recycleview.apply {
//                        layoutManager = LinearLayoutManager(this@MainActivity)
//                        adapter = noteAdapter
//                    }
//                } else {
//                    showEmpty(false)
//                }
//            }

      //  }
    }

//    private fun showEmpty(isShown: Boolean) {
//        binding!!.apply {
//            if (isShown) {
//                recycleview.visibility = View.VISIBLE
//                //tvEmptyText.visibility = View.GONE
//            } else {
//                recycleview.visibility = View.GONE
//               // tvEmptyText.visibility = View.VISIBLE
//            }
//        }
//    }

//    override fun onResume() {
//        super.onResume()
//        if (_binding == null) {
//            _binding = ActivityMainBinding.inflate(layoutInflater)
//            setContentView(binding!!.root)
//
//            binding!!.apply {
//                addfile.setOnClickListener {
//                    AddNoteFragment().show(supportFragmentManager, AddNoteFragment().tag)
//                }
//
////                viewModel.getAllNote()
////                viewModel.noteList.observe(this@MainActivity) {
////                    if (it.isNotEmpty()) {
////                        showEmpty(true)
////                        noteAdapter.differ.submitList(it)
////                        recycleview.apply {
////                            layoutManager = LinearLayoutManager(this@MainActivity)
////                            adapter = noteAdapter
////                        }
////                    } else {
////                        showEmpty(false)
////                    }
////                }
//
//                binding!!.profileIcon.setOnClickListener {
//                    startActivity(Intent(this@MainActivity,RegistrationActivity::class.java))
//                }
//
//                binding!!.bottomNavigationView.setOnItemSelectedListener {
//                    when(it.itemId){
//                        R.id.navImport -> {
//                            //recyclerView.visibility = View.GONE
//                            // Replace Toast with Fragment transaction
//                            val importFragment = ImportFragment()
//                            supportFragmentManager.beginTransaction()
//                                .replace(R.id.frameLayout, importFragment) // Ensure you have a FrameLayout in your layout where the fragment should be placed
//                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                                .addToBackStack(null) // Optional: Add transaction to the back stack
//                                .commit()
////                    recyclerView.visibility = View.GONE
//                        }
//
//                        R.id.navHome -> {
//                            val homeFragment = HomeFragment()
//                            supportFragmentManager.beginTransaction()
//                                .replace(R.id.frameLayout,homeFragment ) // Ensure you have a FrameLayout in your layout where the fragment should be placed
//                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                                .addToBackStack(null) // Optional: Add transaction to the back stack
//                                .commit()
//                        }
//                    }
//
//                    true
//                }
//
//                val homeFragment = HomeFragment()
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.frameLayout,homeFragment ) // Ensure you have a FrameLayout in your layout where the fragment should be placed
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                    .addToBackStack(null) // Optional: Add transaction to the back stack
//                    .commit()
//
//            }
//        }
//
//    }

//    override fun onStop() {
//        super.onStop()
//        _binding = null
//    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//    }

//    override fun onItemClick(note: NoteEntity, fileUriString: String) {
//        val fileUri = Uri.parse(note.fileUri)
//        openFile(fileUri)
//
//    }


    private fun openFile(fileUri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(fileUri, contentResolver.getType(fileUri))
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            startActivity(Intent.createChooser(intent, "Open File"))
        } catch (e: Exception) {
            Log.e("MainActivity", "Error opening file: ${e.message}")
            Toast.makeText(this, "Error opening file", Toast.LENGTH_SHORT).show()
        }
    }
}
