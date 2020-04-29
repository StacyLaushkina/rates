package ru.laushkina.rates.model

import org.junit.Assert.*
import org.junit.Test

class RateShortNameTest {
    @Test
    fun `return unknown when rate short name is unknown`() {
        assertEquals(RateShortName.UNKNOWN, RateShortName.parse("test"))
    }
}