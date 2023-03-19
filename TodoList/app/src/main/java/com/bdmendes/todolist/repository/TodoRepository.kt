package com.bdmendes.todolist.repository

import com.bdmendes.todolist.model.Todo

data class TodoRepository(
    var todos: ArrayList<Todo> = ArrayList(),
    var selectedTodo: Todo? = null
)

val todoRepository =
    TodoRepository(
        arrayListOf(
            Todo("Task 1", "Description 1"),
            Todo("Task 2", "Description 2")
        )
    )