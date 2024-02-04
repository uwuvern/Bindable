package me.ashydev.bindable.event.collection

import me.ashydev.bindable.event.IEvent

class CollectionAddEvent<T, E : List<T>>(
    new: T,
    val index: Int,
    collection: E,
) : ValuedCollectionModifyEvent<T, E>(new, collection, ModifyType.ADD)