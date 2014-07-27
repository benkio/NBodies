/**
 *
 */
package main.it.unibo.pap.nbodies.view.nbodies

import java.awt.Dimension
import scala.swing._
import java.text.NumberFormat
import java.awt.Color
import akka.actor.ActorSystem
import akka.actor.Props
import scala.swing.event.ButtonClicked
import scala.actors.Exit
import javax.swing.WindowConstants
import java.awt.geom.Point2D
import javax.swing.JSpinner
import scala.swing.event.ValueChanged
import main.it.unibo.pap.nbodies.controller._
import main.it.unibo.pap.nbodies.model.messages.Messages._

/**
 * @author enricobenini
 *
 */
object NBodies extends Frame {
  def startNBodies(bodiesNumber: Int, deltaTime: Int) = new Frame {
    visible = true
    title = "NBodies Frame"
    size = new Dimension(1400, 800)
    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

    val deltaTimeTextField = new JSpinner()
    deltaTimeTextField.setModel(new javax.swing.SpinnerNumberModel(0, 0, 1000000000, 1))
    deltaTimeTextField.setValue(deltaTime)
    val deltaTimeSpinnerWrapped = Component.wrap(deltaTimeTextField)
    val canvas = new NBodiesCanvas() {
      preferredSize = new Dimension(1200, 600)
    }

    var actorSystem = ActorSystem("actorSystem")
    var painter = actorSystem.actorOf(Props(new Painter(canvas)), "Painter")
    var mainController = actorSystem.actorOf(Props(new MainController(bodiesNumber, deltaTime, painter.path)), "mainController")

    contents = new BoxPanel(Orientation.Vertical) {
      border = Swing.EmptyBorder(10, 20, 10, 20)
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += Button("Start")(mainController ! StartSimultation(deltaTimeTextField.getValue().asInstanceOf[Int]))
        contents += Button("One Step")(mainController ! OneStep(deltaTimeTextField.getValue().asInstanceOf[Int]))
        contents += Button("Stop")(mainController ! Stop)
        contents += Button("Reset")(mainController ! Reset)
        contents += deltaTimeSpinnerWrapped
      }

      contents += canvas
    }
  }
}