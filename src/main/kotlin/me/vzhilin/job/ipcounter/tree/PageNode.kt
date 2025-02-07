package me.vzhilin.job.ipcounter.tree

import java.util.BitSet

class PageNode(val capacity: Int): Node() {
    private var nodes: Array<Node?> = Array(capacity) { null }
    private var fullNodes: BitSet = BitSet(capacity)
    private var fullCount = 0

    fun getOrCreate(index: Int, creator: () -> Node): Node {
        if (nodes[index] == null && !fullNodes[index]) {
            nodes[index] = creator()
        }

        return nodes[index]!!
    }

    fun markFull(index: Int) {
        if (fullNodes[index]) {
            throw IllegalArgumentException("$index is full already")
        } else {
            fullNodes[index] = true
            nodes[index] = null
        }
        ++fullCount
    }

    fun isFull(): Boolean {
        return fullCount == capacity
    }

    fun isFull(index: Int): Boolean {
        return fullNodes[index]
    }

    override fun visit(nv: NodeVisitor) {
        for (i in 0 until capacity) {
            if (fullNodes.get(i)) {
                nv.fullNode(i)
            } else if (nodes[i] == null) {
                nv.emptyNode(i)
            } else {
                val next = nodes[i]!!
                if (next is PageNode) {
                    nv.beginPage(i)
                    next.visit(nv)
                    nv.endPage(i)
                } else {
                    nv.beginBits(i)
                    next.visit(nv)
                    nv.endBits(i)
                }
            }
        }
    }
}
