package id.dtprsty.movieme.util

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateConverter {

    fun toSimpleString(dateString: String?): String {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = null
        try{
            date = dateFormat.parse(dateString)
        }catch (e: ParseException){
            e.printStackTrace()
        }
        return SimpleDateFormat("E, dd MMM yyyy").format(date)
    }

    fun dateToYear(dateString: String?): String {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = null
        try{
            date = dateFormat.parse(dateString)
        }catch (e: ParseException){
            e.printStackTrace()
        }
        return SimpleDateFormat("yyyy").format(date)
    }
}