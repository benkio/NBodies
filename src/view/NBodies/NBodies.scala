/**
 *
 */
package view.NBodies

import scala.swing.Frame
import scala.swing.BorderPanel
import scala.swing.Label
import scala.swing.BorderPanel.Position._
import java.awt.Dimension
/**
 * @author enricobenini
 *
 */
object NBodies extends Frame {
	def startNBodies = new Frame{
	  visible = true
	  title = "NBodies Frame"
	  size = new Dimension(700,700)
	  val canvas = new NBodiesCanvas()
	  canvas.preferredSize = new Dimension(500,500)
	  
	  contents = new BorderPanel{
	    layout(new Label("Hi")) = North 
	    layout(canvas) = South 
	  } 
	}
}