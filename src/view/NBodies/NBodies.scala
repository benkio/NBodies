/**
 *
 */
package view.NBodies

import java.awt.Dimension
import scala.swing._
import java.text.NumberFormat
import controller.Painter
import java.awt.Color
import controller.MainController
import akka.actor.ActorSystem

/**
 * @author enricobenini
 *
 */
object NBodies extends Frame {
	def startNBodies(bodiesNumber:Int,deltaTime:Int) = new Frame{
	  visible = true
	  title = "NBodies Frame"
	  size = new Dimension(700,700)
	  
	  val startButton = new Button("Start")
	  val stopButton = new Button("Start")
	  val resetButton = new Button("Start")
	  val deltaTimeTextField = new FormattedTextField(NumberFormat.getInstance()) 
	  
	  var actorSystem = ActorSystem("actorSystem")
	  var mainController = actorSystem.actorOf(MainController.Props(bodiesNumber, deltaTime))
	  
	  val canvas = new NBodiesCanvas(){
	  preferredSize = new Dimension(500,500)
	  background_=(Color.BLACK)
	  }
	  
	  contents = new BoxPanel(Orientation.Vertical ){
	    border = Swing.EmptyBorder(10,20,10,20)
	    contents += new FlowPanel{
	    	contents += startButton 
	    	contents += stopButton 
	    	contents += resetButton
	    	contents += deltaTimeTextField 
	    }
	    
	    contents += canvas 
	  } 
	}
}