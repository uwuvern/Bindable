package me.ashydev.bindable.types

import me.ashydev.bindable.action.Action
import me.ashydev.bindable.action.stack.ActionStack
import me.ashydev.bindable.event.collection.*

interface ICollectionModify<T, E : List<T>> {
    fun getCollectionAdd(): ActionStack<CollectionAddEvent<T, E>>
    fun getCollectionRemove(): ActionStack<CollectionRemoveEvent<T, E>>
    fun getCollectionSet(): ActionStack<CollectionSetEvent<T, E>>
    fun getCollectionClear(): ActionStack<CollectionClearEvent<T, E>>

    fun onCollectionAdd(action: Action<CollectionAddEvent<T, E>>, runOnceImmediately: Boolean = false): ICollectionModify<T, E>
    fun onCollectionRemove(action: Action<CollectionRemoveEvent<T, E>>, runOnceImmediately: Boolean = false): ICollectionModify<T, E>
    fun onCollectionSet(action: Action<CollectionSetEvent<T, E>>, runOnceImmediately: Boolean = false): ICollectionModify<T, E>
    fun onCollectionClear(action: Action<CollectionClearEvent<T, E>>, runOnceImmediately: Boolean = false): ICollectionModify<T, E>
}