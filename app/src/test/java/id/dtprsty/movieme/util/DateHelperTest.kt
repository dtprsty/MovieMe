package id.dtprsty.movieme.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DateHelperTest {

    @Test
    fun toSimpleString() {
        assertEquals("Sat, 29 Feb 2020", DateHelper.toSimpleString("2020-02-29"))
        assertEquals("Sun, 15 Mar 2020", DateHelper.toSimpleString("2020-03-15"))
        assertEquals("Sat, 14 Mar 2020", DateHelper.toSimpleString("2020-03-14"))
    }

    @Test
    fun dateToYear() {
        assertEquals("2020", DateHelper.dateToYear("2020-02-29"))
        assertEquals("1999", DateHelper.dateToYear("1999-11-31"))
        assertEquals("2001", DateHelper.dateToYear("2001-02-29"))
    }
}