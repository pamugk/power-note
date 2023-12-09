package utils

import java.net.URI

fun isValidUrl(url: String) =
    try {
        URI.create(url)
        url.isNotEmpty()
    } catch (_: Exception) { false }