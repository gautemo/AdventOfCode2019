import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.io.File
import javax.swing.JFrame
import javax.swing.JPanel

class SpaceImage(input: String, private val width: Int, private val height: Int){
    private val layers: List<Layer> = input.chunked(width*height){ Layer(it) }

    fun multiplyDigitsOnLeastZeros(digit1: Int, digit2: Int) = layers.minBy { it.zeros() }?.multiplyNrDigits(digit1, digit2)

    fun drawImage(){
        val scale = 10
        val img = Img(layers, width, height, scale)

        val frame = JFrame()
        frame.isVisible = true
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane.add(img)
        frame.pack()
    }
}

class Layer(val input: CharSequence){
    fun zeros() = input.count { it == '0' }

    fun multiplyNrDigits(digit1: Int, digit2: Int) = input.count { it.toString().toInt() == digit1 } * input.count { it.toString().toInt() == digit2 }
}

class Img(private val layers: List<Layer>, private val w: Int, private val h: Int, private val scale: Int) : JPanel() {
    init {
        preferredSize = Dimension(w*scale, h*scale)
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        g?.run{
            for(x in 0 until w){
                for(y in 0 until h){
                    val useColor = findColor(x, y)
                    useColor?.let {
                        color = it
                        fillRect(x*scale, y*scale, scale, scale)
                    }
                }
            }
        }
    }

    private fun findColor(x: Int, y: Int): Color?{
        for(layer in layers){
            when(layer.input[y*w + x]){
                '1' -> return Color.black
                '0' -> return Color.white
            }
        }
        return null
    }
}

fun main(){
    val input = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText().trim()
    val spaceImage = SpaceImage(input, 25, 6)
    val res = spaceImage.multiplyDigitsOnLeastZeros(1, 2)
    println(res)
    spaceImage.drawImage()
}