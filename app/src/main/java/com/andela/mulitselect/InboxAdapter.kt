package com.andela.mulitselect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.andela.mulitselect.databinding.ListItemBinding
import android.util.SparseBooleanArray



class InboxAdapter(val onClick: (item: Inbox, view: View, position: Int) -> Unit,
                   val onLongClick: (item: Inbox, view: View, position: Int) -> Boolean
                   ):
    RecyclerView.Adapter<InboxAdapter.InboxViewHolder>() {

    private val inboxItem = mutableListOf<Inbox>()
    private val selectedItems= SparseBooleanArray()
    private var selectedIndex = -1

    fun updateItems(list: List<Inbox>) {
        inboxItem.clear()
        inboxItem.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = DataBindingUtil
            .inflate<ListItemBinding>(inflater,
                R.layout.list_item, parent, false)
        return InboxViewHolder(binding)
    }

    override fun getItemCount(): Int = inboxItem.size

    override fun onBindViewHolder(holder: InboxViewHolder, position: Int) {
        val items = inboxItem[position]
        holder.bindItems(
            createLongClick(items, position),
            createClick(items, position),
            items)
        holder.binding.lytParent.isActivated = false
        toggleIcon(holder.binding, position)
    }

    inner class InboxViewHolder(val binding: ListItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bindItems(listener: View.OnLongClickListener, click: View.OnClickListener,
                      items: Inbox) {
            binding.items = items
            binding.longclick = listener
            binding.clickListener = click
            binding.executePendingBindings()
        }
    }

    private fun createLongClick(item: Inbox, position: Int): View.OnLongClickListener {
        return View.OnLongClickListener {
             onLongClick(item, it, position)
        }
    }

    private fun createClick(item: Inbox, position: Int): View.OnClickListener {
        return View.OnClickListener {
             onClick(item, it, position)
        }
    }

    fun getSelectedItems(): List<Int> {
        val items: ArrayList<Int> = ArrayList(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }

    fun selectedItemsCount() = selectedItems.size()

    fun removeItem(position: Int) {
        inboxItem.removeAt(position)
        selectedIndex = -1
    }

    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    fun toggleSelection(position: Int) {
        selectedIndex = position
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    fun toggleIcon(binding: ListItemBinding, position: Int) {
        if (selectedItems.get(position, false)) {
            binding.lytImage.visibility = View.GONE
            binding.lytChecked.visibility = View.VISIBLE
            if (selectedIndex == position) selectedIndex = -1
        } else {
            binding.lytImage.visibility = View.VISIBLE
            binding.lytChecked.visibility = View.GONE
            if (selectedIndex == position) selectedIndex = -1;
        }
    }
}