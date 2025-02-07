package me.vzhilin.job

import me.vzhilin.job.ipcounter.IpAddressParser
import me.vzhilin.job.ipcounter.BitCounter
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import kotlin.system.exitProcess

private fun countIps(filePath: String): Long {
    val bc = BitCounter()
    val parser = IpAddressParser()

    BufferedInputStream(FileInputStream(filePath), 8 * 8192).use { bis ->
        var ch: Int = bis.read()
        do {
            if (parser.feed(ch.toChar())) {
                bc.put(parser.getAddr())
            }

            ch = bis.read()
        } while (ch != -1)
    }
    return bc.getCount()
}

fun main(argv: Array<String>) {
    if (argv.isEmpty()) {
        println("Usage: ipcounter <file_path>")
        return
    }

    val filePath = argv[0]
    if (!File(filePath).exists()) {
        System.err.println("File not found: $filePath")
        exitProcess(1)
    }

    println(countIps(filePath))
}
