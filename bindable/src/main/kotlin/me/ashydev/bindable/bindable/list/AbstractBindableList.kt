package me.ashydev.bindable.bindable.list

import me.ashydev.bindable.IBindable
import me.ashydev.bindable.action.Action
import me.ashydev.bindable.action.stack.ActionStack
import me.ashydev.bindable.event.ValueChangedEvent
import me.ashydev.bindable.event.collection.CollectionAddEvent
import me.ashydev.bindable.event.collection.CollectionClearEvent
import me.ashydev.bindable.event.collection.CollectionRemoveEvent
import me.ashydev.bindable.event.collection.CollectionSetEvent
import me.ashydev.bindable.list.IBindableList
import me.ashydev.bindable.reference.WeakLockedList
import me.ashydev.bindable.types.ICollectionModify
import me.ashydev.bindable.types.IDisableable
import java.lang.ref.WeakReference

@Suppress("UNCHECKED_CAST")
abstract class AbstractBindableList<T, V : AbstractBindableList<T, V>>(
    collection: Collection<T>? = null,
    private var internalDisabled: Boolean = false
) : IBindableList<T, V> {

    private val collection = collection?.toMutableList() ?: mutableListOf()

    private val collectionAdd: ActionStack<CollectionAddEvent<T, V>> = ActionStack()
    private val collectionSet: ActionStack<CollectionSetEvent<T, V>> = ActionStack()
    private val collectionRemoveEvent: ActionStack<CollectionRemoveEvent<T, V>> = ActionStack()
    private val collectionClearEvent: ActionStack<CollectionClearEvent<T, V>> = ActionStack()

    private val disableChanged: ActionStack<ValueChangedEvent<Boolean>> = ActionStack()

    private var weakReferenceInstance: WeakReference<AbstractBindableList<T, V>>? = null
    private val weakReference: WeakReference<AbstractBindableList<T, V>>
        get() {
            if (weakReferenceInstance == null) {
                weakReferenceInstance = WeakReference(this)
            }

            return weakReferenceInstance!!
        }

    private val bindings: WeakLockedList<AbstractBindableList<T, V>> = WeakLockedList()

    override fun get(index: Int): T = collection[index]

    override fun set(index: Int, element: T): T {
        val last = collection[index]

        setIndex(index, element, HashSet())

        return last
    }

    private fun setIndex(
        index: Int,
        value: T,
        applied: HashSet<V>,
    ): V {
        if (alreadyApplied(applied)) return this as V

        ensureMutationAllowed()

        if (collection[index] == value) return this as V

        val last = collection[index]
        collection[index] = value

        for (binding in bindings)
            binding.get()?.setIndex(index, value, applied)

        return apply {
            collectionSet.execute(CollectionSetEvent(last, value, index, this as V))
        } as V
    }


    override fun add(element: T): Boolean = add(element, HashSet())

    private fun add(element: T, applied: HashSet<V>): Boolean {
        if (alreadyApplied(applied)) return false

        ensureMutationAllowed()

        collection.add(element)

        for (binding in bindings)
            binding.get()?.add(element, applied)


        collectionAdd.execute(CollectionAddEvent(element, size, this as V))

        return true
    }

    override fun indexOf(element: T): Int = collection.indexOf(element)

    override fun clear() = clear(HashSet())

    private fun clear(applied: HashSet<V>) {
        if (alreadyApplied(applied)) return

        ensureMutationAllowed()

        collection.clear()

        for (binding in bindings)
            binding.get()?.clear(applied)

        collectionClearEvent.execute(CollectionClearEvent(this as V))
    }

    override fun remove(element: T): Boolean = remove(element, HashSet())

    private fun remove(element: T, applied: HashSet<V>): Boolean {
        if (alreadyApplied(applied)) return false

        ensureMutationAllowed()

        val index = collection.indexOf(element)

        if (index == -1) return false

        collection.removeAt(index)

        for (binding in bindings)
            binding.get()?.remove(element, applied)

        collectionRemoveEvent.execute(CollectionRemoveEvent(element, index, this as V))

        return true
    }


    override fun contains(element: T): Boolean = collection.contains(element)
    override fun containsAll(elements: Collection<T>): Boolean = collection.containsAll(elements)

    override fun isEmpty(): Boolean = collection.isEmpty()

    override fun iterator(): MutableIterator<T> = collection.iterator()

    override fun lastIndexOf(element: T): Int = collection.lastIndexOf(element)

    override fun add(index: Int, element: T) = add(index, element, HashSet())

    private fun add(index: Int, element: T, applied: HashSet<V>) {
        if (alreadyApplied(applied)) return

        ensureMutationAllowed()

        collection.add(index, element)

        for (binding in bindings)
            binding.get()?.add(index, element, applied)

        collectionAdd.execute(CollectionAddEvent(element, index, this as V))
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean = addAll(index, elements, HashSet())

    private fun addAll(index: Int, elements: Collection<T>, applied: HashSet<V>): Boolean {
        if (alreadyApplied(applied)) return false

        ensureMutationAllowed()

        collection.addAll(index, elements)

        for (binding in bindings)
            binding.get()?.addAll(index, elements, applied)

        for ((i, element) in elements.withIndex())
            collectionAdd.execute(CollectionAddEvent(element, index + i, this as V))

        return true
    }

    override fun addAll(elements: Collection<T>): Boolean = addAll(elements, HashSet())

    private fun addAll(elements: Collection<T>, applied: HashSet<V>): Boolean {
        if (alreadyApplied(applied)) return false

        ensureMutationAllowed()

        collection.addAll(elements)

        for (binding in bindings)
            binding.get()?.addAll(elements, applied)

        for ((i, element) in elements.withIndex())
            collectionAdd.execute(CollectionAddEvent(element, size - elements.size + i, this as V))

        return true
    }

    override fun listIterator(): MutableListIterator<T> = collection.listIterator()
    override fun listIterator(index: Int): MutableListIterator<T> = collection.listIterator(index)

    override fun removeAll(elements: Collection<T>): Boolean = removeAll(elements, HashSet())

    private fun removeAll(elements: Collection<T>, applied: HashSet<V>): Boolean {
        if (alreadyApplied(applied)) return false

        ensureMutationAllowed()

        val removed = collection.removeAll(elements)

        for (binding in bindings)
            binding.get()?.removeAll(elements, applied)

        for (element in elements)
            collectionRemoveEvent.execute(CollectionRemoveEvent(element, -1, this as V))

        return removed
    }

    override fun removeAt(index: Int): T {
        val last = collection[index]

        removeAt(index, HashSet())

        return last
    }

    private fun removeAt(index: Int, applied: HashSet<V>): T {
        if (alreadyApplied(applied)) return collection[index]

        ensureMutationAllowed()

        val last = collection.removeAt(index)

        for (binding in bindings)
            binding.get()?.removeAt(index, applied)

        collectionRemoveEvent.execute(CollectionRemoveEvent(last, index, this as V))

        return last
    }

    override fun retainAll(elements: Collection<T>): Boolean = retainAll(elements, HashSet())

    private fun retainAll(elements: Collection<T>, applied: HashSet<V>): Boolean {
        if (alreadyApplied(applied)) return false

        ensureMutationAllowed()

        val removed = collection.retainAll(elements)

        for (binding in bindings)
            binding.get()?.retainAll(elements, applied)

        for (element in collection)
            if (!elements.contains(element))
                collectionRemoveEvent.execute(CollectionRemoveEvent(element, -1, this as V))

        return removed
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> = collection.subList(fromIndex, toIndex)

    override fun bindTo(bindable: V): V {
        if (bindings.contains(bindable.weakReference))
            throw IllegalArgumentException("Attempted to bind ${javaClass.simpleName} to ${bindable.javaClass.simpleName}, but it was already bound")

        bindable.copyTo(this)

        addBinding(bindable)

        bindable.addBinding(this as V)

        return this
    }

    override fun weakBind(bindable: V): V {
        if (bindings.contains(bindable.weakReference))
            throw IllegalArgumentException("Attempted to bind ${javaClass.simpleName} to ${bindable.javaClass.simpleName}, but it was already bound")

        bindable.copyTo(this)

        bindable.addBinding(this as V)

        return this
    }

    override fun create(): V = BindableList<T>() as V

    override fun copy(): IBindableList<T, V> {
        val copy = create()

        copy.bindTo(this as V)

        return copy
    }

    override fun copyTo(copyable: IBindableList<T, V>): IBindableList<T, V> {
        copyable.clear()

        for (element in collection)
            copyable.add(element)

        copyable.setDisabled(isDisabled())

        return copyable
    }

    override fun getBoundCopy(): V = IBindableList.create(this as V)
    override fun getWeakCopy(): V {
        val copy = create()

        copyTo(copy)
        copy.weakBind(this as V)

        return copy
    }
    override fun getUnboundCopy(): V {
        val copy = create()

        copyTo(copy)

        return copy
    }

    override fun addBinding(reference: V): V {
        val weak = reference.weakReference

        if (bindings.contains(weak))
            throw IllegalArgumentException("Attempted to add a binding to ${reference.javaClass.simpleName}, but it was already bound")

        bindings.add(weak)

        return this as V
    }

    override fun removeBinding(reference: V): V {
        val weak = reference.weakReference

        if (!bindings.contains(weak))
            throw IllegalArgumentException("Attempted to remove a binding from ${reference.javaClass.simpleName}, but it was not bound")

        bindings.remove(weak)

        return this as V
    }

    override fun unbind(): V {
        return apply {
            unbindEvents()
            unbindBindings()
            unbindWeak()
        } as V
    }

    override fun unbindEvents(): V {
        collectionAdd.clear()
        collectionSet.clear()
        collectionRemoveEvent.clear()
        collectionClearEvent.clear()

        return this as V
    }

    override fun unbindBindings(): V {
        for (binding in bindings)
            binding.get()?.removeBinding(this as V)

        bindings.clear()

        return this as V
    }

    override fun unbindWeak(): V {
        for (reference in bindings) {
            if (reference.refersTo(this)) {
                val binding: V = reference.get() as V

                binding.bindings.remove(weakReference)
            } else continue
        }

        return this as V
    }

    override fun unbindFrom(unbindable: V): V {
        return apply {
            removeBinding(unbindable)
            unbindable.removeBinding(this as V)
        } as V
    }

    override fun unbindWeakFrom(unbindable: V): V {
        return apply {
            unbindable.removeBinding(this as V)
        } as V
    }

    override fun getCollectionAdd(): ActionStack<CollectionAddEvent<T, V>> = collectionAdd
    override fun getCollectionRemove(): ActionStack<CollectionRemoveEvent<T, V>> = collectionRemoveEvent
    override fun getCollectionSet(): ActionStack<CollectionSetEvent<T, V>> = collectionSet
    override fun getCollectionClear(): ActionStack<CollectionClearEvent<T, V>> = collectionClearEvent

    override fun onCollectionAdd(
        action: Action<CollectionAddEvent<T, V>>,
        runOnceImmediately: Boolean
    ): ICollectionModify<T, V> {
        collectionAdd.push(action)

        if (runOnceImmediately) {
            action.invoke(CollectionAddEvent(collection.last(), size - 1, this as V))
        }

        return this
    }

    override fun onCollectionRemove(
        action: Action<CollectionRemoveEvent<T, V>>,
        runOnceImmediately: Boolean
    ): ICollectionModify<T, V> {
        collectionRemoveEvent.push(action)

        if (runOnceImmediately) {
            action.invoke(CollectionRemoveEvent(collection.last(), size - 1, this as V))
        }

        return this
    }

    override fun onCollectionSet(
        action: Action<CollectionSetEvent<T, V>>,
        runOnceImmediately: Boolean
    ): ICollectionModify<T, V> {
        collectionSet.push(action)

        if (runOnceImmediately) {
            action.invoke(CollectionSetEvent(collection.last(), collection.last(), size - 1, this as V))
        }

        return this
    }

    override fun onCollectionClear(
        action: Action<CollectionClearEvent<T, V>>,
        runOnceImmediately: Boolean
    ): ICollectionModify<T, V> {
        collectionClearEvent.push(action)

        if (runOnceImmediately) {
            action.invoke(CollectionClearEvent(this as V))
        }

        return this
    }

    override fun getDisableChanged(): ActionStack<ValueChangedEvent<Boolean>> = disableChanged

    override fun onDisableChanged(
        block: Action<ValueChangedEvent<Boolean>>,
        runOnceImmediately: Boolean
    ): IDisableable {
        disableChanged.push(block)

        if (runOnceImmediately) {
            block.invoke(ValueChangedEvent(internalDisabled, internalDisabled))
        }

        return this
    }

    private fun setDisabled(disabled: Boolean, bypassChecks: Boolean = false, source: AbstractBindableList<T, V>? = null) {

        internalDisabled = disabled
        triggerDisabledChange(source ?: this, true, bypassChecks)
    }

    private fun triggerDisabledChange(source: AbstractBindableList<T, V>, propagate: Boolean = true, bypassChecks: Boolean = false) {
        val old = internalDisabled

        if (propagate)
            propagateDisabledChange(source, internalDisabled, bypassChecks, this)

        if (old != internalDisabled)
            disableChanged.execute(ValueChangedEvent(old, internalDisabled))
    }

    private fun propagateDisabledChange(source: AbstractBindableList<T, V>, propagate: Boolean, bypassChecks: Boolean, bindable: AbstractBindableList<T, V>) {
        for (binding in bindings) {
            if (binding == source) continue

            binding.get()?.setDisabled(propagate, bypassChecks, bindable)
        }
    }

    override fun setDisabled(disabled: Boolean): IDisableable = apply { setDisabled(disabled, false, null) }
    override fun isDisabled(): Boolean = internalDisabled
    
    private fun ensureMutationAllowed() {
        if (isDisabled())
            throw IllegalStateException("Cannot modify the collection ${javaClass.simpleName} while it is disabled")
    }

    private fun alreadyApplied(applied: HashSet<V>): Boolean {
        if (applied.contains(this as V)) return true

        applied.add(this)
        return false
    }

    override val size: Int = collection?.size ?: -1

    override fun toString(): String = collection.toString()
}