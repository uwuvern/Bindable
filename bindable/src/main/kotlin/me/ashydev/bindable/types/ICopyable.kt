/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.types

interface ICopyable<T : ICopyable<T>> {
    fun copy(): T
    fun copyTo(copyable: T): T
}