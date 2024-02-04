/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.types

import me.ashydev.bindable.IBindable
import me.ashydev.bindable.ILeasedBindable
import me.ashydev.bindable.action.Action
import me.ashydev.bindable.action.stack.ActionStack
import me.ashydev.bindable.event.lease.LeaseUpdateEvent

interface ILeasable<T, B : IBindable<T, B>, V : ILeasedBindable<*, *>> {
    fun getLeased(): ActionStack<LeaseUpdateEvent>

    fun onLeaseChange(action: Action<LeaseUpdateEvent>, runOnceImmediately: Boolean = false): B

    fun beginLease(revertOnReturn: Boolean): V
    fun endLease(bindable: V): B
}