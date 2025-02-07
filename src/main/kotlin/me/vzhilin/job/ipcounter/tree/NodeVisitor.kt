package me.vzhilin.job.ipcounter.tree

interface NodeVisitor {
    fun begin()
    fun end()

    fun fullNode(index: Int)
    fun emptyNode(index: Int)

    fun beginPage(index: Int)
    fun endPage(index: Int)

    fun beginBits(index: Int)
    fun endBits(index: Int)

    fun bitPopulation(count: Int, capacity: Int)
}