package me.ashydev.bindable.bindable

import me.ashydev.bindable.IBindable

open class Bindable<T> : AbstractBindable<T, Bindable<T>> {
    constructor(value: T) : super(value)
    constructor() : super()
}