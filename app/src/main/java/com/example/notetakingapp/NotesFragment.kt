package com.example.notetakingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : Fragment(), OnNotesItemClickListener {

    private var adapter: NotesListAdapter? = null
    private var notePinnedAdapter: NotesListAdapter? = null
    private var selectedNoteDetails : NoteDetail? = null

    lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivityViewModel =
            ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        initView()
    }

    private fun initView() {
        rv_notes_list?.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_notes_pinned_list?.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = NotesListAdapter(this)
        notePinnedAdapter = NotesListAdapter(this)
        rv_notes_list?.adapter = adapter
        rv_notes_pinned_list?.adapter = notePinnedAdapter

        mainActivityViewModel.getNotesDetail()
        mainActivityViewModel.getPinnedNotesDetail(true)

        mainActivityViewModel.noteDetailsListLiveData.observe(viewLifecycleOwner, {
            if(!it.isEmpty()) {
                rv_notes_list.visibility =View.VISIBLE
                tv_others.visibility =View.VISIBLE
                adapter?.updateList(it as ArrayList<NoteDetail>)
            } else {
                rv_notes_list.visibility =View.GONE
                tv_others.visibility =View.GONE
            }
        })
        mainActivityViewModel.pinnedNoteDetailsListLiveData.observe(viewLifecycleOwner, {
            if(!it.isEmpty()) {
                rv_notes_pinned_list.visibility =View.VISIBLE
                tv_pinned.visibility =View.VISIBLE
                notePinnedAdapter?.updateList(it as ArrayList<NoteDetail>)
            } else {
                rv_notes_pinned_list.visibility =View.GONE
                tv_pinned.visibility =View.GONE
            }
        })
        mainActivityViewModel.isMainMenuPinButtomClicked.observe(viewLifecycleOwner,{
            if(it) {
                if(selectedNoteDetails!=null) {
                    mainActivityViewModel.onUpdatePinStatusNotesDetail(selectedNoteDetails!!.id,true)

                }
                mainActivityViewModel.isMainMenuPinButtomClicked.value = false
            }
        })
        mainActivityViewModel.isPinnedStatusUpdated.observe(viewLifecycleOwner,{
            mainActivityViewModel.getNotesDetail()
            mainActivityViewModel.getPinnedNotesDetail(true)
        })
        mainActivityViewModel.isMainMenuDeleteButtonClicked.observe(viewLifecycleOwner,{
            if(it)
            selectedNoteDetails?.let { it1 -> mainActivityViewModel.onDeleteNotesDetail(it1.id) }
        })

    }


    override fun onSelectedNotesItem(noteDetail: NoteDetail) {

        findNavController().navigate(
            NotesFragmentDirections.actionNoteFragmentDestToNotesDetailsFragmentDest(
                noteDetail.id
            )
        )
        mainActivityViewModel.isNoteAdapterItemClicked.value = true
    }

    override fun onLongPressedNotesItem(noteDetail: NoteDetail?) {
        selectedNoteDetails =noteDetail
        if(null == noteDetail) {
            mainActivityViewModel.isNeedToShowLongPressEventToolbar.value =false
        } else {
            mainActivityViewModel.isNeedToShowLongPressEventToolbar.value =true
        }
    }
}