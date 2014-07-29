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

class Body(forceCalculatorActorPath: ActorPath) extends Actor {

  implicit val ec = Implicit.ec
  implicit lazy val timeout = Implicit.timeout
  var mass = Random.nextDouble() * 10
  var radius = () => mass
  var coordinate = new Point2D.Double(10 + Random.nextDouble() * 1180, 10 + Random.nextDouble() * 580)
  var force = new Point2D.Double(0, 0)
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
    case OneStep(deltaTime) => {
      println("Body: One Step with " ++ deltaTime.toString() ++ " deltaTime")
      forceCalculatorRef ! CalculateForce(coordinate, mass)
    }
    case Force(newForce) => {
      force = newForce
      println("Body(" ++ coordinate.getX().toString() ++ ", " ++ coordinate.getY().toString() ++ ") new force calculate of x:" ++ force.getX().toString() ++ " y:" ++ force.getY().toString())
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

}