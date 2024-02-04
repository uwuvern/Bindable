package me.ashydev.bindable.event.lease

import me.ashydev.bindable.event.IEvent

class LeaseUpdateEvent(
    val type: UpdateType
) : IEvent<LeaseUpdateEvent.UpdateType> {

    enum class UpdateType {
        BEGIN,
        END,
        NONE
    }
}