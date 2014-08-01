package it.unibo.pap.nbodies.model

import java.awt.geom.Point2D
import scala.concurrent.duration._

object ModelConstants {
  val massMultiplier = 100
  val radiusDivider = 10
  val initialVelocity = new Point2D.Double(0, 0)
  val initialForce = new Point2D.Double(0, 0)
  val startNextStepDelay = 1 second

}