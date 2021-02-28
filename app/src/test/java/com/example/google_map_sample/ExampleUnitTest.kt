package com.example.google_map_sample

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
@Test
fun flowTest(){
    val namesFlow = flow {
        val names = listOf("Jody", "Steve", "Lance", "Joe")
        for (name in names) {
            delay(100)
            emit(name)
        }
    }
    runBlocking {
        namesFlow
            .map { name -> name.length }
            .filter { length -> length < 5 }
            .collect { println(it) }
    }


}
