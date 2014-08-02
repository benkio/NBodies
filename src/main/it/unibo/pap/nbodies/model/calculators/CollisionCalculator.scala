package it.unibo.pap.nbodies.model.calculators

import akka.actor.Actor
import scala.collection.mutable.ListBuffer
import akka.actor.ActorRef
import java.awt.geom.Point2D
import it.unibo.pap.nbodies.model.messages.Messages._
import it.unibo.pap.nbodies.utility.PhysicalEngine

class CollisionCalculator(bodiesNumber: Int) extends Actor {
  // ActorRef to reply, coordinate, radius, flag collided
  var bodiesDetails = new ListBuffer[(ActorRef, Point2D.Double, Double, CollidedFlag)]()
  var currentBodiesNumber = bodiesNumber
  override def receive = {
    case StartSimultation => context.become(Ready)
    case OneStep => context.become(Ready)
    case _ => {
      println("message refused")
      unhandled()
    }
  }

  //TODO: check collisions
  def Ready: Receive = {
    case CalculateCollision(coordinate, radius) => {
      if (bodiesDetails.length < currentBodiesNumber) {
        val collided = compute(coordinate, radius)
        bodiesDetails.append((sender(), coordinate, radius, new CollidedFlag(collided)))
        if (bodiesDetails.length == currentBodiesNumber) self ! SendCollisionResults
      } else unhandled(CalculateCollision)
    }
    case Stop => {
      bodiesDetails.clear
      context unbecome
    }
    case Reset => {
      bodiesDetails.clear
      context unbecome
    }
    case SendCollisionResults => {
      bodiesDetails.foreach(body => {
        if (body._4.collided)
          body._1 ! IsCollided
        else body._1 ! NotCollided
      })
      bodiesDetails.clear()
    }
    case SetBodiesNumber(num) => {
      currentBodiesNumber = num
      bodiesDetails.clear
    }
  }

  private def compute(coordinate: Point2D.Double, radius: Double): Boolean = {
    var BodyCollision = false
    bodiesDetails.foreach(body => {
      val collided = PhysicalEngine.getDistance(coordinate, body._2) <= ((radius + body._3) + (radius + body._3) / 2)
      if (radius < body._3) BodyCollision = collided
      else if (!body._4.collided) body._4.collided = collided
    })
    BodyCollision
  }

}
case class CollidedFlag(value: Boolean) { var collided = value }