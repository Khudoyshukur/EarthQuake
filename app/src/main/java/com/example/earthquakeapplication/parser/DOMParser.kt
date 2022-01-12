package com.example.earthquakeapplication.parser

import android.location.Location
import com.example.earthquakeapplication.model.EarthQuake
import org.w3c.dom.Element
import java.io.InputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class DOMParser : EarthquakeParser {
    override val streamType: StreamType
        get() = StreamType.Xml

    override fun parse(inputStream: InputStream): List<EarthQuake> {
        val earthquakes = arrayListOf<EarthQuake>()

        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()

        val document = builder.parse(inputStream)

        val list = document.getElementsByTagName("entry")
        if (list != null && list.length > 0) {
            for (i in 0 until list.length) {
                val entry = list.item(i) as Element

                val id = entry.getElementsByTagName("id").item(0) as Element
                val title = entry.getElementsByTagName("title").item(0) as Element
                val g = entry.getElementsByTagName("georss:point").item(0) as Element
                val updated = entry.getElementsByTagName("updated").item(0) as Element
                val link = entry.getElementsByTagName("link").item(0) as Element

                var details = ""
                if (title.firstChild.nodeValue.contains("-")) {
                    details = title.firstChild.nodeValue.split("-")[1].trim()
                }

                val location = Location("GPS")
                location.latitude = g.firstChild.nodeValue.split(" ")[0].toDouble()
                location.longitude = g.firstChild.nodeValue.split(" ")[1].toDouble()

                val date: Date = try {
                    val format = SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss.SSS'Z'", Locale.ROOT)
                    format.parse(updated.firstChild.nodeValue) ?: GregorianCalendar(0, 0, 0).time
                } catch (e: Exception) {
                    GregorianCalendar(0, 0, 0).time
                }

                val magnitude = title.firstChild.nodeValue.split(" ")[1].toDoubleOrNull() ?: -1.0

                val earthQuake = EarthQuake(
                    id.firstChild.nodeValue,
                    date,
                    details,
                    location,
                    magnitude,
                    "https://earthquake.usgs.gov${link.getAttribute("href")}"
                )
                earthquakes.add(earthQuake)
            }
        }

        return earthquakes
    }
}