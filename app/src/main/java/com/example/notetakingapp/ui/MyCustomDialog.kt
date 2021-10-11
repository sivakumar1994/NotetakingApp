package com.example.notetakingapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.notetakingapp.utils.Constants
import com.example.notetakingapp.R
import com.example.notetakingapp.listener.OnConfirmationDialogeListener
import kotlinx.android.synthetic.main.custom_dialog_fragment.view.*

class MyCustomDialog(var listener: OnConfirmationDialogeListener?, var onclick: () -> Unit = {}) :
    DialogFragment() {
    private lateinit var title: String
    private lateinit var cancel: String
    private lateinit var confirm: String


    companion object {
        const val TITLE = "TITLE"

        @JvmStatic
        fun newInstance(
            title: String,
            cancel: Int = R.string.cancel,
            confirm: Int = R.string.confirm,
            onclick: () -> Unit
        ): MyCustomDialog {
            return MyCustomDialog(null, onclick).apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                    putInt(Constants.DIALOG_POSITIVE_BTN, confirm)
                    putInt(Constants.DIALOG_NEGATIVE_BTN, cancel)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_corner)
        return inflater.inflate(R.layout.custom_dialog_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cancelId = arguments?.getInt(Constants.DIALOG_NEGATIVE_BTN, 0) ?: 0
        val confirmId = arguments?.getInt(Constants.DIALOG_POSITIVE_BTN, 0) ?: 0

        title = arguments?.getString(TITLE) ?: getString(R.string.delete_msg)
        cancel = if (cancelId > 0) getString(cancelId) else getString(R.string.cancel)
        confirm = if (confirmId > 0) getString(confirmId) else getString(R.string.confirm)
        view.tvTitle.text = title
        view.btnCacel.text = cancel
        view.btnConfirm.text = confirm
        view.btnCacel?.setOnClickListener {
            dialog?.dismiss()
        }
        view.btnConfirm?.setOnClickListener {
            dialog?.dismiss()
            listener?.onConfirm()
            onclick.invoke()
        }
    }
}