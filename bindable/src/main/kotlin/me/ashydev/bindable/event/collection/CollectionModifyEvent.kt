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