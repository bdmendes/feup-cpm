package com.bdmendes.todolist.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bdmendes.todolist.R
import com.bdmendes.todolist.model.Priority
import com.bdmendes.todolist.model.Todo
import com.bdmendes.todolist.repository.todoRepository
import java.time.LocalDate

class TodoAdapter(context: Context, val todos: List<Todo>) :
    ArrayAdapter<Todo>(context, R.layout.todo_item, todos) {
    private fun setPriorityIcon(view: View, todo: Todo) {
        val iconView = view.findViewById<android.widget.ImageView>(R.id.task_priority)
        when (todo.priority) {
            Priority.HIGH -> iconView.setImageResource(R.drawable.baseline_whatshot_24_red)
            Priority.MEDIUM -> iconView.setImageResource(R.drawable.baseline_whatshot_24_orange)
            Priority.LOW -> iconView.setImageResource(R.drawable.baseline_whatshot_24_blue)
        }
    }

    private fun setupCheckBox(view: View, todo: Todo) {
        with(view.findViewById<android.widget.CheckBox>(R.id.task_completed)) {
            isChecked = todo.dueDate != null
            setOnClickListener {
                todo.doneDate = if (isChecked) {
                    LocalDate.now()
                } else {
                    null
                }
            }
        }
    }

    override fun getView(
        pos: Int,
        view: View?,
        parent: ViewGroup
    ): View {
        val todo = todos[pos]
        val view = view ?: android.view.LayoutInflater.from(context)
            .inflate(R.layout.todo_item, parent, false)

        setPriorityIcon(view, todo)

        view.findViewById<android.widget.TextView>(R.id.task_title).text = todo.title
        view.findViewById<android.widget.TextView>(R.id.task_description).text = todo.description
        view.findViewById<android.widget.TextView>(R.id.task_due_date).text =
            todo.dueDate.toString().substring(0, 10)

        setupCheckBox(view, todo)

        view.findViewById<androidx.appcompat.widget.AppCompatImageView>(R.id.task_edit)
            .setOnClickListener {
                todoRepository.selectedTodo = todo
                println("Selected todo: ${todoRepository.selectedTodo}")
                val intent = android.content.Intent(
                    context,
                    com.bdmendes.todolist.TodoDetailActivity::class.java
                )
                context.startActivity(intent)
            }

        return view
    }
}
