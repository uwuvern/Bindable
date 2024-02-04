package me.ashydev.bindable.types

interface ICopyable<T : ICopyable<T>> {
    fun copy(): T
    fun copyTo(copyable: T): T
}