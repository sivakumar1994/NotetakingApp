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
import kotlinx.android.synthetic.main.my_toolbar.*

class NotesFragment : Fragment(), OnNotesItemClickListener {

    private var adapter: NotesListAdapter? = null

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
        mainActivityViewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        initView()
    }

    private fun initView() {
        rv_notes_list?.layoutManager = GridLayoutManager(requireContext(), 2)
        //adapter = mainActivityViewModel.getNotesDetail().value?.let { NotesListAdapter(this ) }
        adapter = NotesListAdapter(this )
        rv_notes_list?.adapter = adapter
        mainActivityViewModel.getNotesDetail()
        mainActivityViewModel.noteDetailsListLiveData.observe(viewLifecycleOwner,{
            adapter?.updateList(it as ArrayList<NoteDetail>)
        })

    }


    override fun onSelectedNotesItem(noteDetail: NoteDetail) {

        findNavController().navigate(NotesFragmentDirections.actionNoteFragmentDestToNotesDetailsFragmentDest(noteDetail.id))
        mainActivityViewModel.isNoteAdapterItemClicked.value = true
    }
}