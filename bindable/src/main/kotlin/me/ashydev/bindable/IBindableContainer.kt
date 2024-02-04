/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable

interface IBindableContainer<V : IBindableContainer<V>> {
    var target: V?
        get() = null
        set(value) {
            if (value == null)
                return

            bindTo(value)
        }

    fun bindTo(bindable: V): V
    fun weakBind(bindable: V): V

    fun create(): V

    fun getBoundCopy(): V
    fun getUnboundCopy(): V
    fun getWeakCopy(): V

    fun addBinding(reference: V): V
    fun removeBinding(reference: V): V
}