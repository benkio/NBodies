package it.unibo.pap.nbodies.model.calculators

import akka.actor.Actor
import scala.collection.mutable.ListBuffer
import akka.actor.ActorRef
import java.awt.geom.Point2D
import it.unibo.pap.nbodies.model.messages.Messages._

class Collision(bodiesNumber: Int) extends Actor {
  // ActorRef to reply, Coordinate, radius
  var bodiesDetails = new ListBuffer[(ActorRef, Point2D.Double)]()

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
      if (bodiesDetails.length < bodiesNumber) {
        //        val force = compute(coordinate, mass)
        //        bodiesDetails.append((sender(), coordinate, mass, force))
        //        if (bodiesDetails.length == bodiesNumber) self ! sendForcesResults
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
    case sendForcesResults => {
      //      bodiesDetails.foreach(body => { body._1 ! Force(body._4) })
      bodiesDetails.clear()
    }
  }

}