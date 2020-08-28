package com.tbright.webrtcdemo.utils

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KProperty

/**
 * Extra的参数代理
 */

class ExtraDelegate<T>(private var extraName: String, private var defaultValue: T) {
    private var extraValue: T? = null
    operator fun getValue(thisRes: AppCompatActivity, property: KProperty<*>): T {
        extraValue = getExtra(extraValue, extraName, thisRes)
        return extraValue ?: defaultValue
    }
    operator fun setValue(thisRes: AppCompatActivity, property: KProperty<*>, setExtraValue: T?) {
        extraValue = setExtraValue
    }

    operator fun getValue(thisRes: Fragment, property: KProperty<*>): T {
        extraValue = getExtra(extraValue, extraName, thisRes)
        return extraValue ?: defaultValue
    }

    operator fun setValue(thisRes: Fragment, property: KProperty<*>, setExtraValue: T?) {
        extraValue = setExtraValue
    }
}

fun <T> extraDelegate(extra: String, default: T) = ExtraDelegate(extra, default)

fun extraDelegate(extra: String) = ExtraDelegate(extra, null)

@Suppress("UNCHECKED_CAST")
private fun <T> getExtra(oldExtraValue: T?, extraName: String, thisRef: AppCompatActivity): T? =
    oldExtraValue ?: thisRef.intent?.extras?.get(extraName) as T?

@Suppress("UNCHECKED_CAST")
private fun <T> getExtra(oldExtraValue: T?, extraName: String, thisRes: Fragment): T? =
    oldExtraValue ?: thisRes.arguments?.get(extraName) as T?