/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.types

import me.ashydev.bindable.IBindable
import me.ashydev.bindable.action.Action
import me.ashydev.bindable.action.stack.ActionStack
import me.ashydev.bindable.event.ValueChangedEvent

interface IHasDefault<T> {
    fun getDefaultChanged(): ActionStack<ValueChangedEvent<T>>

    fun getDefaultValue(): T
    fun setDefault(): IHasDefault<T>

    fun setDefaultValue(default: T): IHasDefault<T>

    fun onDefaultChanged(action: Action<ValueChangedEvent<T>>, runOnceImmediately: Boolean = false): IHasDefault<T>

    fun isDefault(): Boolean
}