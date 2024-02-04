package me.ashydev.bindable.event.collection

import me.ashydev.bindable.event.IEvent

class CollectionRemoveEvent<T, E : List<T>>(
    value: T,
    val index: Int,
    collection: E,

) : ValuedCollectionModifyEvent<T, E>(value, collection, ModifyType.REMOVE)