package me.vzhilin.job.ipcounter.tree

sealed class Node {
    abstract fun visit(nv: NodeVisitor)
}

