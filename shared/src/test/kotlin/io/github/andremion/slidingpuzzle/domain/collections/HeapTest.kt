package io.github.andremion.slidingpuzzle.domain.collections

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HeapTest {

    private val sut = Heap<Int>(compareBy { it })

    @Test
    fun `poll empty`() {
        val result = sut.poll()

        assertNull(result)
    }

    @Test
    fun `poll four elements`() {
        sut.add(4)
        sut.add(3)
        sut.add(9)
        sut.add(1)

        val result1 = sut.poll()
        val result2 = sut.poll()
        val result3 = sut.poll()
        val result4 = sut.poll()

        assertEquals(1, result1)
        assertEquals(3, result2)
        assertEquals(4, result3)
        assertEquals(9, result4)
    }
}
