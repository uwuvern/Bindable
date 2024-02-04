package me.ashydev.bindable.event.collection

open class ValuedCollectionModifyEvent<T, E : List<T>>(
    val value: T,

    collection: E,
    type: ModifyType
) : CollectionModifyEvent<T, E>(collection, type)