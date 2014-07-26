package it.unibo.pap.nbodies.controller

import akka.actor._
import akka.util._
import java.awt.geom.Point2D
import java.awt.Point
import it.unibo.pap.nbodies.model._
import it.unibo.pap.nbodies.model.body.Body
import akka.dispatch.Futures
import scala.concurrent.Await
import akka.pattern.{ ask, pipe }
import akka.util.Timeout
import scala.concurrent._
import it.unibo.pap.nbodies.model.Messages.Messages._

class MainController(bodiesNumber: Int, deltaTime: Int, painter: ActorPath) extends Actor with IMainController {
  implicit val ec = ExecutionContext.Implicits.global
  implicit lazy val timeout = Timeout(5000)

  val initialBodiesNumber = bodiesNumber
  val initialDeltaTime = deltaTime
  val painterRef = painter
  var bodies = List[ActorRef]()
  var i = 0
  createAndPaintBodies()

  def removeBody(body: ActorRef) = {
    val (left, right) = bodies.span(_ != body)
    bodies = left ::: right.drop(1)
  }
  /**
   * TODO: Implement
   */
  override def receive = {
    case "Start Button Pressed" => {
      println("Start Button Pressed")

    }
    case "Stop Button Pressed" => println("Stop Button Pressed")
    case "Reset Button Pressed" => println("Reset Button Pressed")
    case "Delta Time Changed" => println("Delta Time Changed")
  }

  private def createAndPaintBodies() = {
    var bodyDetailsList = List[(Point2D, Double)]()
    for (i <- 1 to bodiesNumber) {
      var body = context.actorOf(Props[Body])
      val bodyDetailsFuture = for {
        a <- ask(body, GetXCoordinate).mapTo[Double]
        b <- ask(body, GetYCoordinate).mapTo[Double]
        c <- ask(body, GetRadius).mapTo[Double]
      } yield (new Point2D.Double(a, b), c)

      bodyDetailsList ::= Await.result(bodyDetailsFuture, timeout.duration)
      context.actorSelection(painterRef) ! PaintObj(bodyDetailsList)
      bodies ::= body
    }
  }
}
