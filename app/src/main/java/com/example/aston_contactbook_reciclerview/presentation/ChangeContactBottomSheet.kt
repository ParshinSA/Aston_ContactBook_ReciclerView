package com.example.aston_contactbook_reciclerview.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aston_contactbook_reciclerview.R
import com.example.aston_contactbook_reciclerview.data.modelis.Contact
import com.example.aston_contactbook_reciclerview.presentation._common.recycler_view.contact.ContactItemRv
import com.google.gson.Gson

class ChangeContactBottomSheet : AddNewContactBottomSheet() {

    private lateinit var contactItemRv: ContactItemRv

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        handleOldContact()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun handleOldContact() {
        val oldContactJson = requireArguments().getString(CONTACT_STRING_MODEL)
        if (oldContactJson == null) this.dismiss()

        try {
            contactItemRv = Gson().fromJson(oldContactJson, ContactItemRv::class.java)
        } catch (t: Throwable) {
            t.printStackTrace()
            this.dismiss()
        }
    }

    override fun settings() {
        val oldContact = contactItemRv.dataContact
        binding.textFieldPhoneNumber.editText?.setText(oldContact.phoneNumber)
        binding.textFieldFirstName.editText?.setText(oldContact.firstName)
        binding.textFieldLastName.editText?.setText(oldContact.lastName)
        binding.titleBottomSheet.text = resources.getString(R.string.change_contact)

        binding.btnConfirm.setOnClickListener {
            val firstName = binding.textFieldFirstName.editText?.text?.toString()
                ?: return@setOnClickListener
            val lastName = binding.textFieldLastName.editText?.text?.toString()
                ?: return@setOnClickListener
            val phoneNumber = binding.textFieldPhoneNumber.editText?.text?.toString()
                ?: return@setOnClickListener

            mainActivity.receiveContactItemRv(
                contactItemRv.copy(
                    dataContact = Contact(0, firstName, lastName, phoneNumber),
                    isChecked = false,
                )
            )
            this.dismiss()
        }
        binding.btnDecline.setOnClickListener { this.dismiss() }
    }

    companion object {
        private const val CONTACT_STRING_MODEL = "CONTACT_STRING_MODEL"
        const val TAG = "ChangeContactBottomSheet"

        fun newInstance(contactStringModel: String): ChangeContactBottomSheet {
            return ChangeContactBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(CONTACT_STRING_MODEL, contactStringModel)
                }
            }
        }
    }

}