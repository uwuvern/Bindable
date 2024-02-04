/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.action.stack

interface IExecutionStack<T> {
    fun execute(value: T): IExecutionStack<T>
    fun executeOne(value: T): IExecutionStack<T>
}