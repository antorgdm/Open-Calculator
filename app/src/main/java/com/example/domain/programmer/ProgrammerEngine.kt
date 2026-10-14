package com.example.domain.programmer

enum class BitSize(val bits: Int, val label: String, val mask: ULong) {
    BYTE(8, "8-bit BYTE", 0xFFuL),
    WORD(16, "16-bit WORD", 0xFFFFuL),
    DWORD(32, "32-bit DWORD", 0xFFFFFFFFuL),
    QWORD(64, "64-bit QWORD", 0xFFFFFFFFFFFFFFFFuL)
}

enum class NumberBase(val radix: Int, val label: String) {
    HEX(16, "HEX"),
    DEC(10, "DEC"),
    OCT(8, "OCT"),
    BIN(2, "BIN")
}

object ProgrammerEngine {

    fun clampToBitSize(value: ULong, bitSize: BitSize): ULong {
        return value and bitSize.mask
    }

    fun toSignedDecimalString(value: ULong, bitSize: BitSize): String {
        val clamped = clampToBitSize(value, bitSize)
        return when (bitSize) {
            BitSize.BYTE -> {
                val byteVal = clamped.toByte()
                byteVal.toString()
            }
            BitSize.WORD -> {
                val shortVal = clamped.toShort()
                shortVal.toString()
            }
            BitSize.DWORD -> {
                val intVal = clamped.toInt()
                intVal.toString()
            }
            BitSize.QWORD -> {
                val longVal = clamped.toLong()
                longVal.toString()
            }
        }
    }

    fun formatInBase(value: ULong, base: NumberBase, bitSize: BitSize): String {
        val clamped = clampToBitSize(value, bitSize)
        return when (base) {
            NumberBase.HEX -> clamped.toString(16).uppercase()
            NumberBase.DEC -> toSignedDecimalString(clamped, bitSize)
            NumberBase.OCT -> clamped.toString(8)
            NumberBase.BIN -> {
                val raw = clamped.toString(2).padStart(bitSize.bits, '0')
                raw.chunked(4).joinToString(" ")
            }
        }
    }

    fun parseInput(text: String, base: NumberBase, bitSize: BitSize): ULong? {
        val cleaned = text.replace(" ", "").trim()
        if (cleaned.isEmpty()) return 0uL
        return try {
            if (base == NumberBase.DEC && cleaned.startsWith("-")) {
                val signed = cleaned.toLongOrNull() ?: return null
                clampToBitSize(signed.toULong(), bitSize)
            } else {
                val parsed = cleaned.toULong(base.radix)
                clampToBitSize(parsed, bitSize)
            }
        } catch (_: Exception) {
            null
        }
    }

    fun bitwiseAnd(a: ULong, b: ULong, bitSize: BitSize): ULong = clampToBitSize(a and b, bitSize)
    fun bitwiseOr(a: ULong, b: ULong, bitSize: BitSize): ULong = clampToBitSize(a or b, bitSize)
    fun bitwiseXor(a: ULong, b: ULong, bitSize: BitSize): ULong = clampToBitSize(a xor b, bitSize)
    fun bitwiseNot(a: ULong, bitSize: BitSize): ULong = clampToBitSize(a.inv(), bitSize)

    fun shiftLeft(a: ULong, places: Int, bitSize: BitSize): ULong {
        if (places >= bitSize.bits) return 0uL
        if (places <= 0) return clampToBitSize(a, bitSize)
        return clampToBitSize(a shl places, bitSize)
    }

    fun shiftRight(a: ULong, places: Int, bitSize: BitSize): ULong {
        if (places >= bitSize.bits) return 0uL
        if (places <= 0) return clampToBitSize(a, bitSize)
        return clampToBitSize(a shr places, bitSize)
    }

    fun toggleBit(current: ULong, bitIndex: Int, bitSize: BitSize): ULong {
        if (bitIndex < 0 || bitIndex >= bitSize.bits) return current
        val mask = 1uL shl bitIndex
        return clampToBitSize(current xor mask, bitSize)
    }

    fun isBitSet(current: ULong, bitIndex: Int): Boolean {
        if (bitIndex < 0 || bitIndex >= 64) return false
        return ((current shr bitIndex) and 1uL) == 1uL
    }
}
