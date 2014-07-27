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

class MainController(bodiesNumber: Int, deltaTime: Int, painter: ActorPath) extends Actor {
  implicit val ec = Implicit.ec
  implicit lazy val timeout = Implicit.timeout
  var currentBodiesNumber = bodiesNumber
  var currentDeltaTime = deltaTime
  val painterRef = painter
  var i = 0
  createBodies(bodiesNumber)
  context.actorSelection(painterRef) ! PaintObj(getBodiesDetailsList())
  val forceCalculator = context.actorOf(Props(new ForceCalculator(bodiesNumber)), "forceCalculator")

  /**
   * TODO: Implement
   */
  override def receive = {
    case StartSimultation(deltaTime) => {
      println("MainController: Start Button Pressed")
      currentDeltaTime = deltaTime
    }
    case Stop => {
      println("MainController: Stop Button Pressed")
      context.children.foreach(body => body ! Stop)
    }
    case Reset => println("MainController: Reset Button Pressed")
    case OneStep(deltaTime) => {
      println("MainController: One Step Button Pressed")
      currentDeltaTime = deltaTime
      context.children.foreach(body => body ! OneStep(deltaTime))
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
