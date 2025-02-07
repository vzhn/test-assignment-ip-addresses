package me.vzhilin.job.ipcounter.tree

class BitsNode(private val bitsCapacity: Int): Node() {
    init {
        if (bitsCapacity % 64 != 0) {
            throw IllegalArgumentException("capacity should be dividable on 64")
        }

        if (bitsCapacity > 65536) {
            throw IllegalArgumentException("maximal capacity for BitPlane is 65536 bits")
        }

        if (bitsCapacity < 64) {
            throw IllegalArgumentException("minimal capacity for BitPlane is 64 bits")
        }
    }

    private val bits = LongArray(bitsCapacity / 64)
    private var size = 0

    fun put(index: Int): Boolean {
        val wordIndex = index / 64
        val bitPosition = index % 64

        val prevValue = bits[wordIndex]
        val newValue = prevValue or (1L shl bitPosition)
        bits[wordIndex] = newValue

        if (prevValue != newValue) {
            ++size
            return true
        } else {
            return false
        }
    }

    fun isFull(): Boolean {
        return size == bitsCapacity
    }

    override fun visit(nv: NodeVisitor) {
        val nBits = bits.sumOf { it.countOneBits() }
        nv.bitPopulation(nBits, bitsCapacity)
    }
}