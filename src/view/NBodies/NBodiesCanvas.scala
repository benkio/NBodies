package view.NBodies

import scala.swing.Panel
import java.awt.{ Color, Graphics2D }
import model.body.Body
import model.Constraints
import java.awt.Dimension


class NBodiesCanvas() extends Panel with INBodiesCanvas {
  
  var bodies = List[Body]()
  override def paintComponent(g: Graphics2D) {
    
    // Start by erasing this Canvas
    g.clearRect(0, 0, size.width, size.height)
    
    // Draw background here
    g.setColor(Constraints.canvasBackgroundColor )
    
    // Draw things that change on top of background
    for (body <- bodies) {
      g.setColor(Constraints.bodiesColor)
      g.fillOval(body.x, body.y, 10, 10)
    }
  }

  /** Add a "dart" to list of things to display */
  def addBody(body: Body) {
    bodies = bodies :+ body
    repaint()
  }
  
  def removeBody(body: Body) = {
    val (left, right) = bodies.span(_ != body)
    bodies = left ::: right.drop(1)
    repaint()
  }
  
}