package com.bdmendes.todolist

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bdmendes.todolist.model.Todo
import com.bdmendes.todolist.repository.TodoRepository
import com.bdmendes.todolist.view.TodoAdapter

class MainActivity : AppCompatActivity() {
    private val todoListView by lazy { findViewById<android.widget.ListView>(R.id.task_list) }
    private val emptyMessage by lazy { findViewById<android.widget.TextView>(R.id.empty_message) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.action_new_task -> {
                AddTaskDialogFragment.newInstance { taskName, dueDate ->
                    val todoRepository = TodoRepository.getInstance(this)
                    val todo = Todo(
                        title = taskName,
                        description = "",
                        dueDate = dueDate
                    )
                    todoRepository.add(todo)
                    val adapter = todoListView.adapter as TodoAdapter
                    adapter.notifyDataSetChanged()
                }.show(supportFragmentManager, "AddTaskDialogFragment")
            }
        }
        return true
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        super.onContextItemSelected(item)
        val todoRepository = TodoRepository.getInstance(this)
        when (item.itemId) {
            R.id.todo_context_add_notes -> {
                AddNotesDialogFragment.newInstance(todoRepository.selectedTodo?.notes) { notes ->
                    todoRepository.selectedTodo?.notes = notes
                    todoRepository.update(todoRepository.selectedTodo!!)
                    val adapter = todoListView.adapter as TodoAdapter
                    adapter.notifyDataSetChanged()
                }.show(supportFragmentManager, "AddNotesDialogFragment")
            }
            R.id.todo_context_remove_task -> {
                todoRepository.delete(todoRepository.selectedTodo!!)
                val adapter = todoListView.adapter as TodoAdapter
                adapter.notifyDataSetChanged()
            }
        }
        return true
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.listview_context, menu)
    }

    override fun onStart() {
        super.onStart()
        registerForContextMenu(todoListView)
        val todoRepository = TodoRepository.getInstance(this)
        todoListView.run {
            emptyView = emptyMessage
            adapter = TodoAdapter(this@MainActivity, todoRepository.getAll())
            setOnItemLongClickListener { _, _, pos, _ ->
                todoRepository.selectedTodo = todoRepository.getAll()[pos]
                false
            }
        }
    }
}