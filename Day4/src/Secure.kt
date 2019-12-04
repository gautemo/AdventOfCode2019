
fun main(){
    val chars = 123.toString()
    var count = 0
    for(i in 382345..843167){
        if(isPassword(i)){
            count++
        }
    }
    println(count)
}

fun isPassword(number: Int): Boolean{
    val chars = number.toString()
    var double = false
    for(i in 1 until 6){
        if(chars[i] == chars[i-1] && (i < 5 && chars[i+1] != chars[i])){
            double = true
        }
        if(chars[i].toInt() < chars[i-1].toInt()){
            return false
        }
    }
    if(double){
        println(number)
    }
    return double
}