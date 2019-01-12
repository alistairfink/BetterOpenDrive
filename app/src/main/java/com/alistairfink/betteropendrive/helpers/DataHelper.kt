package com.alistairfink.betteropendrive.helpers

import java.math.BigDecimal

class DataHelper
{
    companion object {
        fun dataSizeToString(size: Int): String
        {

            var denominator = BigDecimal("1024")
            var b = size.toBigDecimal()
            var kb = b/denominator
            if (kb < BigDecimal.ONE)
            {
                return "$b B"
            }
            var mb = kb/denominator
            if (mb < BigDecimal.ONE)
            {
                return "$kb KB"
            }
            var gb = mb/denominator
            if (gb < BigDecimal.ONE)
            {
                return "$mb MB"
            }
            var tb = gb/denominator
            if (tb < BigDecimal.ONE)
            {
                return "$gb GB"
            }
            return "$tb TB"
        }
    }
}