package com.example.notetakingapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.notetakingapp.R
import com.example.notetakingapp.listener.OnConfirmationDialogeListener
import com.example.notetakingapp.ui.viewmodel.MainActivityViewModel
import com.example.notetakingapp.utils.Constants.BUNDLE_NOTE_ID
import com.example.notetakingapp.utils.Constants.DIALOG_TAG
import com.example.notetakingapp.utils.SharedPreferenceHelper
import kotlinx.android.synthetic.main.fragment_notes_details.*


class NotesDetailsFragment : Fragment(), OnConfirmationDialogeListener {

    private lateinit var mainActivityViewModel: MainActivityViewModel

    private var noteId = -1L

    private var isPinned = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel =
            ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteId = arguments?.getLong(BUNDLE_NOTE_ID, -1L) ?: -1L
        if (noteId != -1L) {
            mainActivityViewModel.getNoteDetailById(noteId)
        }
        attachObserver()
    }

    private fun attachObserver() {
        mainActivityViewModel.specificNoteData.observe(viewLifecycleOwner, {
            if (it != null) {
                et_notes_title?.setText(it.title)
                et_description?.setText(it.content)
                if (it?.isPinned != null)
                    this.isPinned = it.isPinned
                if (it?.imageUrl != null)
                    img_attached.setImageURI(Uri.parse(it.imageUrl))
                mainActivityViewModel.title.value = it.title
                mainActivityViewModel.content.value = it.content
            }
        })

        mainActivityViewModel.imageUriForLoadImage.observe(
            viewLifecycleOwner, { item ->
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
                    Toast.makeText(context, getString(R.string.pin_note_error_save_alert), Toast.LENGTH_SHORT)
                        .show()
                } else if (!isPinned) {
                    //  Toast.makeText(context, "Note Pinned Successfully", Toast.LENGTH_SHORT).show()
                    mainActivityViewModel.onUpdatePinStatusNotesDetail(noteId, true)
                    mainActivityViewModel.isPinned.value = true
                    isPinned = true
                } else {
                    Toast.makeText(context, getString(R.string.pin_note_un_pinned), Toast.LENGTH_SHORT).show()
                    mainActivityViewModel.onUpdatePinStatusNotesDetail(noteId, false)
                    isPinned = false

                }
                mainActivityViewModel.isPinButtomClicked.value = false
            }
        })
        mainActivityViewModel.isSharedButtonClicked.observe(viewLifecycleOwner, {
            if (it) {
                if (noteId != -1L) {
                    if (SharedPreferenceHelper(requireContext()).getSharedNotesPref()) {
                        val intent = Intent(Intent.ACTION_SEND)
                        val shareBody =
                            "Title : " + mainActivityViewModel.title.value + '\n' + "Content :" + mainActivityViewModel.content.value
                        intent.type = "text/plain"
                        intent.putExtra(
                            Intent.EXTRA_SUBJECT,
                            getString(R.string.share_subject)
                        )
                        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
                        startActivity(
                            Intent.createChooser(
                                intent,
                                getString(R.string.share_using)
                            )
                        )
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.share_error_enable_setting),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(context, getString(R.string.share_error_save), Toast.LENGTH_SHORT)
                        .show()
                }
                mainActivityViewModel.isSharedButtonClicked.value = false
            }
        })
    }

    private fun showDeleteDialog() {
        MyCustomDialog(this).show(childFragmentManager, DIALOG_TAG)
    }

    override fun onConfirm() {
        mainActivityViewModel.isDeleteConfirmButtonClicked.value = true
        mainActivityViewModel.onDeleteNotesDetail(noteId)
        mainActivityViewModel.isDeleteButtonClicked.value = false
        NavHostFragment.findNavController(this).popBackStack()
    }


}