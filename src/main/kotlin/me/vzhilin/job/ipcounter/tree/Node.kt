package me.vzhilin.job.ipcounter.tree

sealed class Node {
    internal abstract fun visit(nv: NodeVisitor)
}

