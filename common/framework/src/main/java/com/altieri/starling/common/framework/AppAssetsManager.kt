package com.altieri.starling.common.framework

import android.content.Context
import android.util.Log

/**
 * wrapper of the assets manager to access files in the assets folder
 * This is not thought to be used to hide the implementation details but just to allow mocking and to make it easier to replace it
 */
object AppAssetsManager {
    fun readFile(context: Context, file: String) =
        context
            .assets
            .open(file)
            .bufferedReader()
            .use {
                val text = it.readText()
                Log.d("AppAssetsManager", text)
                text
            }
}
