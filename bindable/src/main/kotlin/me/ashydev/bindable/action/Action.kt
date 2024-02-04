package me.ashydev.bindable.action

fun interface Action<T> {
    fun invoke(value: T)
}