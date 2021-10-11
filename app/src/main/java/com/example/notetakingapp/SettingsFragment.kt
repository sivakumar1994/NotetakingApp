package com.example.notetakingapp

import android.app.backup.SharedPreferencesBackupHelper
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
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
        switch1.setChecked(SharedPreferenceHelper(requireContext()).getNotesOrderPref())
        switch1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            SharedPreferenceHelper(requireContext()).saveNotesOrderPref(isChecked)
        })
    }
}