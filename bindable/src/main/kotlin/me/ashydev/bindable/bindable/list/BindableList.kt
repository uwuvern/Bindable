package me.ashydev.bindable.bindable.list

import me.ashydev.bindable.bindable.Bindable

class BindableList<T>(collection: Collection<T>? = emptyList(), internalDisabled: Boolean = false) :
    AbstractBindableList<T, BindableList<T>>(collection, internalDisabled) {
}