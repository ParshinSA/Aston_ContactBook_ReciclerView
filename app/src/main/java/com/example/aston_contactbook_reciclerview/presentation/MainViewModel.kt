package com.example.aston_contactbook_reciclerview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aston_contactbook_reciclerview.presentation._common.recycler_view.contact.ContactItemRv
import com.example.aston_contactbook_reciclerview.presentation._dependensy.ContactBookRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Collections

class MainViewModel(
    private val repository: ContactBookRepository,
) : ViewModel() {

    private val contactBookMutStateFlow = MutableStateFlow<List<ContactItemRv>?>(null)
    private val screenStateMutStateFlow = MutableStateFlow(MainActivity.ScreenState.NORMAL)

    val screenStateStateFlow get() = screenStateMutStateFlow.asStateFlow()
    val contactBookStateFlow get() = contactBookMutStateFlow.asStateFlow()

    fun getContacts() {
        contactBookMutStateFlow.value = repository.createContactBook()
            .map { ContactItemRv(it, false) }
    }

    fun updateContactItemRv(newContactItemRv: ContactItemRv) {
        contactBookMutStateFlow.value = contactBookMutStateFlow.value
            ?.map { contactItemRv: ContactItemRv ->
                if (newContactItemRv.id == contactItemRv.id)
                    newContactItemRv
                else
                    contactItemRv
            }
    }

    fun addContact(newContactItemRv: ContactItemRv) {
        val newList = contactBookMutStateFlow.value?.toMutableList() ?: mutableListOf()
        val newId = newList.maxByOrNull { it.id }?.id?.plus(1) ?: 0
        val newContact = newContactItemRv.dataContact.copy(id = newId)
        newList.add(0, ContactItemRv(dataContact = newContact, isChecked = false))
        contactBookMutStateFlow.value = newList
    }

    fun deleteCheckedContact() {
        contactBookMutStateFlow.value = contactBookMutStateFlow.value?.filter { !it.isChecked }
    }

    fun changeScreenState(screenState: MainActivity.ScreenState) {
        screenStateMutStateFlow.value = screenState
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

    fun itemMoved(oldPosition: Int, newPosition: Int) {
        val movedList = contactBookMutStateFlow.value?.toMutableList() ?: return

        Collections.swap(
            movedList,
            oldPosition,
            newPosition
        )

        contactBookMutStateFlow.value = movedList
    }
}