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
import it.unibo.pap.nbodies.model.calculators.ForceCalculator
import it.unibo.pap.nbodies.model.messages.Messages._
import it.unibo.pap.nbodies.controller.Implicit
import it.unibo.pap.nbodies.view.ViewConstants
import it.unibo.pap.nbodies.controller._

/**
 * @author enricobenini
 *
 */
object NBodies extends Frame {
  implicit val ec = Implicit.ec
  implicit lazy val timeout = Implicit.timeout
  def startNBodies(bodiesNumber: Int, deltaTime: Int) = new Frame {
    visible = true
    title = "NBodies Frame"
    size = ViewConstants.FrameDimension
    peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

    val deltaTimeTextField = new JSpinner()
    deltaTimeTextField.setModel(new javax.swing.SpinnerNumberModel(0, 0, 1000000000, 1))
    deltaTimeTextField.setValue(deltaTime)
    val deltaTimeSpinnerWrapped = Component.wrap(deltaTimeTextField)
    val canvas = new NBodiesCanvas() {
      preferredSize = ViewConstants.CanvasDimension
    }

    var actorSystem = ActorSystem("actorSystem")
    var painter = actorSystem.actorOf(Props(new Painter(canvas)), "Painter")
    var forceCalculator = actorSystem.actorOf(Props(new ForceCalculator(bodiesNumber)), "forceCalculator")
    var mainController = actorSystem.actorOf(Props(new MainController(bodiesNumber, deltaTime, painter.path, forceCalculator.path)), "mainController")

    contents = new BoxPanel(Orientation.Vertical) {
      border = Swing.EmptyBorder(10, 20, 10, 20)
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += Button("Start")({
          forceCalculator ! StartSimultation
          mainController ! StartSimultation(deltaTimeTextField.getValue().asInstanceOf[Int])
        })
        contents += Button("One Step")({
          forceCalculator ! OneStep
          mainController ! OneStep(deltaTimeTextField.getValue().asInstanceOf[Int])
        })
        contents += Button("Stop")({
          forceCalculator ! Stop
          mainController ! Stop
        })
        contents += Button("Reset")({
          forceCalculator ! Reset
          mainController ! Reset
        })
        contents += deltaTimeSpinnerWrapped
      }

      contents += canvas
    }
  }
}