/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.reference

import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.util.*
import java.util.function.Consumer
import java.util.function.IntFunction
import java.util.function.Predicate
import java.util.function.UnaryOperator
import java.util.stream.Stream

class WeakLockedList<T> : ArrayList<WeakReference<T>> {
    constructor(initialCapacity: Int) : super(initialCapacity)
    constructor() : super()
    constructor(c: MutableCollection<out WeakReference<T>>) : super(c)

    override fun get(index: Int): WeakReference<T> {
        synchronized(this) {
            return super.get(index)
        }
    }

    override fun set(index: Int, element: WeakReference<T>): WeakReference<T> { 
        synchronized(this) {
            return super.set(index, element)
        }
    }


    override fun add(element: WeakReference<T>): Boolean {
        synchronized(this) {
            return super.add(element)
        }
    }

    override fun add(index: Int, element: WeakReference<T>) { 
        synchronized(this) {
            super.add(index, element)
        }
    }

    override fun addAll(elements: Collection<WeakReference<T>>): Boolean {
        synchronized(this) {
            return super.addAll(elements)
        }
    }

    override fun addAll(index: Int, elements: Collection<WeakReference<T>>): Boolean {
        synchronized(this) {
            return super.addAll(index, elements)
        }
    }

    override fun clear() {
        synchronized(this) {
            super.clear()
        }
    }

    override fun iterator(): MutableIterator<WeakReference<T>> {
        synchronized(this) {
            return super.iterator()
        }
    }

    override fun remove(element: WeakReference<T>): Boolean {
        synchronized(this) {
            return super.remove(element)
        }
    }

    override fun removeAll(elements: Collection<WeakReference<T>>): Boolean {
        synchronized(this) {
            return super.removeAll(elements.toSet())
        }
    }

    override fun retainAll(elements: Collection<WeakReference<T>>): Boolean {
        synchronized(this) {
            return super.retainAll(elements)
        }
    }

    override fun contains(element: WeakReference<T>): Boolean {
        synchronized(this) {
            return super.contains(element)
        }
    }

    override fun containsAll(elements: Collection<WeakReference<T>>): Boolean {
        synchronized(this) {
            return super.containsAll(elements)
        }
    }

    override fun isEmpty(): Boolean {
        synchronized(this) {
            return super.isEmpty()
        }
    }

    override fun equals(other: Any?): Boolean {
        synchronized(this) {
            return super.equals(other)
        }
    }

    override fun hashCode(): Int {
        synchronized(this) {
            return super.hashCode()
        }
    }

    override fun toString(): String {
        synchronized(this) {
            return super.toString()
        }
    }

    override fun forEach(action: Consumer<in WeakReference<T>>?) {
        synchronized(this) {
            super.forEach(action)
        }
    }

    override fun spliterator(): Spliterator<WeakReference<T>> {
        synchronized(this) {
            return super.spliterator()
        }
    }

    override fun toArray(): Array<Any> {
        synchronized(this) {
            return super.toArray()
        }
    }

    override fun <T : Any?> toArray(a: Array<out T>): Array<T> {
        synchronized(this) {
            return super.toArray(a)
        }
    }

    override fun <T : Any?> toArray(generator: IntFunction<Array<T>>?): Array<T> {
        synchronized(this) {
            return super.toArray(generator)
        }
    }

    override fun removeIf(filter: Predicate<in WeakReference<T>>): Boolean {
        synchronized(this) {
            return super.removeIf(filter)
        }
    }

    override fun stream(): Stream<WeakReference<T>> {
        synchronized(this) {
            return super.stream()
        }
    }

    override fun parallelStream(): Stream<WeakReference<T>> {
        synchronized(this) {
            return super.parallelStream()
        }
    }

    override fun listIterator(index: Int): MutableListIterator<WeakReference<T>> {
        synchronized(this) {
            return super.listIterator(index)
        }
    }

    override fun listIterator(): MutableListIterator<WeakReference<T>> {
        synchronized(this) {
            return super.listIterator()
        }
    }

    override fun removeAt(index: Int): WeakReference<T> {
        synchronized(this) {
            return super.removeAt(index)
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<WeakReference<T>> {
        synchronized(this) {
            return super.subList(fromIndex, toIndex)
        }
    }

    override fun indexOf(element: WeakReference<T>): Int {
        synchronized(this) {
            return super.indexOf(element)
        }
    }

    override fun lastIndexOf(element: WeakReference<T>): Int {
        synchronized(this) {
            return super.lastIndexOf(element)
        }
    }

    override fun replaceAll(operator: UnaryOperator<WeakReference<T>>) {
        synchronized(this) {
            super.replaceAll(operator)
        }
    }

    override fun sort(c: Comparator<in WeakReference<T>>?) {
        synchronized(this) {
            super.sort(c)
        }
    }

    override fun removeRange(fromIndex: Int, toIndex: Int) {
        synchronized(this) {
            super.removeRange(fromIndex, toIndex)
        }
    }

    override fun clone(): Any {
        synchronized(this) {
            return super.clone()
        }
    }

    override fun trimToSize() {
        synchronized(this) {
            super.trimToSize()
        }
    }

    override fun ensureCapacity(minCapacity: Int) {
        synchronized(this) {
            super.ensureCapacity(minCapacity)
        }
    }

    override val size: Int
        get() = super.size
}