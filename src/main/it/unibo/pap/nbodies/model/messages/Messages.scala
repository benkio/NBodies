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
  case object RemoveBody extends Request

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
  case object DecreaseBodiesNumber extends Request
  case class SetBodiesNumber(num: Int) extends Request

  /**
   * Messages to the ForceCalculator
   */
  case class CalculateForce(coordinate: Point2D.Double, mass: Double) extends Request

  /**
   * Messages to the CollisionCalculator
   */
  case class CalculateCollision(bodyDetails: BodyDetails) extends Request

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~Response~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  trait Response
  /**
   * Response of Force Calculator
   */
  case class Force(force: Point2D.Double) extends Response

  /**
   * Response of Collision Calculator
   */
  case object IsCollided extends Response
  case class NotCollided(bodyDetails: BodyDetails) extends Response

  //~~~~~~~~~~~~~~~~~~~~~~~Internal Messages~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Internal Force Calculator Message
   */
  case object SendForcesResults

  /**
   * Internal Force Calculator Message
   */
  case object SendCollisionResults

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