package com.bdmendes.todolist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment

typealias NotesListener = (String) -> Unit

class AddNotesDialog(context: Context, currentNotes: String, notesListener: NotesListener) :
    AppCompatDialog(context) {
    private val notes by lazy { findViewById<android.widget.EditText>(R.id.notes) }
    private val cancelButton by lazy { findViewById<android.widget.Button>(R.id.cancel_button) }
    private val addButton by lazy { findViewById<android.widget.Button>(R.id.add_notes_button) }

    init {
        setTitle("Add Notes")
        setContentView(R.layout.add_notes_dialog)

        notes?.setText(currentNotes)

        cancelButton?.setOnClickListener { dismiss() }
        addButton?.setOnClickListener {
            notesListener(notes?.text.toString())
            dismiss()
        }

        window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        with(notes!!) {
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

class AddNotesDialogFragment : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        return AddNotesDialog(
            activity as Context,
            requireArguments().getString("notes") ?: "",
            requireArguments().getSerializable("listener") as NotesListener
        )
    }

    companion object {
        fun newInstance(notes: String?, listener: NotesListener) = AddNotesDialogFragment().apply {
            arguments = Bundle().apply {
                putString("notes", notes ?: "")
                putSerializable("listener", listener as java.io.Serializable)
            }
        }
    }
}