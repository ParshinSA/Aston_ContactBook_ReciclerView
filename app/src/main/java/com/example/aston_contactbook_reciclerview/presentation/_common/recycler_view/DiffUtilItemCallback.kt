package com.example.aston_contactbook_reciclerview.presentation._common.recycler_view

import androidx.recyclerview.widget.DiffUtil
import com.example.aston_contactbook_reciclerview.presentation._common.markers.Displayable
import com.example.aston_contactbook_reciclerview.presentation._common.recycler_view.contact.ContactItemRv

class DiffUtilItemCallback : DiffUtil.ItemCallback<Displayable>() {

    override fun areItemsTheSame(oldItem: Displayable, newItem: Displayable): Boolean {
        val result = oldItem.id == newItem.id
        return result
    }

    override fun areContentsTheSame(oldItem: Displayable, newItem: Displayable): Boolean {
        val result = when {
            equalsTypes<ContactItemRv>(oldItem, newItem) -> {
                val old = oldItem as ContactItemRv
                val new = newItem as ContactItemRv
                old.hashCode() == new.hashCode()
            }

            else -> false
        }
        return result
    }

    override fun getChangePayload(oldItem: Displayable, newItem: Displayable): Any? {
        val result = when {
            equalsTypes<ContactItemRv>(oldItem, newItem) -> {
                val old = oldItem as ContactItemRv
                val new = newItem as ContactItemRv
                old.isChecked == new.isChecked
            }

            else -> null
        }
        return result
    }

    private inline fun <reified T : Displayable> equalsTypes(
        oldItem: Displayable,
        newItem: Displayable
    ) = oldItem is T && newItem is T

}