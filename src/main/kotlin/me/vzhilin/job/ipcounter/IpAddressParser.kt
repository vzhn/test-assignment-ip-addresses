package me.vzhilin.job.ipcounter

/**
 * Parser for valid input, no syntax checks
 */
class IpAddressParser {
    private var addr = 0L
    private var accumulator = 0L

    /**
     * @return true when new address is ready
     */
    fun feed(ch: Char): Boolean {
        when (ch) {
            in '0'..'9' -> {
                accumulator = (accumulator and 0xffffff00) or ((accumulator and 0xff) * 10 + (ch - '0'))
            }
            '.' -> {
                accumulator = accumulator shl 8
            }
            '\n' -> {
                addr = accumulator
                accumulator = 0L
                return true
            }
            else -> throw RuntimeException("unexpected input: '${ch}'")
        }

        return false
    }

    fun getAddr(): Long {
        return addr
    }
}