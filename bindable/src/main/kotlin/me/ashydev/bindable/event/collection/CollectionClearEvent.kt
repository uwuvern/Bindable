/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.event.collection

class CollectionClearEvent<T, E : List<T>>(
    collection: E,
) : CollectionModifyEvent<T, E>(collection, ModifyType.CLEAR)