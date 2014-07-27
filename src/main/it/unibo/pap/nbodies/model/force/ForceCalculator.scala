package it.unibo.pap.nbodies.model.force

import akka.actor.Actor
import java.awt.geom.Point2D
import akka.actor.Props
import it.unibo.pap.nbodies.model.messages.Messages.CalculateForce
import it.unibo.pap.nbodies.utility.PhysicalEngine
import akka.actor._
import it.unibo.pap.nbodies.model.messages.Messages.Force
import scala.collection.mutable.ListBuffer

class ForceCalculator(bodiesNumber: Int) extends Actor {
  // ActorRef to reply, Coordinate, mass, force to calculate
  val bodiesDetails = new ListBuffer[(ActorRef, Point2D.Double, Double, Point2D.Double)]()

  override def receive = {
    case CalculateForce(coordinate, mass) => {
      println("force calculation start")
      val force = compute(coordinate, mass)
      bodiesDetails.append((sender(), coordinate, mass, force))
      println("bodyadded, bodiesDetails.length: " ++ bodiesDetails.length.toString())
      if (bodiesDetails.length == bodiesNumber) {
        bodiesDetails.foreach(body => { body._1 ! Force(body._4) })
        bodiesDetails.clear()
      }
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