package me.ashydev.bindable.event

class ValueChangedEvent<T>(
    val old: T?,
    val new: T
) : IEvent<T>