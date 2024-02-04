package me.ashydev.bindable.action.stack

interface IExecutionStack<T> {
    fun execute(value: T): IExecutionStack<T>
    fun executeOne(value: T): IExecutionStack<T>
}