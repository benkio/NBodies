package it.unibo.pap.nbodies.model.body

import java.awt.geom.Point2D
import scala.util.Random
import java.awt.Point
import it.unibo.pap.nbodies.model.messages.Messages._
import it.unibo.pap.nbodies.controller.Implicit
import scala.concurrent._
import scala.util.{ Success, Failure }
import akka.actor.ActorIdentity
import akka.actor.ActorRef
import akka.actor.ActorSelection
import akka.actor.ActorSystem
import akka.actor.Identify
import akka.actor.Actor
import akka.pattern._
import akka.actor.Props
import akka.util.Timeout
import akka.actor.ActorPath
import it.unibo.pap.nbodies.view._
import it.unibo.pap.nbodies.model.ModelConstants
import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import akka.actor.Cancellable
import it.unibo.pap.nbodies.model.BodyDetails

class Body(forceCalculatorActorPath: ActorPath, painterPath: ActorPath) extends Actor {

  implicit val ec = Implicit.ec
  implicit lazy val timeout = Implicit.timeout
  var currentDeltaTime = 0
  var bodyDetails = new BodyDetails(new Point2D.Double(
    10 + Random.nextDouble() * (ViewConstants.CanvasDimension.getWidth() - 20),
    10 + Random.nextDouble() * (ViewConstants.CanvasDimension.getHeight() - 20)),
    Random.nextDouble() * ModelConstants.massMultiplier)
  var force = ModelConstants.initialForce
  var velocity = ModelConstants.initialVelocity
  val forceCalculatorRef = context.system.actorSelection(forceCalculatorActorPath)

  /**
   * TODO: Implement the receive for the body
   */
  override def receive = {
    case GetBodyDetails => sender() ! bodyDetails
    case Stop => {
      println("Body: Stop Event")
      context.become(StopMode)
    }
    case Reset => {
      println("Body: Reset Event")
      context.become(StopMode)
    }
    case OneStep(deltaTime) => {
      println("Body: One Step with " ++ deltaTime.toString() ++ " deltaTime")
      currentDeltaTime = deltaTime
      forceCalculatorRef ! CalculateForce(bodyDetails.coordinate, bodyDetails.mass)

    }
    case Force(newForce) => {
      force = newForce
      println("Body(" ++ bodyDetails.coordinate.getX().toString() ++ ", " ++ bodyDetails.coordinate.getY().toString() ++ ") new force calculate of x:" ++ force.getX().toString() ++ " y:" ++ force.getY().toString())
      computeNewPosition()
      println("New Position Body(" ++ bodyDetails.coordinate.getX().toString() ++ ", " ++ bodyDetails.coordinate.getY().toString() ++ ") ")
      context.actorSelection(painterPath) tell (PaintBody(bodyDetails), self)
      context.parent.tell(StepFinished, self)
    }
  }

  def StopMode: Receive = {
    case OneStep(deltaTime) => {
      context.unbecome();
      self forward OneStep(deltaTime)
    }
    case StartSimultation(deltaTime) => {
      context.unbecome();
      self forward StartSimultation(deltaTime)
    }
    case Reset => {
      resetInternalValues()
      println("Body(" ++ bodyDetails.coordinate.getX().toString() ++ ", " ++ bodyDetails.coordinate.getY().toString() ++ ") new force calculate of x:" ++ force.getX().toString() ++ " y:" ++ force.getY().toString())
      context.actorSelection(painterPath) tell (PaintBody(bodyDetails), self)
      context.actorSelection(painterPath) tell (DrawFrame, self)
    }
    case _ => unhandled()
  }

  private def computeNewPosition() {
    bodyDetails.coordinate.x += velocity.getX() * currentDeltaTime + (math.pow(currentDeltaTime, 2) / 2) * force.getX()
    bodyDetails.coordinate.y += velocity.getY() * currentDeltaTime + (math.pow(currentDeltaTime, 2) / 2) * force.getY()

    velocity.x += currentDeltaTime * (force.getX() / bodyDetails.mass)
    velocity.y += currentDeltaTime * (force.getY() / bodyDetails.mass)

    if (bodyDetails.coordinate.getX() > ViewConstants.CanvasDimension.getWidth()) {
      bodyDetails.coordinate.x = ViewConstants.CanvasDimension.getWidth() - 10
      velocity.x = (-velocity.x)
    }
    if (bodyDetails.coordinate.getY() > ViewConstants.CanvasDimension.getHeight()) {
      bodyDetails.coordinate.y = ViewConstants.CanvasDimension.getHeight() - 10
      velocity.y = (-velocity.y)
    }
    if (bodyDetails.coordinate.getX() < 0) {
      bodyDetails.coordinate.x = 10
      velocity.x = (-velocity.x)
    }
    if (bodyDetails.coordinate.getY() < 0) {
      bodyDetails.coordinate.y = 10
      velocity.y = (-velocity.y)
    }
  }

  private def resetInternalValues() {
    bodyDetails.mass = Random.nextDouble() * ModelConstants.massMultiplier
    bodyDetails.coordinate = new Point2D.Double(10 + Random.nextDouble() * (ViewConstants.CanvasDimension.getWidth() - 20), 10 + Random.nextDouble() * (ViewConstants.CanvasDimension.getHeight() - 20))
    force = ModelConstants.initialForce
    velocity = ModelConstants.initialVelocity
  }
}