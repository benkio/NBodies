package it.unibo.pap.nbodies.model.calculators

import akka.actor.Actor
import scala.collection.mutable.ListBuffer
import akka.actor.ActorRef
import java.awt.geom.Point2D
import it.unibo.pap.nbodies.model.messages.Messages._
import it.unibo.pap.nbodies.utility.PhysicalEngine
import it.unibo.pap.nbodies.model.BodyDetails

class CollisionCalculator(bodiesNumber: Int) extends Actor {
  // ActorRef to reply, coordinate, radius, flag collided
  var bodiesDetails = new ListBuffer[(ActorRef, BodyDetails)]()
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
    case CalculateCollision(bodyDetails) => {
      if (bodiesDetails.length < currentBodiesNumber) {
        bodiesDetails.append((sender(), compute(bodyDetails)))
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
        if (body._2.isCollided)
          body._1 ! IsCollided
        else body._1 ! NotCollided(body._2)
      })
      bodiesDetails.clear()
    }
    case SetBodiesNumber(num) => {
      currentBodiesNumber = num
      bodiesDetails.clear
    }
  }

  private def compute(bodyDetails: BodyDetails): BodyDetails = {
    bodiesDetails.foreach(body => {
      val collided = PhysicalEngine.getDistance(bodyDetails.coordinate, body._2.coordinate) <= ((bodyDetails.radius() + body._2.radius()) * 2)
      if (collided) {
        if (bodyDetails.radius() < body._2.radius()) {
          bodyDetails.isCollided = collided
          body._2.mass += bodyDetails.mass
        } else {
          body._2.isCollided = collided
          bodyDetails.mass += body._2.mass
        }
      }
    })
    bodyDetails
  }
}