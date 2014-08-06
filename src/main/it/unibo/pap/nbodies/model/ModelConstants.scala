package it.unibo.pap.nbodies.model

import java.awt.geom.Point2D
import scala.concurrent.duration._

object ModelConstants {
  val massMultiplier = Math.pow(10, Math.random() * 23)
  val bodyDensity = 4.328548123
  val minimumMass = 10
  val initialVelocityX = 0
  val initialVelocityY = 0
  val initialForceX = 0
  val initialForceY = 0
  val startNextStepDelay = 50 millisecond
  val maxBodiesDistance = 149600
}