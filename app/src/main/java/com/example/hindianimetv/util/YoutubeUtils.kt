package com.example.hindianimetv.util

import androidx.core.net.toUri

object YoutubeUtils {

    fun getThumbnailUrlFromVideoId(videoId: String): String {
        return "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
    }

    fun extractYoutubeId(url: String?): String? {
        if (url == null) return null
        val patterns = listOf(
            "v=([A-Za-z0-9_-]{11})",
            "be/([A-Za-z0-9_-]{11})",
            "embed/([A-Za-z0-9_-]{11})",
            "v/([A-Za-z0-9_-]{11})"
        )
        patterns.forEach { pattern ->
            val regex = Regex(pattern)
            val match = regex.find(url)
            if (match != null) return match.groupValues[1]
        }

        val uri = try {
            url.toUri()
        } catch (_: Exception) {
            null
        }

        uri?.getQueryParameter("v")?.let {
            if (it.length >= 11) return it
        }
        return null
    }
}

