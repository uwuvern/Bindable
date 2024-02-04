/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.bindable

import me.ashydev.bindable.IBindable
import me.ashydev.bindable.ILeasedBindable
import me.ashydev.bindable.action.stack.ActionStack

class LeasedBindable<T> : Bindable<T>, ILeasedBindable<T, Bindable<T>> {
    private var source: Bindable<T>? = null
    private var revertOnReturn: Boolean = true

    override var value: T
        get() = super.value
        set(value) {
            if (source != null) checkValid()

            if (internalValue?.equals(value) == true) return

            setValue(internalValue, value, true)
        }

    override var default: T
        get() = super.default
        set(value) {
            if (source != null) checkValid()

            if (internalDefault?.equals(value) == true) return

            updateDefault(internalDefault, value, true)
        }

    override var disabled: Boolean
        get() = super.disabled
        set(value) {
            if (source != null) checkValid()

            if (internalDisabled == value) return

            setDisabled(value, true)
        }


    private var valueBeforeLease: T? = null
    private var disabledBeforeLease: Boolean = false

    constructor(source: Bindable<T>, revertOnReturn: Boolean = true) {
        bindTo(source)

        this.source = source

        if (revertOnReturn) {
            this.revertOnReturn = true;
            valueBeforeLease = value
        }

        disabledBeforeLease = disabled

        disabled = true
    }

    private constructor() : super()

    private var hasReturned: Boolean = false

    override fun lease(): Boolean {
        if (hasReturned) return false

        unbind()
        return true
    }

    override fun unbind(): LeasedBindable<T> {
        if (source != null && !hasReturned) {
            if (revertOnReturn) value = valueBeforeLease!!

            disabled = disabledBeforeLease

            source!!.removeBinding(this)
            source
            hasReturned = true
        }

        internalUnbind()
        return this
    }

    override fun create(): Bindable<T> = LeasedBindable()

    private fun checkValid() {
        if (source != null && hasReturned) throw IllegalStateException("This bindable has already been returned")
    }
}