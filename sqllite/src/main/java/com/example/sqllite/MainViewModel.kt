package com.example.sqllite

import androidx.lifecycle.ViewModel
import com.example.sqllite.database.HoardDatabase
import com.example.sqllite.entity.Hoard

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class MainViewModel : ViewModel() {

    fun getAllHoards() = HoardDatabase.getAllHoardsAsLiveData()

    fun addHoard(hoardName: String) {
        HoardDatabase.insert(
            Hoard(
                hoardName = hoardName,
                isHoardAccessible = (0..10).random() % 2 == 0, // fake
                goldHoarded = (0..1).random() //fake
            )
        )
    }
}