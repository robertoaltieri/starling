package com.altieri.starling.common.bl

import java.util.UUID

class AppUUIDGenerator {
    fun randomUUID(): String = UUID.randomUUID().toString()
}
