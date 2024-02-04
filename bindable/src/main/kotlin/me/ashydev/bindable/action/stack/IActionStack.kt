/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.action.stack

import me.ashydev.bindable.action.Action

interface IActionStack<T> : IExecutionStack<T>, IStack<Action<T>> {
    fun asList(): List<Action<T>>
}