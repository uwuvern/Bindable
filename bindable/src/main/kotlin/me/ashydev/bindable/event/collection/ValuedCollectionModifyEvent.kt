/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.event.collection

open class ValuedCollectionModifyEvent<T, E : List<T>>(
    val value: T,

    collection: E,
    type: ModifyType
) : CollectionModifyEvent<T, E>(collection, type)