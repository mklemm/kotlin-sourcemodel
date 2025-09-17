package net.codesup.util

import java.time.LocalDateTime
import java.time.temporal.Temporal
import kotlin.String

class TestDateTimeClass<T: Temporal>(val temporalProperty: LocalDateTime) {

	var `var`: String = "Initial Value"

	val property2: List<LocalDateTime>? = listOf(LocalDateTime.now())

}
