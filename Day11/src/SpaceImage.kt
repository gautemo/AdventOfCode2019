import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JFrame
import javax.swing.JPanel

class SpaceImage(private val panels: Map<Panel, Int>){

    fun drawImage(){
        val scale = 10
        val img = Img(panels, scale)

        val frame = JFrame()
        frame.isVisible = true
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane.add(img)
        frame.pack()
    }
}

class Img(private val panels: Map<Panel, Int>, private val scale: Int) : JPanel() {
    private val w = panels.map { it.key.x }.max()!! + 1
    private val h = panels.map { it.key.y }.max()!! + 1

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
        return if(panels.containsKey(Panel(x,y)) && panels[Panel(x,y)] == 1) Color.white else Color.black
    }
}