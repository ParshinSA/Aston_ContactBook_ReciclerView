package com.example.aston_contactbook_reciclerview.presentation._common.recycler_view.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_contactbook_reciclerview.R
import com.example.aston_contactbook_reciclerview.databinding.ItemContactBinding
import com.example.aston_contactbook_reciclerview.presentation.MainActivity
import com.example.aston_contactbook_reciclerview.presentation.MainActivity.ScreenState.DELETE_CONTACT
import com.example.aston_contactbook_reciclerview.presentation._common.markers.Displayable
import com.example.aston_contactbook_reciclerview.presentation._common.recycler_view.contact.ContactRvAdapter.ContactViewHolder
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ContactRvAdapter(
    private val updateContactItemRv: (newContactItemRv: ContactItemRv) -> Unit,
    private val screenState: StateFlow<MainActivity.ScreenState>,
    private val initChangeContact: (ContactItemRv) -> Unit,
) : AbsListItemAdapterDelegate<ContactItemRv, Displayable, ContactViewHolder>() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun isForViewType(item: Displayable, items: MutableList<Displayable>, position: Int) =
        item is ContactItemRv

    override fun onCreateViewHolder(parent: ViewGroup): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(
            updateContactItemRv = updateContactItemRv,
            initChangeContact = initChangeContact,
            screenState = screenState,
            view = view
        )
    }

    override fun onBindViewHolder(
        item: ContactItemRv, holder: ContactViewHolder, payloads: MutableList<Any>
    ) = holder.bind(item)

    inner class ContactViewHolder(
        private val updateContactItemRv: (newContactItemRv: ContactItemRv) -> Unit,
        private val screenState: StateFlow<MainActivity.ScreenState>,
        private val initChangeContact: (ContactItemRv) -> Unit,
        view: View,
    ) : RecyclerView.ViewHolder(view) {

        private val binding = ItemContactBinding.bind(view)
        private val resources = view.context.resources

        private val normalStateColor = resources.getColor(R.color.contactUnChecked, null)
        private val checkedStateColor = resources.getColor(R.color.contactChecked, null)

        private lateinit var currentContactItemRv: ContactItemRv

        init {
            handleShortClickOnContact()
            observeOnContactState()
        }

        fun bind(contactItemRv: ContactItemRv) {
            currentContactItemRv = contactItemRv
            setFullName(contactItemRv)
            setPhoneNumber(contactItemRv)
            setCheckState(contactItemRv.isChecked)
        }

        private fun setPhoneNumber(contactItemRv: ContactItemRv) {
            binding.phoneNumber.text = contactItemRv.dataContact.phoneNumber
        }

        private fun setCheckState(isChecked: Boolean) {
            if (isChecked) {
                binding.contactCheckBox.setImageResource(R.drawable.ic_check_box_true)
                binding.contactContainer.setBackgroundColor(checkedStateColor)
            } else {
                binding.contactCheckBox.setImageResource(R.drawable.ic_check_box_false)
                binding.contactContainer.setBackgroundColor(normalStateColor)
            }
        }

        private fun setFullName(contactItemRv: ContactItemRv) {
            with(contactItemRv.dataContact) {
                binding.fullName.text = resources.getString(R.string.fullName, firstName, lastName)
            }
        }

        private fun normalState() {
            binding.contactCheckBox.isVisible = false
            setCheckState(false)
        }

        private fun checkableState() {
            changeCheckStateContact(false)
            binding.contactCheckBox.isVisible = true
        }

        private fun observeOnContactState() {
            coroutineScope.launch {
                screenState.onEach { state: MainActivity.ScreenState ->
                    when (state) {
                        DELETE_CONTACT -> checkableState()
                        else -> normalState()
                    }
                }.collect()
            }
        }

        private fun handleShortClickOnContact() {
            binding.contactContainer.setOnClickListener {
                when (screenState.value) {
                    DELETE_CONTACT -> changeCheckStateContact(!currentContactItemRv.isChecked)
                    else -> initChangeContact(currentContactItemRv)
                }
            }
        }

        private fun changeCheckStateContact(newState: Boolean) {
            updateContactItemRv(currentContactItemRv.copy(isChecked = newState))
        }

    }

}
