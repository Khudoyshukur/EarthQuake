package com.example.earthquakeapplication.parser

import android.location.Location
import com.example.earthquakeapplication.model.EarthQuake
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class XmlPullParser : EarthquakeParser {
    override val streamType: StreamType
        get() = StreamType.Xml

    override fun parse(inputStream: InputStream): List<EarthQuake> {
        val earthquakes = arrayListOf<EarthQuake>()

        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true

        val parser = factory.newPullParser()
        parser.setInput(inputStream, null)

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "entry") {
                parser.next()

                var id = "no id"
                var date = GregorianCalendar(0, 0, 0).time
                var mDetails = "no details"
                var mLocation: Location? = null
                var mMagnitude = 5.0
                var mLink: String? = null

                while (!(parser.eventType == XmlPullParser.END_TAG && parser.name == "entry")) {
                    try {
                        if (parser.eventType == XmlPullParser.START_TAG && parser.name == "id") {
                            id = parser.nextText()
                        } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "title") {
                            mDetails = parser.nextText()
                            mDetails = if (mDetails.contains("-")) {
                                mDetails.split("-")[1].trim()
                            } else {
                                ""
                            }
                        } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "category") {
                            if (parser.getAttributeValue(0) == "Magnitude") {
                                mMagnitude =
                                    parser.getAttributeValue(1).filter { it.isDigit() }.toDouble()
                            }
                        } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "link") {
                            mLink = "https://earthquake.usgs.gov${
                                parser.getAttributeValue(
                                    null,
                                    "href"
                                )
                            }"
                        } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "point") {
                            val location = parser.nextText().split(" ")
                            mLocation = Location("GPS")
                            mLocation.latitude = location[0].toDouble()
                            mLocation.longitude = location[1].toDouble()
                        } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "updated") {
                            val dFormat =
                                SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss.SSS'Z'", Locale.ROOT)
                            date =
                                dFormat.parse(parser.nextText()) ?: GregorianCalendar(0, 0, 0).time
                        }
                    } catch (e: Exception) {
                    }
                    parser.next()
                }
                earthquakes.add(EarthQuake(id, date, mDetails, mLocation, mMagnitude, mLink))
            }

            parser.next()
        }

        return earthquakes
    }
}