package me.ashydev.bindable.action.stack

import me.ashydev.bindable.action.Action

interface IActionStack<T> : IExecutionStack<T>, IStack<Action<T>> {
    fun asList(): List<Action<T>>
}