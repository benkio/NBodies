package it.unibo.pap.nbodies.model.calculators

import akka.actor.Actor
import java.awt.geom.Point2D
import akka.actor.Props
import akka.actor._
import it.unibo.pap.nbodies.model.messages.Messages._
import scala.collection.mutable.ListBuffer
import it.unibo.pap.nbodies.utility.PhysicalEngine

class ForceCalculator(bodiesNumber: Int) extends Actor {
  // ActorRef to reply, Coordinate, mass, force to calculate
  var bodiesDetails = new ListBuffer[(ActorRef, Point2D.Double, Double, Point2D.Double)]()

  override def receive = {
    case StartSimultation => context.become(Ready)
    case OneStep => context.become(Ready)
    case _ => {
      println("message refused")
      unhandled()
    }
  }

  def Ready: Receive = {
    case CalculateForce(coordinate, mass) => {
      if (bodiesDetails.length < bodiesNumber) {
        val force = compute(coordinate, mass)
        bodiesDetails.append((sender(), coordinate, mass, force))
        if (bodiesDetails.length == bodiesNumber) self ! sendForcesResults
      } else unhandled(CalculateForce)
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
      bodiesDetails.foreach(body => { body._1 ! Force(body._4) })
      bodiesDetails.clear()
    }
  }
  private def compute(coordinate: Point2D.Double, mass: Double): Point2D.Double = {
    var newBodyForce = new Point2D.Double(0, 0)
    bodiesDetails.foreach(body => {
      val force = PhysicalEngine.getForce(mass, coordinate, body._3, body._2)
      newBodyForce.x += force.getX()
      newBodyForce.y += force.getY()
      body._4.x -= force.getX()
      body._4.y -= force.getY()
    })
    newBodyForce
  }
}