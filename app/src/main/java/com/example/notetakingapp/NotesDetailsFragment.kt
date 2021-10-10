package com.example.notetakingapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_notes_details.*


class NotesDetailsFragment : Fragment() {

    lateinit var mainActivityViewModel: MainActivityViewModel
    private var noteId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel =
            ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        noteId = arguments?.getLong("notesId",-1L)?: -1L

        if(noteId != -1L) {
            mainActivityViewModel.getNoteDetailById(noteId)
        }

           /* mainActivityViewModel.content.value = content
            mainActivityViewModel.imageUriForLoadImage.value = imageUrl
            et_notes_title?.setText(title)
            et_description?.setText(title)
            img_attached.setImageURI(Uri.parse(imageUrl))*/


        attachObserver()

    }

    private fun attachObserver() {
        mainActivityViewModel.specificNoteData.observe(viewLifecycleOwner,{it->

            et_notes_title?.setText(it.title)
            et_description?.setText(it.content)
            img_attached.setImageURI(Uri.parse(it.imageUrl))
        })
        mainActivityViewModel.imageUriForLoadImage.observe(
            viewLifecycleOwner,
            Observer<String> { item ->
                img_attached.setImageURI(Uri.parse(item))
            })

        mainActivityViewModel.isSaveButtonClicked.observe(viewLifecycleOwner, {

            if (it) {
                mainActivityViewModel.title.value = et_notes_title.text.toString()
                mainActivityViewModel.content.value = et_description.text.toString()
                if(noteId == -1L) {
                    mainActivityViewModel.onInsertNotesDetail()
                } else {
                    mainActivityViewModel.onUpdateNotesDetail(noteId)
                }
                NavHostFragment.findNavController(this).popBackStack()
            }
        })
    }


}