package com.example.aston_contactbook_reciclerview.presentation._common.recycler_view.contact

import com.example.aston_contactbook_reciclerview.data.modelis.Contact
import com.example.aston_contactbook_reciclerview.presentation._common.markers.Displayable

data class ContactItemRv(
    val dataContact: Contact,
    val isChecked: Boolean,
    override val id: Int = dataContact.id
) : Displayable