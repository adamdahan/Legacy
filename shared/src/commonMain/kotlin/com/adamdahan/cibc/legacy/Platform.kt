package com.adamdahan.cibc.legacy

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform