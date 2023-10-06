package com.mung.blog.aop

fun <T> NativeKotlinPerformence(key: String, function : () -> T): T {
    println("==================== Performence start ======================")
    println(key)
    val result = function.invoke()
    println(result)
    println("==================== Performence end ======================")
    return result
}
