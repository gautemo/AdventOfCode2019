import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class FuelReqKtTest {

    @Test
    fun testSumOfFuel() {
        val masses = listOf("12", "14", "1969", "100756")
        val res = sumOfFuel(masses)
        assertEquals(2 + 2 + 654 + 33583, res)
    }

    @ParameterizedTest
    @MethodSource("masses")
    fun shouldCalculateFuel(mass: Int, expected: Int){
        val res = calculateFuel(mass)
        assertEquals(expected, res)
    }

    @ParameterizedTest
    @MethodSource("massesRecursively")
    fun `calculate fuel recursively`(mass: Int, expected: Int){
        val res = calculateFuelRecursively(mass)
        assertEquals(expected, res)
    }

    @Test
    fun testSumOfFuelRecursively() {
        val masses = listOf("12", "1969", "100756")
        val res = sumOfFuelRecursively(masses)
        assertEquals(2 + 966 + 50346, res)
    }

    companion object{
        @JvmStatic
        fun masses() = listOf(
            Arguments.of(12, 2),
            Arguments.of(14, 2),
            Arguments.of(1969, 654),
            Arguments.of(100756, 33583)
        )

        @JvmStatic
        fun massesRecursively() = listOf(
            Arguments.of(12, 2),
            Arguments.of(1969, 966),
            Arguments.of(100756, 50346)
        )
    }
}