/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.types

import me.ashydev.bindable.action.Action
import me.ashydev.bindable.action.stack.ActionStack
import me.ashydev.bindable.event.ValueChangedEvent

interface IDisableable {
    fun getDisableChanged(): ActionStack<ValueChangedEvent<Boolean>>

    fun onDisableChanged(block: Action<ValueChangedEvent<Boolean>>, runOnceImmediately: Boolean = false): IDisableable

    fun setDisabled(disabled: Boolean): IDisableable

    fun disable(): IDisableable = setDisabled(true)
    fun enable(): IDisableable = setDisabled(false)

    fun isDisabled(): Boolean
}