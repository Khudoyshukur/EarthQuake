package com.example.earthquakeapplication.fragments

import androidx.viewbinding.ViewBinding

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

abstract class ScrollableFragment<VB: ViewBinding>(
    private val inflate: Inflate<VB>
): BaseFragment<VB>(inflate) {

    abstract fun scrollToTop()
}