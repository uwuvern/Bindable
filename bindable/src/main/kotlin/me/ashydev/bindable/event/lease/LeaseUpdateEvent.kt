/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

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