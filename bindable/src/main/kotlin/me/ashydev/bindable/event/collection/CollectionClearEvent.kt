package me.ashydev.bindable.event.collection

class CollectionClearEvent<T, E : List<T>>(
    collection: E,
) : CollectionModifyEvent<T, E>(collection, ModifyType.CLEAR)