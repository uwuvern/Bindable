/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.event.collection

import me.ashydev.bindable.event.IEvent

class CollectionRemoveEvent<T, E : List<T>>(
    value: T,
    val index: Int,
    collection: E,

) : ValuedCollectionModifyEvent<T, E>(value, collection, ModifyType.REMOVE)