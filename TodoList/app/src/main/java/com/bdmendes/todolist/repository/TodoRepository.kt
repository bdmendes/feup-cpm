package com.bdmendes.todolist.repository

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteOpenHelper

class TodoRepository private constructor(context: android.content.Context) :
    SQLiteOpenHelper(context, "todo.db", null, 1) {
    companion object {
        private var instance: TodoRepository? = null
        fun getInstance(context: android.content.Context): TodoRepository {
            if (instance == null) {
                instance = TodoRepository(context)
                instance!!.todos = instance!!.getAllFromDb()
                println("todos: ${instance!!.todos}")
            }
            return instance!!
        }
    }

    var selectedTodo: com.bdmendes.todolist.model.Todo? = null
    private var todos: MutableList<com.bdmendes.todolist.model.Todo> = arrayListOf()

    override fun onCreate(db: android.database.sqlite.SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS todo (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "description TEXT," +
                    "priority INTEGER NOT NULL," +
                    "due_date TEXT," +
                    "notes TEXT," +
                    "done_date TEXT" +
                    ")"
        )
    }

    override fun onUpgrade(
        db: android.database.sqlite.SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS todo")
        onCreate(db)
    }

    fun add(todo: com.bdmendes.todolist.model.Todo) {
        val db = writableDatabase

        val values = android.content.ContentValues()
        values.put("title", todo.title)
        values.put("description", todo.description)
        values.put("priority", todo.priority.ordinal)
        values.put("due_date", todo.dueDate.toString())
        values.put("notes", todo.notes)
        values.put("done_date", todo.doneDate?.toString())

        val id = db.insert("todo", null, values)
        todo.id = id

        println(getAllFromDb())

        db.close()

        todos.add(todo)
    }

    fun update(todo: com.bdmendes.todolist.model.Todo) {
        val db = writableDatabase
        val values = android.content.ContentValues()
        values.put("title", todo.title)
        values.put("description", todo.description)
        values.put("priority", todo.priority.ordinal)
        values.put("due_date", todo.dueDate.toString())
        values.put("notes", todo.notes)
        values.put("done_date", todo.doneDate?.toString())
        db.update("todo", values, "id = ?", arrayOf(todo.id.toString()))
        db.close()

        todos.removeIf { it.id == todo.id }
        todos.add(todo)
    }

    fun delete(todo: com.bdmendes.todolist.model.Todo) {
        val db = writableDatabase
        db.delete("todo", "id = ?", arrayOf(todo.id.toString()))
        db.close()

        todos.removeIf { it.id == todo.id }
    }

    @SuppressLint("Range")
    private fun getAllFromDb(): MutableList<com.bdmendes.todolist.model.Todo> {
        val todos = mutableListOf<com.bdmendes.todolist.model.Todo>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM todo", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex("id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val description = cursor.getString(cursor.getColumnIndex("description"))
                val priority = com.bdmendes.todolist.model.Priority.values()[cursor.getInt(
                    cursor.getColumnIndex("priority")
                )]
                val dueDate =
                    java.time.LocalDate.parse(cursor.getString(cursor.getColumnIndex("due_date")))
                val notes = cursor.getString(cursor.getColumnIndex("notes"))
                val doneDate = cursor.getString(cursor.getColumnIndex("done_date"))
                    ?.let { java.time.LocalDate.parse(it) }
                todos.add(
                    com.bdmendes.todolist.model.Todo(
                        id,
                        title,
                        description,
                        priority,
                        notes,
                        dueDate,
                        doneDate
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todos
    }

    fun getAll(): List<com.bdmendes.todolist.model.Todo> {
        return todos
    }
}