package com.example.to_docompose.util

enum class ActionLabels {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION
}

fun String?.toAction(): ActionLabels {
    return if (this.isNullOrEmpty()) ActionLabels.NO_ACTION else ActionLabels.valueOf(this)
}