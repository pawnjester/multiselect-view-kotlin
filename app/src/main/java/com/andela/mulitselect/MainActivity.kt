package com.andela.mulitselect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.andela.mulitselect.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var actionCallback: ActionCallback

    private var actionMode: ActionMode? = null

    private var listAdapter: InboxAdapter ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Inbox"
        actionCallback = ActionCallback()
        listAdapter = InboxAdapter({ item, view, position ->
            if (listAdapter?.selectedItemsCount()!! > 0) {
                toggleActionBar(position)
            } else {
                Toast.makeText(this, "clicked this", Toast.LENGTH_SHORT).show()
            }
        }, { item, view, position ->
            toggleActionBar(position)
            true
        })
        with(binding.emailList) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = listAdapter
        }

        displayInboxItems(Util.getInboxData(this))
    }

    fun displayInboxItems(list: List<Inbox>) {
        listAdapter?.updateItems(list)
    }

    fun deleteBox () {
        val selectedItems = listAdapter?.getSelectedItems()

        for (i in selectedItems?.size!!.minus(1) downTo 0) {
            listAdapter?.removeItem(selectedItems.get(i))
        }
        listAdapter?.notifyDataSetChanged()
    }

    fun toggleActionBar(position: Int) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionCallback)
        }

        toggleSelection(position)
    }

    private fun toggleSelection(position: Int) {
        listAdapter?.toggleSelection(position)
        val count = listAdapter?.selectedItemsCount()
        if (count == 0 ) {
            actionMode?.finish()
        } else {
            actionMode?.title = count.toString()
            actionMode?.invalidate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    inner class ActionCallback : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when(item?.itemId) {
                R.id.delteItem -> {
                    deleteBox()
                    mode?.finish()
                    return true
                }
            }
            return false
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            Util.toggleStatusBarColor(this@MainActivity, R.color.blue_grey_700)
            mode?.menuInflater?.inflate(R.menu.menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

        override fun onDestroyActionMode(mode: ActionMode?) {
            listAdapter?.clearSelection()
            actionMode = null
            Util.toggleStatusBarColor(this@MainActivity, R.color.colorPrimary)
        }

    }
}
