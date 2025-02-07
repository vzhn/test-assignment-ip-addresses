package me.vzhilin.job.ipcounter

import me.vzhilin.job.ipcounter.tree.Stat
import kotlin.test.Test
import kotlin.test.assertEquals

class MiniTest {
    @Test
    fun oneBitSet() {
        val bc = BitCounter(BitAddressConfiguration(kotlin.collections.listOf(2), 6))
        bc.put(0)

        val stat = Stat.Companion.collect(bc)
        assertEquals(1, bc.getCount())
        assertEquals(3, stat.emptyNodesCount[1])
        assertEquals(0, stat.fullNodesCount[1])
        assertEquals(kotlin.collections.mapOf(1 to 1), stat.bitPopulations)
    }

    @Test
    fun onePageSet() {
        val bc = BitCounter(BitAddressConfiguration(listOf(2), 6))

        val bits = 6
        for (i in 0 until (1 shl bits)) {
            bc.put(i.toLong())
        }

        val stat = Stat.collect(bc)
        assertEquals(64, bc.getCount())
        assertEquals(3, stat.emptyNodesCount[1])
        assertEquals(1, stat.fullNodesCount[1])
        assertEquals(emptyMap(), stat.bitPopulations)
    }

    @Test
    fun allPagesSet() {
        val bc = BitCounter(BitAddressConfiguration(listOf(2), 6))

        val bits = 2 + 6
        for (i in 0..(1 shl bits)) {
            bc.put(i.toLong())
        }

        val stat = Stat.collect(bc)
        assertEquals(256, bc.getCount())
        assertEquals(0, stat.emptyNodesCount[1])
        assertEquals(4, stat.fullNodesCount[1])
        assertEquals(emptyMap(), stat.bitPopulations)
    }
}