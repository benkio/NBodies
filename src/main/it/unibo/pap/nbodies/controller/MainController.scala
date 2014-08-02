package it.unibo.pap.nbodies.controller

import akka.actor._
import akka.util._
import java.awt.geom.Point2D
import java.awt.Point
import akka.dispatch.Futures
import scala.concurrent.Await
import akka.pattern.{ ask, pipe }
import akka.util.Timeout
import scala.concurrent._
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit
import it.unibo.pap.nbodies.model.BodyDetails
import it.unibo.pap.nbodies.model.messages.Messages._
import it.unibo.pap.nbodies.model.body.Body
import it.unibo.pap.nbodies.model.ModelConstants

class MainController(bodiesNumber: Int, deltaTime: Int, painter: ActorPath, forceCalculator: ActorPath, collisionCalculator: ActorPath) extends Actor {
  implicit val ec = Implicit.ec
  implicit lazy val timeout = Implicit.timeout
  var currentBodiesNumber = bodiesNumber
  var bodiesNumberStepTerminated = currentBodiesNumber
  var currentDeltaTime = deltaTime
  var startMode = false
  val painterRef = context.system.actorSelection(painter).resolveOne()

  createBodies(currentBodiesNumber)
  context.actorSelection(painter) ! SetBodiesDetails(getBodiesDetailsList())
  /**
   * TODO: Implement
   */
  override def receive = {
    case StartSimultation(deltaTime) => {
      println("MainController: Start Button Pressed")
      currentDeltaTime = deltaTime
      startMode = true
      context.actorSelection(forceCalculator) ! StartSimultation
      context.actorSelection(collisionCalculator) ! StartSimultation
      self ! OneStep(deltaTime)
    }
    case Stop => {
      println("MainController: Stop Button Pressed")
      startMode = false
      context.actorSelection(forceCalculator) ! Stop
      context.actorSelection(collisionCalculator) ! Stop
      context.children.foreach(body => body ! Stop)
    }
    case Reset => {
      println("MainController: Reset Button Pressed")
      currentBodiesNumber = bodiesNumber
      context.actorSelection(collisionCalculator) ! SetBodiesNumber(bodiesNumber)
      context.actorSelection(forceCalculator) ! SetBodiesNumber(bodiesNumber)
      context.actorSelection(forceCalculator) ! Reset
      context.actorSelection(collisionCalculator) ! Reset
      startMode = false
      context.children.foreach(body => body ! Reset)
    }
    case OneStep(deltaTime) => {
      currentDeltaTime = deltaTime
      bodiesNumberStepTerminated = currentBodiesNumber
      context.actorSelection(forceCalculator) ! OneStep
      context.actorSelection(collisionCalculator) ! OneStep
      println("MainController: One Step Button Pressed")
      context.children.foreach(body => body ! OneStep(deltaTime))
    }
    case StepFinished => {
      bodiesNumberStepTerminated -= 1
      if (bodiesNumberStepTerminated == 0) {
        context.actorSelection(collisionCalculator) ! SetBodiesNumber(currentBodiesNumber)
        context.actorSelection(forceCalculator) ! SetBodiesNumber(currentBodiesNumber)
        Await.result(painterRef, 5 second) ! DrawFrame
        if (startMode) context.system.scheduler.scheduleOnce(ModelConstants.startNextStepDelay, self, OneStep(currentDeltaTime))
      }
    }
    case DecreaseBodiesNumber => {
      currentBodiesNumber -= 1
    }
  }

  private def createBodies(bodiesNumber: Int) = {
    for (i <- 1 to bodiesNumber) context.actorOf(Props(new Body(forceCalculator, painter, collisionCalculator)))
  }

  private def getBodiesDetailsList() = {
    var bodyDetailsList = List[(BodyDetails, ActorRef)]()
    context.children.foreach(body => {
      val future = (body ? GetBodyDetails)
      bodyDetailsList ::= (Await.result(future, timeout.duration).asInstanceOf[BodyDetails], body)
    })
    bodyDetailsList
  }
}
