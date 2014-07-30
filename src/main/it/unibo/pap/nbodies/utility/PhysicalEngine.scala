package it.unibo.pap.nbodies.utility

import it.unibo.pap.nbodies.model.body.Body
import java.awt.geom.Point2D

object PhysicalEngine {
  val g = 9.81

  /**
   * Calculates the force between two bodies
   */
  def getForce(ma: Double, pa: Point2D.Double, mb: Double, pb: Point2D.Double): Point2D.Double = {
    val distance = if (getDistance(pa, pb) != 0) getDistance(pa, pb) else 1
    val module = g * ma * mb / math.pow(distance, 2)
    val cosAngle = (pb.getX() - pa.getX()) / distance
    val senAngle = (pb.getY() - pa.getY()) / distance
    new Point2D.Double(module * cosAngle, module * senAngle)
  }

  /**
   * Calculate the distance between two body
   */
  def getDistance(a: Point2D.Double, b: Point2D.Double): Double = {
    math.sqrt(
      math.pow(b.getX() - a.getX(), 2) +
        math.pow(b.getY() - a.getY(), 2))
  }

  /**
   * Calculate the escape velocity, where m is the mass and r the distance from the center of gravity
   */
  def escapeVelocity(m: Double, r: Double) {
    math.sqrt(2 * g * m / r)
  }
}