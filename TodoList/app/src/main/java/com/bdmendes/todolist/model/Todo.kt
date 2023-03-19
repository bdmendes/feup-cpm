package com.bdmendes.todolist.model

import java.time.LocalDate

enum class Priority { HIGH, MEDIUM, LOW }

data class Todo(
    var title: String,
    var description: String,
    var priority: Priority = Priority.LOW,
    var notes: String = "",
    var dueDate: LocalDate = LocalDate.now(),
    var doneDate: LocalDate? = null
)