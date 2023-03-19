package com.bdmendes.todolist;

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import java.time.LocalDate

typealias AddTaskListener = (String, LocalDate) -> Unit

class AddTaskDialog(context: Context, private val listener: AddTaskListener) :
    AppCompatDialog(context) {
    private val taskName by lazy { findViewById<android.widget.EditText>(R.id.task_name) }
    private val dueDate by lazy { findViewById<android.widget.EditText>(R.id.due_date) }
    private val cancelButton by lazy { findViewById<android.widget.Button>(R.id.cancel_button) }
    private val addButton by lazy { findViewById<android.widget.Button>(R.id.add_task_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Add Task")
        setContentView(R.layout.add_task_dialog)

        cancelButton?.setOnClickListener { dismiss() }
        addButton?.setOnClickListener {
            listener(taskName?.text.toString(), LocalDate.parse(dueDate?.text.toString()))
            dismiss()
        }

        window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        with(dueDate!!) {
            setText(LocalDate.now().toString())
            setOnClickListener {
                val datePicker = android.app.DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        setText(LocalDate.of(year, month + 1, dayOfMonth).toString())
                    },
                    LocalDate.now().year,
                    LocalDate.now().monthValue - 1,
                    LocalDate.now().dayOfMonth
                )
                datePicker.show()
            }
        }

        with(taskName!!) {
            requestFocus()
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                    addButton?.performClick()
                    true
                } else {
                    false
                }
            }
        }
    }
}

class AddTaskDialogFragment : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        return AddTaskDialog(
            activity as Context,
            requireArguments().getSerializable("listener") as AddTaskListener
        )
    }

    companion object {
        fun newInstance(listener: AddTaskListener) = AddTaskDialogFragment().apply {
            arguments = Bundle().apply {
                putSerializable("listener", listener as java.io.Serializable)
            }
        }
    }
}