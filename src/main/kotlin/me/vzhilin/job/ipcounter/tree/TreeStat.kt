package me.vzhilin.job.ipcounter.tree

import me.vzhilin.job.ipcounter.BitCounter

typealias Level = Int

class Stat private constructor(
    val emptyNodesCount: Map<Level, Int>,
    val fullNodesCount: Map<Level, Int>,
    val bitPopulations: Map<Int, Int>,
    val totalPagesCount: Int,
    val totalBitPagesCount: Int
) {
    companion object {
        fun collect(bc: BitCounter): Stat {
            var depth = 0
            val emptyNodes = mutableMapOf<Level, Int>()
            val fullNodes = mutableMapOf<Level, Int>()
            val bitPopulations = mutableMapOf<Int, Int>()
            var totalPages = 0
            var totalBitPages = 0

            (1 until bc.conf.pagesCount).forEach { level ->
                emptyNodes[level] = 0
                fullNodes[level] = 0
            }

            val statVisitor = object : NodeVisitor {
                override fun begin() = Unit
                override fun end() = Unit

                override fun fullNode(index: Int) {
                    fullNodes[depth] = fullNodes.getOrDefault(depth, 0) + 1
                }

                override fun emptyNode(index: Int) {
                    emptyNodes[depth] = emptyNodes.getOrDefault(depth, 0) + 1
                }

                override fun beginPage(index: Int) {
                    depth += 1
                    totalPages += 1
                }

                override fun endPage(index: Int) {
                    depth -= 1
                }

                override fun beginBits(index: Int) {
                    depth += 1
                    totalBitPages += 1
                }

                override fun endBits(index: Int) {
                    depth -= 1
                }

                override fun bitPopulation(count: Int, capacity: Int) {
                    bitPopulations[count] = bitPopulations.getOrDefault(count, 0) + 1
                }
            }

            bc.visit(statVisitor)
            return Stat(
                emptyNodesCount=emptyNodes,
                fullNodesCount=fullNodes,
                bitPopulations=bitPopulations,
                totalPagesCount=totalPages,
                totalBitPagesCount=totalBitPages
            )
        }
    }
}