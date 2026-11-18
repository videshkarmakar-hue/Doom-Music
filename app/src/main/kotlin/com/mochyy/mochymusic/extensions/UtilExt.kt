// UtilExt.kt

package com.videshkarmakar.doom.extensions

fun <T> tryOrNull(block: () -> T): T? =
    try {
        block()
    } catch (e: Exception) {
        null
    }
