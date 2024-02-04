/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.event.collection

import me.ashydev.bindable.event.IEvent

class CollectionSetEvent<T, E : List<T>>(
    val old: T,
    new: T,

    val index: Int,
    collection: E,
) : ValuedCollectionModifyEvent<T, E>(new, collection, ModifyType.SET)