package com.example.notetakingapp

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_notes_details.*


class NotesDetailsFragment : Fragment(), OnConfirmationDialogeListener {

    lateinit var mainActivityViewModel: MainActivityViewModel
    private var noteId = -1L
    var isPinned = false;

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



        noteId = arguments?.getLong("notesId", -1L) ?: -1L

        if (noteId != -1L) {
            mainActivityViewModel.getNoteDetailById(noteId)
        }

        attachObserver()

    }

    private fun attachObserver() {
        mainActivityViewModel.specificNoteData.observe(viewLifecycleOwner, {
            if (it != null) {
                et_notes_title?.setText(it?.title)
                et_description?.setText(it?.content)
                if (it?.isPinned != null)
                    this.isPinned = it.isPinned
                if (it?.imageUrl != null)
                    img_attached.setImageURI(Uri.parse(it.imageUrl))
            }
        })

        mainActivityViewModel.imageUriForLoadImage.observe(
            viewLifecycleOwner,
            Observer<String> { item ->
                if (item != null)
                    img_attached.setImageURI(Uri.parse(item))
            })

        mainActivityViewModel.isSaveButtonClicked.observe(viewLifecycleOwner, {

            if (it) {
                mainActivityViewModel.title.value = et_notes_title.text.toString()
                mainActivityViewModel.content.value = et_description.text.toString()
                if (noteId == -1L) {
                    mainActivityViewModel.onInsertNotesDetail()
                } else {
                    mainActivityViewModel.isPinned.value = isPinned
                    mainActivityViewModel.onUpdateNotesDetail(noteId)
                }
                noteId = -1L
                NavHostFragment.findNavController(this).popBackStack()
            }
        })
        mainActivityViewModel.isDeleteButtonClicked.observe(viewLifecycleOwner, {
            if (it) {
                showDeleteDialog()
                mainActivityViewModel.isDeleteButtonClicked.value = false
            }
        })
        mainActivityViewModel.isPinButtomClicked.observe(viewLifecycleOwner, {
            if (it) {
                if (noteId == -1L) {
                    Toast.makeText(context, "kindly save your Notes before pin", Toast.LENGTH_SHORT)
                        .show()
                } else if (!isPinned) {
                  //  Toast.makeText(context, "Note Pinned Successfully", Toast.LENGTH_SHORT).show()
                    mainActivityViewModel.onUpdatePinStatusNotesDetail(noteId, true)
                    mainActivityViewModel.isPinned.value = true
                    isPinned =true
                } else {
                    Toast.makeText(context, "Note UnPinned Successfully", Toast.LENGTH_SHORT).show()
                    mainActivityViewModel.onUpdatePinStatusNotesDetail(noteId, false)
                    isPinned =false

                }
                mainActivityViewModel.isPinButtomClicked.value = false
            }
        })
    }

    private fun showDeleteDialog() {
        MyCustomDialog(this).show(childFragmentManager, "MyCustomDialog")
    }
    override fun onConfirm() {
        mainActivityViewModel.isDeleteConfirmButtonClicked.value = true
        mainActivityViewModel.onDeleteNotesDetail(noteId)
        mainActivityViewModel.isDeleteButtonClicked.value = false
        NavHostFragment.findNavController(this).popBackStack()
    }


}