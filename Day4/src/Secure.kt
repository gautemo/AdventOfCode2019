
fun main(){
    var count = 0
    for(i in 382345..843167){
        if(isPassword(i, true)){
            count++
        }
    }
    println(count)
}

fun isPassword(number: Int, onlyDouble: Boolean): Boolean{
    val chars = number.toString()
    for(i in 1 until 6){
        if(chars[i].toInt() < chars[i-1].toInt()){
            return false
        }
    }
    return if(onlyDouble) hasDouble(chars) else hasRepeating(chars)
}

fun hasDouble(numbers: String) = Regex("""([0-9])\1+""").findAll(numbers).any { it.value.length == 2 }

fun hasRepeating(numbers: String) = !Regex("""([0-9])\1+""").findAll(numbers).none()