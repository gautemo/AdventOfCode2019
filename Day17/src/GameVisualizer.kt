import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.GridLayout
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

class GameVisualizer(val move: (Int) -> Unit) : KeyListener{
    private val frame = JFrame()
    private val img = Img(listOf(), 10)

    init {
        frame.contentPane.add(img)
        frame.addKeyListener(this)
        frame.isFocusable = true

        frame.setSize(600, 1000)
        frame.isVisible = true
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    }

    fun drawImage(panels: List<Point>){
        img.panels = panels
        img.repaint()
    }

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyPressed(e: KeyEvent?) {
        when(e?.keyCode){
            KeyEvent.VK_LEFT -> move(3)
            KeyEvent.VK_DOWN -> move(2)
            KeyEvent.VK_RIGHT -> move(4)
            KeyEvent.VK_UP -> move(1)
        }
    }

    override fun keyReleased(e: KeyEvent?) {}
}

class Img(var panels: List<Point>, private val scale: Int) : JPanel() {

    init{
        font = Font("TimesRoman", Font.PLAIN, 40)
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        background = Color.pink

        val w = panels.map { it.x.toInt() }.max()?.plus(1) ?: 0
        val h = panels.map { it.y.toInt() }.max()?.plus(1) ?: 0

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
            panels.firstOrNull { it.tile == Tile.DROID }?.let{
                color = Color.orange
                fillRect(it.x.toInt()*scale, it.y.toInt()*scale, scale, scale)
            }
        }
    }

    private fun findColor(x: Int, y: Int): Color?{
        return when(panels.lastOrNull { it.x.toInt() == x && it.y.toInt() == y }?.tile){
            Tile.DROID -> Color.orange
            Tile.SPACE -> Color.black
            Tile.SCAFFOLD -> Color.gray
            else -> null
        }
    }
}