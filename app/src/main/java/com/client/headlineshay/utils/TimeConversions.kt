package com.client.headlineshay.utils

import java.text.SimpleDateFormat
import java.util.*

class TimeConversions{


    companion object{

        fun getCurrentDate():Date{
            return Date()
        }

        /*Gives difference of two Dates in hours,minutes,seconds*/
        fun getDifferenceIn(startDate: Date?, nowDate:Date, unit:String):String{
            //gives difference in hours
            val diff: Long = nowDate.time - startDate!!.time

            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60

            return when(unit){
                "hh" -> hours.toString()
                "mm" -> minutes.toString()
                "ss" -> seconds.toString()
                "hhmmss" -> "$hours:$minutes:$seconds"
                else -> hours.toString()
            }
        }


        fun stringToDate(stringDate: String?):Date?{
            val format = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            return format.parse(stringDate);
        }

    }


}