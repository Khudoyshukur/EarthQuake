package com.example.sqllite.entity

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

data class Hoard(
    var id: Long? = null,
    var hoardName: String? = null,
    var goldHoarded: Int? = null,
    var isHoardAccessible: Boolean = false
)