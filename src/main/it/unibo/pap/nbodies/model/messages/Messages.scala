package it.unibo.pap.nbodies.model.Messages

import akka.actor.ActorRef
import java.awt.geom.Point2D

object Messages {

  sealed trait Request

  case class PaintObj(bodiesList: List[(Point2D, Double)]) extends Request
  case object GetXCoordinate extends Request
  case object GetYCoordinate extends Request
  case object GetRadius extends Request

}