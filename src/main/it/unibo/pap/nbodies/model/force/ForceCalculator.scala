package it.unibo.pap.nbodies.model.force

import akka.actor.Actor
import java.awt.geom.Point2D
import main.it.unibo.pap.nbodies.model.force.ForceCalculationConstants
import akka.actor.Props
import it.unibo.pap.nbodies.model.messages.Messages.CalculateForce
import it.unibo.pap.nbodies.utility.PhysicalEngine
import scala.actors.ActorRef
import akka.actor.Terminated

class ForceCalculator(bodiesNumber: Int) extends Actor {
  // ActorRef to reply, Coordinate, mass, force to calculate
  var bodiesDetails = List[(ActorRef, Point2D, Double, Point2D)]()

  override def receive = {
    case CalculateForce(coordinate, mass) => {
      bodiesDetails.::((sender(), coordinate, mass, compute()))
      if (bodiesDetails.length == bodiesNumber) {
        bodiesDetails.foreach(body => { body._1 ! body._4 })
        context stop self
      }
    }
  }
  private def compute() {
    var newBodyForce = new Point2D.Double(0, 0)
    bodiesDetails.foreach(body => {
      /*	
         * val force = PhysicalEngine(...)
         * newBodyForce.getX() += force.getX()
         * newBodyForce.getY() += force.getY()
         * body._4.getX() -= force.getX()
         * body._4.getY() -= force.getY()
         * 
         */
    })
    newBodyForce
  }
}