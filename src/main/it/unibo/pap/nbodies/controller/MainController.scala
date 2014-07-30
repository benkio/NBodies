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
import it.unibo.pap.nbodies.model.messages.Messages._
import it.unibo.pap.nbodies.model.force.ForceCalculator
import main.it.unibo.pap.nbodies.controller.Implicit
import scala.collection.mutable.ListBuffer

class MainController(bodiesNumber: Int, deltaTime: Int, painter: ActorPath, forceCalculator: ActorPath) extends Actor {
  implicit val ec = Implicit.ec
  implicit lazy val timeout = Implicit.timeout
  var currentBodiesNumber = bodiesNumber
  var currentDeltaTime = deltaTime
  var bodiesPositionUpdated = ListBuffer[(Point2D, Double)]()
  createBodies(bodiesNumber)
  context.actorSelection(painter) ! PaintObj(getBodiesDetailsList().to[ListBuffer])

  /**
   * TODO: Implement
   */
  override def receive = {
    case StartSimultation(deltaTime) => {
      println("MainController: Start Button Pressed")
      currentDeltaTime = deltaTime
      self ! OneStep(deltaTime)
    }
    case Stop => {
      println("MainController: Stop Button Pressed")
      context.children.foreach(body => body ! Stop)
    }
    case Reset => {
      println("MainController: Reset Button Pressed")
      context.children.foreach(body => body ! Reset)
    }
    case OneStep(deltaTime) => {
      currentDeltaTime = deltaTime
      println("MainController: One Step Button Pressed")
      context.children.foreach(body => body ! OneStep(deltaTime))
    }
    case PositionUpdated(bodyDetail) => {
      if (bodiesPositionUpdated.length == bodiesNumber) bodiesPositionUpdated.clear()
      bodiesPositionUpdated.append(bodyDetail)
      if (bodiesPositionUpdated.length == bodiesNumber) {
        context.actorSelection(painter) ! PaintObj(bodiesPositionUpdated)
      }
    }
  }

  private def createBodies(bodiesNumber: Int) = {
    for (i <- 1 to bodiesNumber) context.actorOf(Props(new Body(forceCalculator)))
  }

  private def getBodyDetailsFuture(body: ActorRef) = {
    val bodyDetailsFuture = for {
      a <- ask(body, GetXCoordinate).mapTo[Double]
      b <- ask(body, GetYCoordinate).mapTo[Double]
      c <- ask(body, GetRadius).mapTo[Double]
    } yield (new Point2D.Double(a, b), c)

    bodyDetailsFuture
  }

  private def getBodiesDetailsList(): List[(Point2D, Double)] = {
    var bodyDetailsList = List[(Point2D, Double)]()
    context.children.foreach(body => {
      bodyDetailsList ::= Await.result(getBodyDetailsFuture(body), timeout.duration)
    })
    bodyDetailsList
  }
}
