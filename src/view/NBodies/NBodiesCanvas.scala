package view.NBodies

import scala.swing.Panel
import java.awt.{ Color, Graphics }
import model.body.Body
import java.awt.Dimension
import java.awt.geom.Ellipse2D
import java.awt.Graphics2D

class NBodiesCanvas() extends Panel{
  
  var bodies = List[Body]()
  
  override def paintComponent(g: Graphics2D) {
    
    // Start by erasing this Canvas
    g.clearRect(0, 0, size.width, size.height)
    
    // Draw things that change on top of background
    for (body <- bodies) {
      if (body.modified){
    	  g.setColor(Color.WHITE)
    	  drawCircle(g, body.coordinate.getX(), body.coordinate.getY(), body.radius())
      }
    }
  }
  
  def drawCircle(cg:Graphics2D,xCenter:Double, yCenter:Double, radius:Double) {
    val ellipse = new Ellipse2D.Double(xCenter-radius, yCenter-radius, 2*radius, 2*radius);
    cg.draw(ellipse);
  }
  def setBodiesList(bodies : List[Body]) = this.bodies = bodies 
}