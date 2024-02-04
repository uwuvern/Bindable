/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

import me.ashydev.bindable.bindable.Bindable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class BindableTest {
    private lateinit var bindable: Bindable<Int>

    @BeforeEach
    fun setUp() {
        bindable = Bindable()
    }

    @Test
    fun testSetGetValue() {
        bindable.set(10)
        assertEquals(10, bindable.get())
    }

    @Test
    fun testSetGetDefault() {
        bindable.setDefaultValue(20)
        assertEquals(20, bindable.getDefaultValue())
    }

    @Test
    fun testIsDefault() {
        bindable.setDefaultValue(30)
        bindable.set(30)
        assertTrue(bindable.isDefault())
    }

    @Test
    fun testSetGetDisabled() {
        bindable.setDisabled(true)
        assertTrue(bindable.isDisabled())
    }

    @Test
    fun testSetDisabledThrowsException() {
        bindable.setDisabled(true)
        Assertions.assertThrows(
            IllegalStateException::class.java
        ) { bindable.set(40) }
    }
}