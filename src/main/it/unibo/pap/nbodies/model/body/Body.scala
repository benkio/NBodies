package it.unibo.pap.nbodies.model.body

import java.awt.geom.Point2D
import scala.util.Random
import java.awt.Point
import it.unibo.pap.nbodies.model.messages.Messages._
import main.it.unibo.pap.nbodies.controller.Implicit
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
import it.unibo.pap.nbodies.model.force.ForceCalculator
import akka.actor.ActorPath
import main.it.unibo.pap.nbodies.view.ViewConstants
import main.it.unibo.pap.nbodies.model.ModelConstants

class Body(forceCalculatorActorPath: ActorPath) extends Actor {

  implicit val ec = Implicit.ec
  implicit lazy val timeout = Implicit.timeout
  var currentDeltaTime = 0
  var mass = Random.nextDouble() * ModelConstants.massMultiplier
  var radius = () => mass / ModelConstants.radiusDivider
  var coordinate = new Point2D.Double(10 + Random.nextDouble() * (ViewConstants.CanvasDimension.getWidth() - 20), 10 + Random.nextDouble() * (ViewConstants.CanvasDimension.getHeight() - 20))
  var force = ModelConstants.initialForce
  var velocity = ModelConstants.initialVelocity
  val forceCalculatorRef = context.system.actorSelection(forceCalculatorActorPath)

  /**
   * TODO: Implement the receive for the body
   */
  override def receive = {
    case GetXCoordinate => sender() ! coordinate.getX()
    case GetYCoordinate => sender() ! coordinate.getY()
    case GetRadius => sender ! radius()
    case Stop => {
      println("Body: Stop Event")
      context.become(StopMode)
    }
    case Reset => {
      println("Body: Reset Event")
      resetInternalValues()
      context.become(StopMode)
      context.parent.tell(PositionUpdated((coordinate, radius())), self)
    }
    case OneStep(deltaTime) => {
      println("Body: One Step with " ++ deltaTime.toString() ++ " deltaTime")
      currentDeltaTime = deltaTime
      forceCalculatorRef ! CalculateForce(coordinate, mass)
    }
    case Force(newForce) => {
      force = newForce
      println("Body(" ++ coordinate.getX().toString() ++ ", " ++ coordinate.getY().toString() ++ ") new force calculate of x:" ++ force.getX().toString() ++ " y:" ++ force.getY().toString())
      computeNewPosition()
      println("New Position Body(" ++ coordinate.getX().toString() ++ ", " ++ coordinate.getY().toString() ++ ") ")
      context.parent.tell(PositionUpdated((coordinate, radius())), self)
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
    case _ => unhandled()
  }

  private def computeNewPosition() {
    coordinate.x += velocity.getX() * currentDeltaTime + (math.pow(currentDeltaTime, 2) / 2) * force.getX()
    coordinate.y += velocity.getY() * currentDeltaTime + (math.pow(currentDeltaTime, 2) / 2) * force.getY()

    velocity.x += currentDeltaTime * (force.getX() / mass)
    velocity.y += currentDeltaTime * (force.getY() / mass)

    if (coordinate.getX() > ViewConstants.CanvasDimension.getWidth()) {
      coordinate.x = ViewConstants.CanvasDimension.getWidth() - 10
      velocity.x = (-velocity.x)
    }
    if (coordinate.getY() > ViewConstants.CanvasDimension.getHeight()) {
      coordinate.y = ViewConstants.CanvasDimension.getHeight() - 10
      velocity.y = (-velocity.y)
    }
    if (coordinate.getX() < 0) {
      coordinate.x = 10
      velocity.x = (-velocity.x)
    }
    if (coordinate.getY() < 0) {
      coordinate.y = 10
      velocity.y = (-velocity.y)
    }
  }

  private def resetInternalValues() {
    mass = Random.nextDouble() * ModelConstants.massMultiplier
    coordinate = new Point2D.Double(10 + Random.nextDouble() * (ViewConstants.CanvasDimension.getWidth() - 20), 10 + Random.nextDouble() * (ViewConstants.CanvasDimension.getHeight() - 20))
  }
}