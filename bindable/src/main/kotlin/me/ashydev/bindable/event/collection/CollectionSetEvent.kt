package me.ashydev.bindable.event.collection

import me.ashydev.bindable.event.IEvent

class CollectionSetEvent<T, E : List<T>>(
    val old: T,
    new: T,

    val index: Int,
    collection: E,
) : ValuedCollectionModifyEvent<T, E>(new, collection, ModifyType.SET)