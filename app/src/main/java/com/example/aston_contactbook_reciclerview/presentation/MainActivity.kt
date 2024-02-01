package com.example.aston_contactbook_reciclerview.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_contactbook_reciclerview.R
import com.example.aston_contactbook_reciclerview.databinding.ActivityMainBinding
import com.example.aston_contactbook_reciclerview.presentation._common.recycler_view.DiffUtilItemCallback
import com.example.aston_contactbook_reciclerview.presentation._common.recycler_view.contact.ContactItemRv
import com.example.aston_contactbook_reciclerview.presentation._common.recycler_view.contact.ContactRvAdapter
import com.google.gson.Gson
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModel: MainViewModel

    private val contactRvAdapter by lazy {
        AsyncListDifferDelegationAdapter(
            DiffUtilItemCallback(), ContactRvAdapter(
                updateContactItemRv = viewModel::updateContactItemRv,
                screenState = viewModel.screenStateStateFlow,
                initChangeContact = ::initChangeContact,
            )
        )
    }

    private val callbackMoved by lazy {
        object : ItemTouchHelper
        .SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val oldPosition = viewHolder.absoluteAdapterPosition
                val newPosition = target.absoluteAdapterPosition
                viewModel.itemMoved(oldPosition, newPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        }
    }

    private val itemTouchHelper by lazy { ItemTouchHelper(callbackMoved) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inject()

        if (savedInstanceState == null) {
            initSettings()
            initActions()
        } else initSettings()
    }

    private fun initActions() {
        viewModel.getContacts()
    }

    private fun initSettings() {
        settingsContactList()
        settingsInitDeleteContactState()
        settingsCancelDeleteContactsState()
        settingsConfirmDeleteContacts()
        settingsAddNewContact()
        observeData()
    }

    private fun settingsConfirmDeleteContacts() {
        binding.confirmDelete.setOnClickListener {
            viewModel.deleteCheckedContact()
            viewModel.changeScreenState(ScreenState.NORMAL)
        }
    }

    private fun settingsCancelDeleteContactsState() {
        binding.declineDelete.setOnClickListener {
            viewModel.changeScreenState(ScreenState.NORMAL)
        }
    }

    private fun settingsInitDeleteContactState() {
        binding.toolbar.menu.findItem(R.id.initDelete).setOnMenuItemClickListener {
            viewModel.changeScreenState(ScreenState.DELETE_CONTACT)
            true
        }
    }

    private fun settingsAddNewContact() {
        binding.btnAddContact.setOnClickListener {
            viewModel.changeScreenState(ScreenState.ADD_CONTACT)

            AddNewContactBottomSheet.newInstance()
                .show(supportFragmentManager, AddNewContactBottomSheet.TAG)
        }
    }

    fun receiveContactItemRv(contactItemRv: ContactItemRv?) {
        if (contactItemRv == null) {
            viewModel.changeScreenState(ScreenState.NORMAL)
            return
        }

        when (viewModel.screenStateStateFlow.value) {
            ScreenState.CHANGE_CONTACT -> viewModel.updateContactItemRv(contactItemRv)
            ScreenState.ADD_CONTACT -> {
                viewModel.addContact(contactItemRv)

                lifecycleScope.launch {
                    delay(500)
                    binding.contactList.scrollToPosition(0)
                }
            }

            else -> {}
        }

        viewModel.changeScreenState(ScreenState.NORMAL)
    }

    private fun settingsContactList() {
        itemTouchHelper.attachToRecyclerView(binding.contactList)
        binding.contactList.apply {
            adapter = contactRvAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initChangeContact(contactItemRv: ContactItemRv) {
        viewModel.changeScreenState(ScreenState.CHANGE_CONTACT)
        val contactItemRvJson = Gson().toJson(contactItemRv)
        ChangeContactBottomSheet.newInstance(contactItemRvJson)
            .show(supportFragmentManager, ChangeContactBottomSheet.TAG)
    }

    private fun observeData() {
        viewModel.contactBookStateFlow.onEach { listContactItemRv: List<ContactItemRv>? ->
            if (listContactItemRv == null) return@onEach
            updateCounterCheckedContacts(listContactItemRv)
            updateContactList(listContactItemRv)
        }.launchIn(lifecycleScope)

        viewModel.screenStateStateFlow.onEach { screenState: ScreenState ->
            updateStateCounterCheckedContacts(screenState)
            updateStateBtnConfirmDelete(screenState)
            updateStateBtnDeclineDelete(screenState)
            updateStateBtnInitDelete(screenState)
            updateStateBtnAddContact(screenState)
        }.launchIn(lifecycleScope)

    }

    private fun updateCounterCheckedContacts(listContactItemRv: List<ContactItemRv>) {
        binding.counterCheckedContacts.text = listContactItemRv.count { it.isChecked }.toString()
    }

    private fun updateStateCounterCheckedContacts(screenState: ScreenState) {
        val counterCheckedContacts = binding.counterCheckedContacts
        when (screenState) {
            ScreenState.DELETE_CONTACT -> counterCheckedContacts.show()
            else -> counterCheckedContacts.hide()
        }
    }

    private fun updateStateBtnAddContact(screenState: ScreenState) {
        val bntAddContact = binding.btnAddContact
        when (screenState) {
            ScreenState.NORMAL -> bntAddContact.show()
            else -> bntAddContact.hide()
        }
    }

    private fun updateStateBtnDeclineDelete(screenState: ScreenState) {
        val bntDeclineDelete = binding.declineDelete
        when (screenState) {
            ScreenState.DELETE_CONTACT -> bntDeclineDelete.show()
            else -> bntDeclineDelete.hide()
        }
    }

    private fun updateStateBtnConfirmDelete(screenState: ScreenState) {
        val bntConfirmDelete = binding.confirmDelete
        when (screenState) {
            ScreenState.DELETE_CONTACT -> bntConfirmDelete.show()
            else -> bntConfirmDelete.hide()
        }
    }

    private fun updateStateBtnInitDelete(screenState: ScreenState) {
        val btnInitDelete = binding.toolbar.menu.findItem(R.id.initDelete)
        when (screenState) {
            ScreenState.DELETE_CONTACT -> btnInitDelete.isEnabled = false
            else -> btnInitDelete.isEnabled = true
        }
    }

    private fun updateContactList(newList: List<ContactItemRv>?) {
        newList ?: return
        contactRvAdapter.items = newList
    }

    private fun inject() {
        (applicationContext as? AppApplication)?.appComponent?.inject(this)
    }

    enum class ScreenState {
        DELETE_CONTACT,
        CHANGE_CONTACT,
        ADD_CONTACT,
        NORMAL,
    }

}