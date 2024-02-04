package me.ashydev.bindable.bindable

import me.ashydev.bindable.IBindable
import me.ashydev.bindable.ILeasedBindable
import me.ashydev.bindable.action.Action
import me.ashydev.bindable.action.stack.ActionStack
import me.ashydev.bindable.event.ValueChangedEvent
import me.ashydev.bindable.event.lease.LeaseUpdateEvent
import me.ashydev.bindable.reference.WeakLockedList
import me.ashydev.bindable.types.IDisableable
import me.ashydev.bindable.types.IHasDefault
import java.lang.ref.WeakReference

@Suppress("UNCHECKED_CAST")
abstract class AbstractBindable<T, V : AbstractBindable<T, V>>(
    protected var internalValue: T,
    protected var internalDefault: T,
    protected var internalDisabled: Boolean = false
) : IBindable<T, V> {

    private val valueChanged: ActionStack<ValueChangedEvent<T>> = ActionStack()
    private val disabledChanged: ActionStack<ValueChangedEvent<Boolean>> = ActionStack()
    private val defaultChanged: ActionStack<ValueChangedEvent<T>> = ActionStack()

    private val leased: ActionStack<LeaseUpdateEvent> = ActionStack()

    private var weakReferenceInstance: WeakReference<AbstractBindable<T, V>>? = null
    protected val weakReference: WeakReference<AbstractBindable<T, V>>
        get() {
            if (weakReferenceInstance == null) {
                weakReferenceInstance = WeakReference(this)
            }

            return weakReferenceInstance!!
        }

    private val bindings: WeakLockedList<AbstractBindable<T, V>> = WeakLockedList()

    constructor(defaultValue: T) : this(defaultValue, defaultValue)
    constructor(value: T, defaultValue: T) : this(value, defaultValue, false)
    constructor() : this(null as T, null as T)
    
    override fun get(): T = internalValue

    override fun set(value: T): V {
        if (isDisabled())
            throw IllegalStateException("Cannot set value to ${value.toString()} on a disabled ${javaClass.simpleName}")

        if (value == this.internalValue) return this as V

        return apply {
            setValue(this.internalValue, value)
        } as V
    }

    internal fun setValue(previous: T, new: T, bypassChecks: Boolean = false, source: V? = null) {
        internalValue = new
        triggerValueChanged(previous, new, true, bypassChecks, source ?: this as V)
    }

    private fun triggerValueChanged(previous: T, new: T, propagate: Boolean, bypassChecks: Boolean, source: V) {
        if (propagate)
            propagateValueChanged(previous, new, bypassChecks, source)

        if (previous != new)
            valueChanged.execute(ValueChangedEvent(previous, new))
    }

    private fun propagateValueChanged(previous: T, new: T, bypassChecks: Boolean, source: AbstractBindable<T, V>) {
        for (binding in bindings) {
            if (binding.refersTo(source)) continue

            binding.get()?.setValue(previous, new, bypassChecks, source as V)
        }
    }

    override fun onValueChanged(
        action: Action<ValueChangedEvent<T>>,
        runOnceImmediately: Boolean
    ): AbstractBindable<T, V> {
        valueChanged.push(action)

        if (runOnceImmediately) {
            action.invoke(ValueChangedEvent(internalValue, internalValue))
        }

        return this
    }

    override fun setDefault(): V {
        return set(getDefaultValue())
    }
    override fun getDefaultValue(): T = internalDefault
    override fun isDefault(): Boolean = internalValue?.equals(internalDefault) ?: false

    override fun setDefaultValue(default: T): IHasDefault<T> =
        updateDefault(internalDefault, default)

    internal fun updateDefault(old: T, default: T, bypassChecks: Boolean = false, source: V? = null): V {
        internalDefault = default

        triggerDefaultChanged(old, source ?: this as V, true, bypassChecks)

        return this as V
    }

    private fun triggerDefaultChanged(old: T, source: V, propagate: Boolean = true, bypassChecks: Boolean = false) {
        if (propagate)
            propagateDefaultChanged(source, old, internalDefault, bypassChecks)

        if (old != internalDefault)
            defaultChanged.execute(ValueChangedEvent(old, internalDefault))
    }

    private fun propagateDefaultChanged(source: V, old: T, value: T, bypassChecks: Boolean) {
        for (binding in bindings) {
            if (binding.refersTo(source)) continue

            binding.get()?.updateDefault(old, value, bypassChecks, source)
        }
    }

    override fun onDefaultChanged(
        action: Action<ValueChangedEvent<T>>,
        runOnceImmediately: Boolean
    ) : AbstractBindable<T, V> {
        defaultChanged.push(action)

        if (runOnceImmediately) {
            action.invoke(ValueChangedEvent(internalDefault, internalDefault))
        }

        return this
    }

    internal fun setDisabled(disabled: Boolean, bypassChecks: Boolean = false, source: V? = null) {
        if (!bypassChecks) throwIfLeased()

        val old = internalDisabled

        internalDisabled = disabled

        triggerDisabledChange(old, source ?: this as V, true, bypassChecks)
    }

    private fun triggerDisabledChange(old: Boolean, source: V, propagate: Boolean = true, bypassChecks: Boolean = false) {
        if (propagate)
            propagateDisabledChange(source, internalDisabled, bypassChecks, this as V)

        if (old != internalDisabled)
            disabledChanged.execute(ValueChangedEvent(old, internalDisabled))
    }

    private fun propagateDisabledChange(source: V, propagate: Boolean, bypassChecks: Boolean, bindable: V) {
        for (binding in bindings) {
            if (binding.refersTo(source)) continue

            binding.get()?.setDisabled(propagate, bypassChecks, bindable)
        }
    }

    override fun setDisabled(disabled: Boolean): IDisableable = apply { setDisabled(disabled, false, null) }
    override fun isDisabled(): Boolean = internalDisabled

    override fun onDisableChanged(
        block: Action<ValueChangedEvent<Boolean>>,
        runOnceImmediately: Boolean
    ): IDisableable {
        disabledChanged.push(block)

        if (runOnceImmediately) {
            block.invoke(ValueChangedEvent(internalDisabled, internalDisabled))
        }

        return this
    }

    override fun unbindEvents(): V {
        return apply {
            valueChanged.clear()
            defaultChanged.clear()
            disabledChanged.clear()
        } as V
    }

    override fun unbindBindings(): V {
        for (reference in bindings.toList()) {
            print("${reference.get()}, $this")
            unbindFrom(reference.get() as V)
        }

        bindings.clear()

        return this as V
    }
    override fun unbindWeak(): V {
        for (reference in bindings.toList()) {
            reference.get()?.unbindWeakFrom(this as V)
        }

        return this as V
    }

    override fun unbind(): V = internalUnbind()

    protected fun internalUnbind(): V {
        if (isLeased()) leasedBindable?.lease()

        return apply {
            unbindBindings()
            unbindWeak()
            unbindEvents()
        } as V
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

    override fun getBoundCopy(): V = IBindable.create(this as V)
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

    override fun create(): V = Bindable<T>() as V

    override fun copy(): V {
        val bindable = create()

        bindable.bindTo(this as V)

        return bindable
    }

    override fun copyTo(copyable: IBindable<T, V>): V {
        copyable.value = value
        copyable.default = default
        copyable.disabled = disabled

        return copyable as V
    }

    private var leasedBindable: ILeasedBindable<T, V>? = null
    fun isLeased(): Boolean = leasedBindable != null

    override fun beginLease(revertOnReturn: Boolean): LeasedBindable<T> {
        leased.execute(LeaseUpdateEvent(LeaseUpdateEvent.UpdateType.BEGIN))

        if (checkForLease(this as V))
            throw IllegalStateException("Attempted to begin a lease on a ${javaClass.simpleName} that is already leased")

        leasedBindable = LeasedBindable(this as Bindable<T>, revertOnReturn) as ILeasedBindable<T, V>

        return leasedBindable as LeasedBindable<T>
    }

    private fun checkForLease(source: V): Boolean {
        if (isLeased()) return true

        var found = false

        for (binding in bindings) {
            if (!binding.refersTo(source)) {
                found = found || binding.get()?.checkForLease(this as V) ?: false
            }
        }

        return found
    }

    override fun endLease(bindable: LeasedBindable<T>): V {
        if (!isLeased())
            throw IllegalStateException("Attempted to end a lease on a ${javaClass.simpleName} that is not leased")

        if (bindable != leasedBindable)
            throw IllegalArgumentException("Attempted to end a lease on a ${javaClass.simpleName} with a bindable that is not the current lease")

        leasedBindable = null

        leased.execute(LeaseUpdateEvent(LeaseUpdateEvent.UpdateType.END))
        return this as V
    }

    override fun onLeaseChange(action: Action<LeaseUpdateEvent>, runOnceImmediately: Boolean): V {
        leased.push(action)

        if (runOnceImmediately) {
            action.invoke(LeaseUpdateEvent(LeaseUpdateEvent.UpdateType.NONE))
        }

        return this as V
    }

    override fun getLeased(): ActionStack<LeaseUpdateEvent> = leased

    private fun throwIfLeased() {
        if (isLeased()) throw IllegalStateException("Cannot perform an operation on a ${javaClass.simpleName} that is in a leased state")
    }

    override fun getDefaultChanged(): ActionStack<ValueChangedEvent<T>> = defaultChanged
    override fun getDisableChanged(): ActionStack<ValueChangedEvent<Boolean>> = disabledChanged
    override fun getValueChanged(): ActionStack<ValueChangedEvent<T>> = valueChanged


    final override fun bindTo(bindable: V): V {
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

    override fun toString(): String {
        return "${javaClass.simpleName}:{value=${value}, default=${default}, disabled=${disabled}, leased=${isLeased()}}"
    }
}