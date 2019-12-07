import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class IntStateKtTest {

    @Test
    fun `Max thruster signal 43210`(){
        val list = "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0".split(',').map { it.toInt() }
        val res = maxThrust(list, listOf(0,1,2,3,4))
        assertEquals(43210, res)
    }

    @Test
    fun `Max thruster signal 54321`(){
        val list = "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0".split(',').map { it.toInt() }
        val res = maxThrust(list, listOf(0,1,2,3,4))
        assertEquals(54321, res)
    }

    @Test
    fun `Max thruster signal 65210`(){
        val list = "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0".split(',').map { it.toInt() }
        val res = maxThrust(list, listOf(0,1,2,3,4))
        assertEquals(65210, res)
    }

    @Test
    fun `54321 (from phase setting sequence 0,1,2,3,4`(){
        val list = "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0".split(',').map { it.toInt() }
        val res = thrust(list, listOf(0,1,2,3,4))
        assertEquals(54321, res)
    }

    @Test
    fun `139629729 (from phase setting sequence 9,8,7,6,5)`(){
        val list = "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5".split(',').map { it.toInt() }
        val res = thrust(list, listOf(9,8,7,6,5))
        assertEquals(139629729, res)
    }
}