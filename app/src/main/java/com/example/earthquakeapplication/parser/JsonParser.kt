package com.example.earthquakeapplication.parser

import android.location.Location
import android.util.JsonReader
import com.example.earthquakeapplication.model.EarthQuake
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class JsonParser : EarthquakeParser {
    override val streamType: StreamType
        get() = StreamType.Json

    override fun parse(inputStream: InputStream): List<EarthQuake> {
        val earthquakes = arrayListOf<EarthQuake>()

        val reader = JsonReader(InputStreamReader(inputStream, "UTF-8"))

        reader.beginObject()
        while (reader.hasNext()) {
            val name = reader.nextName()
            if (name == "features") {
                earthquakes.addAll(
                    readEarthquakeArray(reader)
                )
            } else {
                reader.skipValue()
            }
        }
        reader.endObject()

        return earthquakes
    }

    private fun readEarthquakeArray(reader: JsonReader): List<EarthQuake> {
        val earthQuakes = arrayListOf<EarthQuake>()

        reader.beginArray()
        while (reader.hasNext()) {
            earthQuakes.add(
                readEarthquake(reader)
            )
        }
        reader.endArray()

        return earthQuakes
    }

    private fun readEarthquake(reader: JsonReader): EarthQuake {
        var id = "-1"
        var location: Location? = null
        var properties: EarthQuake? = null


        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "id" -> {
                    id = reader.nextString()
                }
                "geometry" -> {
                    location = getLocation(reader)
                }
                "properties" -> {
                    properties = readProperties(reader)
                }
                else -> {
                    reader.skipValue()
                }
            }
        }
        reader.endObject()

        return EarthQuake(
            id,
            properties!!.mDate,
            properties.mDetails,
            location,
            properties.mMagnitude,
            properties.mLink
        )
    }

    private fun getLocation(reader: JsonReader): Location {
        val location = Location("gps")

        reader.beginObject()
        while (reader.hasNext()) {
            val name = reader.nextName()
            if (name == "coordinates") {
                val doubles = readDoubleArray(reader)
                location.latitude = doubles[0]
                location.longitude = doubles[1]
            } else {
                reader.skipValue()
            }
        }
        reader.endObject()

        return location
    }

    private fun readDoubleArray(reader: JsonReader): List<Double> {
        val doubles = arrayListOf<Double>()

        reader.beginArray()
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble())
        }
        reader.endArray()

        return doubles
    }

    private fun readProperties(reader: JsonReader): EarthQuake {
        var magnitude = -1.0
        var date = Calendar.getInstance().time
        var link = ""
        var details = ""

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "mag" -> {
                    magnitude = reader.nextDouble()
                }
                "time" -> {
                    date = Date(reader.nextLong())
                }
                "url" -> {
                    link = reader.nextString()
                }
                "place" -> {
                    details = reader.nextString()
                }
                else -> {
                    reader.skipValue()
                }
            }
        }
        reader.endObject()

        return EarthQuake(
            mMagnitude = magnitude,
            mLink = link,
            mDate = date,
            mDetails = details
        )
    }
}