package com.rysanek.fetchimagefilter.domain.usecases

import okhttp3.OkHttpClient
import okhttp3.Request

fun fetchContentLength(
    url: String
): Long {
    return OkHttpClient().newCall(Request.Builder().url(url).head().build()).execute().headers["Content-Length"]?.toLong() ?: -1
}