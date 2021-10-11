package com.example.notetakingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.example.notetakingapp.R
import com.example.notetakingapp.utils.SharedPreferenceHelper
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switch1.isChecked = SharedPreferenceHelper(requireContext()).getNotesOrderPref()
        switch1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            SharedPreferenceHelper(requireContext()).saveNotesOrderPref(isChecked)
        })
        switch2.isChecked = SharedPreferenceHelper(requireContext()).getSharedNotesPref()
        switch2.setOnCheckedChangeListener { buttonView, isChecked ->
            SharedPreferenceHelper(requireContext()).saveSharedNotesPref(isChecked)
        }
    }
}