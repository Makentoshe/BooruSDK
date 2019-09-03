package com.makentoshe.boorusdk.base

interface Repository<K, V> {
    fun get(key: K): V
}