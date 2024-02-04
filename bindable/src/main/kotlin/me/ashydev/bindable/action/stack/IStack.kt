package me.ashydev.bindable.action.stack

import java.io.Serializable

interface IStack<E> : Collection<E>, Iterable<E>, Serializable {
    fun push(element: E): IStack<E>

    fun pop(): E
    fun peek(): E

    fun clear(): IStack<E>

    fun removeAt(index: Int): IStack<E>
    fun remove(element: E): IStack<E>

    fun indexOf(element: E): Int
}