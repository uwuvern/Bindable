/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.bindable.list

import me.ashydev.bindable.bindable.Bindable

class BindableList<T>(collection: Collection<T>? = emptyList(), internalDisabled: Boolean = false) :
    AbstractBindableList<T, BindableList<T>>(collection, internalDisabled) {
}