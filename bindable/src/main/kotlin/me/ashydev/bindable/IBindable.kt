/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable

import me.ashydev.bindable.bindable.Bindable
import me.ashydev.bindable.bindable.LeasedBindable
import me.ashydev.bindable.types.*
import java.io.Serializable
import java.lang.ref.WeakReference

interface IBindable<T, V : IBindable<T, V>> :
    IDisableable, IHasDefault<T>, IUnbindable<V>,
    IValueChangeable<T>, ICopyable<IBindable<T, V>>,
    IBindableContainer<V>, ILeasable<T, V, LeasedBindable<T>>,
    Serializable {

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <V, T : IBindable<V, T>> create(source: T): T {
            val copy: IBindable<V, T> = source.create()

            if (copy.javaClass != source.javaClass) {
                throw IllegalArgumentException("Attempted to create a bindable copy of ${source.javaClass.simpleName}, but the returned type was ${copy.javaClass.simpleName}"
                    + "Override ${source.javaClass.simpleName}.create() for GetBoundCopy() to return the correct type.")
            }

            copy.bindTo(source);
            return copy as T
        }

        fun <T : IBindable<Any, T>> loose(source: T): T = create(source)
    }

    var value: T
        get() = get()
        set(value) {
            set(value)
        }

    var default: T
        get() = getDefaultValue()
        set(value) {
            setDefaultValue(value)
        }

    var disabled: Boolean
        get() = isDisabled()
        set(value) {
            setDisabled(value)
        }

    fun get(): T
    fun set(value: T): V
}