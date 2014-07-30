package it.unibo.pap.nbodies.model.messages

import akka.actor.ActorRef
import java.awt.geom.Point2D
import scala.collection.mutable.ListBuffer

object Messages {

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Requests~~~~~~~~~~~~~~~~~
  trait Request

  /**
   * Message for ask to painter to repaint the frame with passed coordinates and radius
   */
  case class PaintObj(bodiesList: ListBuffer[(Point2D, Double)]) extends Request

  /**
   * Basic body request for details
   */
  case object GetXCoordinate extends Request
  case object GetYCoordinate extends Request
  case object GetRadius extends Request

  /**
   * Main messages of the application
   */
  case class StartSimultation(deltaTime: Int) extends Request
  case class OneStep(deltaTime: Int) extends Request
  case object Stop extends Request
  case object Reset extends Request

  /**
   * Messages to the ForceCalculator
   */
  case class CalculateForce(coordinate: Point2D.Double, mass: Double) extends Request
  case object CanCalculate extends Request

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~Response~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  trait Response
  /**
   * Response of Force Calculator
   */
  case class Force(force: Point2D.Double) extends Response

  /**
   * Response of bodies
   */
  case class PositionUpdated(bodyDetail: (Point2D.Double, Double))
  //~~~~~~~~~~~~~~~~~~~~~~~Internal Messages~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Internal Force Calculator Message
   */
  case object sendForcesResults

  /**
   * Internal body Message
   */
  case object UpdateForce

}