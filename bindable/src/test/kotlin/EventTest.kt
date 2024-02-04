import me.ashydev.bindable.bindable.Bindable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class EventTest {
    private lateinit var bindable: Bindable<Int>

    @BeforeEach
    fun setUp() {
        bindable = Bindable(5)
    }

    @Test
    fun testValueChanged() {
        bindable.onValueChanged({ event -> assertEquals(10, event.new) })
        bindable.set(10)
    }

    @Test
    fun testValueChangedWithOldValue() {
        bindable.onValueChanged({ event ->
            run {
                assertEquals(10, event.new)
                assertEquals(5, event.old)
            }
        })
        bindable.set(10)
    }

    @Test
    fun testDisabledValueChanged() {
        bindable.onDisableChanged({ event -> assertTrue(event.new) })
        bindable.setDisabled(true)
    }

    @Test
    fun testDefaultValueChanged() {
        bindable.onDefaultChanged({ event -> assertEquals(10, event.new) })
        bindable.setDefaultValue(10)
    }


}