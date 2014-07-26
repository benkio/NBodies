/**
 *
 */
package it.unibo.pap.nbodies.view.nbodies

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
import it.unibo.pap.nbodies.controller.MainController
import it.unibo.pap.nbodies.controller.Painter

/**
 * @author enricobenini
 *
 */
object NBodies extends Frame {
  def startNBodies(bodiesNumber: Int, deltaTime: Int) = new Frame {
    visible = true
    title = "NBodies Frame"
    size = new Dimension(700, 700)
    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

    var actorSystem = ActorSystem("actorSystem")
    var mainController = actorSystem.actorOf(Props(new MainController(bodiesNumber, deltaTime)), "bello")
    var painter = actorSystem.actorOf(Props(new Painter(canvas)), "test")

    val deltaTimeTextField = new JSpinner()
    deltaTimeTextField.setModel(new javax.swing.SpinnerNumberModel(0, 0, 1000000000, 1))
    val deltaTimeSpinnerWrapped = Component.wrap(deltaTimeTextField)
    val canvas = new NBodiesCanvas() {
      preferredSize = new Dimension(500, 500)
    }

    contents = new BoxPanel(Orientation.Vertical) {
      border = Swing.EmptyBorder(10, 20, 10, 20)
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += Button("Start")(mainController ! "Start Button Pressed")
        contents += Button("Stop")(mainController ! "Stop Button Pressed")
        contents += Button("Reset")(mainController ! "Reset Button Pressed")
        contents += deltaTimeSpinnerWrapped
      }

      contents += canvas
    }

    listenTo(deltaTimeSpinnerWrapped)
    reactions += {
      case ValueChanged(deltaTimeSpinnerWrapped) => mainController ! "Delta Time Changed"
    }

  }
}