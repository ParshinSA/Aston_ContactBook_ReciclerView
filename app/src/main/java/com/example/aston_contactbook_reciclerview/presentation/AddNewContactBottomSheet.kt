package com.example.aston_contactbook_reciclerview.presentation

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aston_contactbook_reciclerview.data.modelis.Contact
import com.example.aston_contactbook_reciclerview.databinding.DialogAddNewContactBinding
import com.example.aston_contactbook_reciclerview.presentation._common.recycler_view.contact.ContactItemRv
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class AddNewContactBottomSheet : BottomSheetDialogFragment() {

    protected lateinit var binding: DialogAddNewContactBinding
    val mainActivity by lazy { requireActivity() as MainActivity }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddNewContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settings()
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    open fun settings() {
        binding.btnConfirm.setOnClickListener {
            val firstName = binding.textFieldFirstName.editText?.text?.toString()
                ?: return@setOnClickListener
            val lastName = binding.textFieldLastName.editText?.text?.toString()
                ?: return@setOnClickListener
            val phoneNumber = binding.textFieldPhoneNumber.editText?.text?.toString()
                ?: return@setOnClickListener

            mainActivity.receiveContactItemRv(
                ContactItemRv(
                    dataContact = Contact(0, firstName, lastName, phoneNumber),
                    isChecked = false,
                )
            )
            this.dismiss()
        }
        binding.btnDecline.setOnClickListener { this.dismiss() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        mainActivity.receiveContactItemRv(null)
        super.onDismiss(dialog)
    }

    companion object {
        const val TAG = "AddNewContactBottomSheet"

        fun newInstance() = AddNewContactBottomSheet()
    }
}