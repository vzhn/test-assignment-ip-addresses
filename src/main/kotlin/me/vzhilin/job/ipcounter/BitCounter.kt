package me.vzhilin.job.ipcounter

import me.vzhilin.job.ipcounter.tree.BitSetNode
import me.vzhilin.job.ipcounter.tree.PageNode
import me.vzhilin.job.ipcounter.tree.Node
import me.vzhilin.job.ipcounter.tree.NodeVisitor
import kotlin.Long

class BitCounter(val conf: BitAddressConfiguration = BitAddressConfiguration.DEFAULT) {
    private var counter: Long = 0
    private val root = PageNode(1 shl conf.bitWidth(0))

    // reduce allocations by reusing array
    private var path = arrayOfNulls<Node?>(conf.pagesCount)

    init {
        path[0] = root
    }

    fun put(bit: Long) {
        if (Long.SIZE_BITS - bit.countLeadingZeroBits() > conf.addrWidth) {
            throw IllegalArgumentException("address width exceedes max: ${conf.addrWidth}")
        }

        bit.countLeadingZeroBits()
        if (root.isFull()) {
            return
        }

        for (i in 0 until conf.pagesCount - 1) {
            val currentPage = path[i] as PageNode
            val nextAddr = conf.getAddressPart(i, bit)

            if (currentPage.isFull(nextAddr)) {
                return
            }

            path[i + 1] = currentPage.getOrCreate(nextAddr) {
                val capacity = 1 shl (conf.bitWidth(i + 1))
                val isLastIteration = i == conf.pagesCount - 2

                if (isLastIteration)
                    BitSetNode(capacity)
                else
                    PageNode(capacity)
            }
        }

        val bits = path.last() as BitSetNode
        val bitAddress = conf.getAddressPart(conf.pagesCount - 1,  bit)
        if (bits.put(bitAddress)) {
            ++counter
        }

        if (bits.isFull()) {
            checkAncestorsAndMarkAsFull(path, bit)
        }
    }

    private fun BitCounter.checkAncestorsAndMarkAsFull(pages: Array<Node?>, bit: Long) {
        val pagesExceptBits = pages.size - 2 downTo 0
        for (i in pagesExceptBits) {
            val currentPage = pages[i] as PageNode
            val nextAddr = conf.getAddressPart(i, bit)

            currentPage.markFull(nextAddr)
            if (!currentPage.isFull()) break
        }
    }

    fun getCount(): Long {
        return counter
    }

    internal fun visit(visitor: NodeVisitor) {
        visitor.begin()
        visitor.beginPage(0)
        root.visit(visitor)
        visitor.endPage(0)
        visitor.end()
    }
}
