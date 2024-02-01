package com.example.aston_contactbook_reciclerview.presentation._dependensy

import com.example.aston_contactbook_reciclerview.data.modelis.Contact

interface ContactBookRepository {
    fun createContactBook(): MutableList<Contact>
}
