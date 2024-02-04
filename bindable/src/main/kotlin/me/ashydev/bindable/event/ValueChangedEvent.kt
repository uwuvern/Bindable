/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.event

class ValueChangedEvent<T>(
    val old: T?,
    val new: T
) : IEvent<T>