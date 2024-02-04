import me.ashydev.bindable.bindable.Bindable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BindingTest {
    private lateinit var bindable1: Bindable<Int>
    private lateinit var bindable2: Bindable<Int>

    @BeforeEach
    fun setUp() {
        bindable1 = Bindable(10)
        bindable2 = Bindable(20)
    }

    @Test
    fun testValueAfterBindTo() {
        bindable1.bindTo(bindable2)
        bindable2.set(30)
        assertEquals(30, bindable1.get())
    }

    @Test
    fun testValueAfterUnbindTo() {
        bindable1.bindTo(bindable2)
        bindable1.unbind()
        bindable2.set(40)
        assertEquals(20, bindable1.get())
    }

    @Test
    fun testValueAfterUnbindFrom() {
        bindable1.bindTo(bindable2)
        bindable1.unbindFrom(bindable2)
        bindable2.set(40)
        assertEquals(20, bindable1.get())
    }

    @Test
    fun testValueAfterWeakBind() {
        bindable1.weakBind(bindable2)
        bindable2.set(50)
        assertEquals(50, bindable1.get())
    }
}