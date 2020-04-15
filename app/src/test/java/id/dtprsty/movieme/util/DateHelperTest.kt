package id.dtprsty.movieme.util

import org.junit.Assert.assertEquals
import org.junit.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class DateHelperTest: Spek({

    Feature("Convert date"){
        var date: String? = null
        Scenario("Given date value, convert it to format E, dd MMM yyyy"){
            When("date is 2020-02-29"){
                date = "2020-02-29"
            }

            Then("it should return Sat, 29 Feb 2020"){
                assertEquals("Sat, 29 Feb 2020", DateHelper.toSimpleString(date))
            }

            When("date is 2020-03-15"){
                date = "2020-03-15"
            }

            Then("it should return Sat, Sun, 15 Mar 2020"){
                assertEquals("Sun, 15 Mar 2020", DateHelper.toSimpleString(date))
            }

            When("date is 2020-03-14"){
                date = "2020-03-14"
            }

            Then("it should return Sat, Sun, Sat, 14 Mar 2020"){
                assertEquals("Sat, 14 Mar 2020", DateHelper.toSimpleString(date))
            }
        }

        Scenario("Given date value, return output to yyyy"){
            When("date is 2020-02-29"){
                date = "2020-02-29"
            }

            Then("it should return 2020"){
                assertEquals("2020", DateHelper.dateToYear(date))
            }

            When("date is 1999-11-31"){
                date = "1999-11-31"
            }

            Then("it should return 1999"){
                assertEquals("1999", DateHelper.dateToYear(date))
            }

            When("date is 2001-02-29"){
                date = "2001-02-29"
            }

            Then("it should return 2001"){
                assertEquals("2001", DateHelper.dateToYear(date))
            }
        }
    }
})