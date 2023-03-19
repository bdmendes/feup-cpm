package com.bdmendes.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate

class TodoDetailActivity : AppCompatActivity() {
    private val title by lazy { findViewById<android.widget.EditText>(R.id.title) }
    private val description by lazy { findViewById<android.widget.EditText>(R.id.description) }
    private val priority by lazy { findViewById<android.widget.RadioGroup>(R.id.priority) }
    private val dueDate by lazy { findViewById<android.widget.EditText>(R.id.due_date) }
    private val done by lazy { findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.done) }
    private val doneDate by lazy { findViewById<android.widget.EditText>(R.id.done_date) }
    private val notes by lazy { findViewById<android.widget.EditText>(R.id.notes) }
    private val save by lazy { findViewById<android.widget.Button>(R.id.save) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)

        val todoRepository = com.bdmendes.todolist.repository.TodoRepository.getInstance(this)
        val todo = todoRepository.selectedTodo!!.copy()
        println("TodoDetailActivity.onCreate: $todo")

        title.setText(todo.title)
        description.setText(todo.description)
        priority.check(
            when (todo.priority) {
                com.bdmendes.todolist.model.Priority.HIGH -> R.id.high
                com.bdmendes.todolist.model.Priority.MEDIUM -> R.id.medium
                com.bdmendes.todolist.model.Priority.LOW -> R.id.low
            }
        )
        dueDate.setText(todo.dueDate.toString())
        done.isChecked = todo.doneDate != null
        doneDate.setText(todo.doneDate?.toString() ?: LocalDate.now().toString())
        notes.setText(todo.notes)

        save.setOnClickListener {
            todo.title = title.text.toString()
            todo.description = description.text.toString()
            todo.priority =
                com.bdmendes.todolist.model.Priority.values()[priority.indexOfChild(
                    priority.findViewById(priority.checkedRadioButtonId)
                )]
            todo.dueDate = java.time.LocalDate.parse(dueDate.text.toString())
            todo.doneDate =
                if (done.isChecked) java.time.LocalDate.parse(doneDate.text.toString()) else null
            todo.notes = notes.text.toString()

            todoRepository.update(todo)

            finish()
        }
    }
}