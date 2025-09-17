package net.codesup.util

import kotlin.String
import kotlin.collections.List
import kotlin.collections.listOf

import java.time.LocalDateTime
import java.time.temporal.Temporal

class TestDateTimeClass<T: Temporal>(val temporalProperty: LocalDateTime) {

	var `var`: String = "Initial Value"

	val property2: List<LocalDateTime>? = listOf(LocalDateTime.now())

}
