/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.bindable

import me.ashydev.bindable.IBindable

open class Bindable<T> : AbstractBindable<T, Bindable<T>> {
    constructor(value: T) : super(value)
    constructor() : super()
}