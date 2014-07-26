package view.configuration

import scala.swing._
import scala.swing.BorderPanel.Position._
import scala.swing.FormattedTextField
import event._
import java.awt.Color
import scala.util.Random
import javax.swing.JFormattedTextField
import java.text.NumberFormat
import view.NBodies.NBodies


object Configuration extends SimpleSwingApplication {

  def top = new MainFrame { // top is a required method
	  title = "Scala NBodies Configurator"
	  size = new Dimension(300,120)
	    
	  val nBodiesLabel = new Label("Start number of bodies:")  
	  val nBodiesTextField = new FormattedTextField(NumberFormat.getInstance())
	  val deltaTimeLabel = new Label("Set the delta time:")
	  val deltaTimeTextField = new FormattedTextField(NumberFormat.getInstance())
	  val startButton = new Button("Start System")
	  	  
	  val configPanel = new GridPanel(2,2) {
      contents += nBodiesLabel 
      contents += nBodiesTextField 
      contents += deltaTimeLabel 
      contents += deltaTimeTextField 
	  }
	  
	  contents = new BorderPanel{
	    border = Swing.EmptyBorder(10, 20, 10, 20)
	    layout(configPanel) = North 
	    layout(startButton) = South
	  }
	  	  
	  listenTo(startButton)
	  
	  reactions += {
	    case ButtonClicked(b) => {
	      this.dispose
	      NBodies.startNBodies(nBodiesTextField.text.toInt, deltaTimeTextField.text.toInt)
	      }
	  }
  }
}