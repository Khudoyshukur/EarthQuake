package com.example.earthquakeapplication.parser

import com.example.earthquakeapplication.model.EarthQuake
import java.io.InputStream

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface EarthquakeParser {
    val streamType: StreamType

    fun parse(inputStream: InputStream): List<EarthQuake>
}

sealed class StreamType {
    object Json : StreamType()
    object Xml : StreamType()
}