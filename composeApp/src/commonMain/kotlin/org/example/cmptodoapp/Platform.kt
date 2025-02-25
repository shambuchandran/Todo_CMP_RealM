package org.example.cmptodoapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform