package com.example.aston_contactbook_reciclerview.data.repositories

import android.content.Context
import com.example.aston_contactbook_reciclerview.R
import com.example.aston_contactbook_reciclerview.data.modelis.Contact
import com.example.aston_contactbook_reciclerview.presentation._dependensy.ContactBookRepository
import kotlin.random.Random

class ContactBookRepositoryImpl(
    private val context: Context
) : ContactBookRepository {

    override fun createContactBook(): MutableList<Contact> {
        val listFirstNames = context.resources.getStringArray(R.array.firstNames)
        val listLastNames = context.resources.getStringArray(R.array.lastNames)
        val contactBook = mutableListOf<Contact>()

        repeat(100) {
            contactBook.add(
                Contact(
                    id = it,
                    firstName = listFirstNames.random(),
                    lastName = listLastNames.random(),
                    phoneNumber = "+7" +
                            "(${Random.nextInt(100, 999)})" +
                            "-${Random.nextInt(100, 999)}" +
                            "-${Random.nextInt(10, 99)}" +
                            "-${Random.nextInt(10, 99)}"
                )
            )
        }
        return contactBook
    }



}