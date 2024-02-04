package me.ashydev.bindable.action.stack

import me.ashydev.bindable.action.Action

class ActionStack<T>(vararg actions: Action<T> = emptyArray()) : IActionStack<T> {
    private var actions: MutableList<Action<T>> = mutableListOf()
    override val size: Int
        get() = actions.size

    init {
        for (action in actions) {
            this.actions.add(action)
        }
    }

    override fun contains(element: Action<T>): Boolean = actions.contains(element)
    override fun containsAll(elements: Collection<Action<T>>): Boolean = actions.containsAll(elements)


    override fun pop(): Action<T> = actions.removeAt(actions.size - 1)
    override fun peek(): Action<T> = actions[actions.size - 1]

    override fun clear(): IStack<Action<T>> = apply { actions.clear() }

    override fun removeAt(index: Int): IStack<Action<T>> = apply { actions.removeAt(index) }
    override fun indexOf(element: Action<T>): Int = actions.indexOf(element)
    override fun remove(element: Action<T>): IStack<Action<T>> = apply { actions.remove(element) }

    override fun push(element: Action<T>): IStack<Action<T>> = apply { actions.add(element) }

    override fun execute(value: T): IExecutionStack<T> = apply {
        for (action in actions) {
            action.invoke(value)
        }
    }

    override fun executeOne(value: T): IExecutionStack<T> = apply {
        actions[actions.size - 1].invoke(value)
    }

    override fun isEmpty(): Boolean = actions.isEmpty()

    override fun iterator(): Iterator<Action<T>> = actions.iterator()

    override fun asList(): List<Action<T>> = actions.toList()
}