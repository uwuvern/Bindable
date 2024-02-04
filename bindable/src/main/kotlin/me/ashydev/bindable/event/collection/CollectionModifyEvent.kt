/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.event.collection

import me.ashydev.bindable.event.IEvent

open class CollectionModifyEvent<T, E : List<T>>(
    val collection: E,
    val type: ModifyType
) : IEvent<E> {


    enum class ModifyType {
        ADD,
        REMOVE,
        SET,
        CLEAR
    }
}