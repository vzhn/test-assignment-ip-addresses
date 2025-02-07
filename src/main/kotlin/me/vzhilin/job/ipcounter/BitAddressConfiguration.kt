package me.vzhilin.job.ipcounter

import kotlin.Long

class BitAddressConfiguration(pages: List<Int>, bitset: Int) {
    private val addressWidths = pages + bitset

    val pagesCount = addressWidths.size
    val addrWidth = addressWidths.sum()

    init {
        if (pages.isEmpty()) {
            throw IllegalArgumentException("configuration without pages is not supported")
        }
        if (bitset <= 0) {
            throw IllegalArgumentException("address part for bitmaps should not be empty")
        }
        if (addrWidth > Long.SIZE_BITS) {
            throw IllegalArgumentException("max address width is ${Long.SIZE_BITS}")
        }
    }

    private val offsets = addressWidths.asReversed().runningFold(0) { acc, width ->
        acc + width
    }.dropLast(1).asReversed().toList()

    fun getAddressPart(index: Int, address: Long): Int {
        val mask = (1L shl addressWidths[index]) - 1L
        return ((address shr offsets[index]) and mask).toInt()
    }

    fun bitWidth(pageNumber: Int): Int {
        return addressWidths[pageNumber]
    }

    companion object {
        // default configuration
        //
        // 2 levels of pages
        // each page has up to 2^8 (256) children
        // each bit set has 2^16 (65536) bits
        val DEFAULT = BitAddressConfiguration(listOf(8, 8), 16)
    }
}