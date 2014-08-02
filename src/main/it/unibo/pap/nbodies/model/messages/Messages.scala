package it.unibo.pap.nbodies.model.messages

import akka.actor.ActorRef
import java.awt.geom.Point2D
import scala.collection.mutable.ListBuffer
import it.unibo.pap.nbodies.model.BodyDetails

object Messages {

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Requests~~~~~~~~~~~~~~~~~
  trait Request

  /**
   * Painter Requests
   *
   * Message for ask to painter to repaint the frame with passed coordinates and radius
   * it replace the previous values of the body with the new one
   */
  case class PaintBody(newBodyDetails: BodyDetails) extends Request
  case class SetBodiesDetails(list: List[(BodyDetails, ActorRef)]) extends Request

  /**
   * Basic body request for details
   */
  case object GetBodyDetails extends Request

  /**
   * Main messages of the application
   */
  case class StartSimultation(deltaTime: Int) extends Request
  case class OneStep(deltaTime: Int) extends Request
  case object Stop extends Request
  case object Reset extends Request
  case object StepFinished extends Request

  /**
   * Messages to the ForceCalculator
   */
  case class CalculateForce(coordinate: Point2D.Double, mass: Double) extends Request

  /**
   * Messages to the CollisionCalculator
   */
  case class CalculateCollision(coordinate: Point2D.Double, radius: Double) extends Request

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~Response~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  trait Response
  /**
   * Response of Force Calculator
   */
  case class Force(force: Point2D.Double) extends Response

  //~~~~~~~~~~~~~~~~~~~~~~~Internal Messages~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Internal Force Calculator Message
   */
  case object sendForcesResults

  /**
   * Internal Force Calculator Message
   */
  case object sendCollisionResults

  /**
   * Internal body Message
   */
  case object UpdateForce

  //~~~~~~~~~~~~~~~~~~~~~~~Message From Scheduler~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   * Called by the scheduler every x milliseconds to update the frame.
   */
  case object DrawFrame

}