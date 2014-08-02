package it.unibo.pap.nbodies.model

import java.awt.geom.Point2D
import scala.concurrent.duration._

object ModelConstants {
  val massMultiplier = 100
  val radiusDivider = 10
  val minimumMass = 10
  val initialVelocityX = 0
  val initialVelocityY = 0
  val initialForceX = 0
  val initialForceY = 0
  val startNextStepDelay = 50 millisecond
  val maxBodiesDistance = 1000
}