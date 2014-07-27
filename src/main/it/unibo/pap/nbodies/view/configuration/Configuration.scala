package main.it.unibo.pap.nbodies.view.configuration

import scala.swing._
import scala.swing.BorderPanel.Position._
import scala.swing.FormattedTextField
import event._
import java.awt.Color
import scala.util.Random
import javax.swing.JFormattedTextField
import java.text.NumberFormat
import javax.swing.JSpinner
import main.it.unibo.pap.nbodies.view.nbodies.NBodies

object Configuration extends SimpleSwingApplication {

  def top = new MainFrame { // top is a required method
    title = "Scala NBodies Configurator"
    size = new Dimension(300, 120)

    val nBodiesTextField = new JSpinner()
    nBodiesTextField.setModel(new javax.swing.SpinnerNumberModel(0, 0, 1000000000, 1))
    val deltaTimeTextField = new JSpinner()
    deltaTimeTextField.setModel(new javax.swing.SpinnerNumberModel(0, 0, 1000000000, 1))
    val startButton = new Button("Start System")

    val configPanel = new GridPanel(2, 2) {
      contents += new Label("Start number of bodies:")
      contents += Component.wrap(nBodiesTextField)
      contents += new Label("Set the delta time:")
      contents += Component.wrap(deltaTimeTextField)
    }

    contents = new BorderPanel {
      border = Swing.EmptyBorder(10, 20, 10, 20)
      layout(configPanel) = North
      layout(startButton) = South
    }

    listenTo(startButton)

    reactions += {
      case ButtonClicked(b) => {
        this.close()
        NBodies.startNBodies(nBodiesTextField.getValue().asInstanceOf[Int], deltaTimeTextField.getValue().asInstanceOf[Int])
      }
    }
  }
  def CloseApplication() = quit()
}