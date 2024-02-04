package me.ashydev.bindable

import me.ashydev.bindable.bindable.Bindable
import me.ashydev.bindable.bindable.LeasedBindable
import me.ashydev.bindable.event.ValueChangedEvent

val strength: Bindable<Float> = Bindable(5.0f)

fun main() {
    val reader: Bindable<Float> = strength.getWeakCopy()

    strength.onValueChanged({ something(it) }, true)
    strength.set(10.0f)

    println("Value of strength is ${reader.value}")
}

fun something(event: ValueChangedEvent<Float>) {
    // execute some code (could be some reactive ingame logic)
    println("Value of strength is ${event.new}")
}