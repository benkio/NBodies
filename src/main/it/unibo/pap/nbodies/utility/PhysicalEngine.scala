package it.unibo.pap.nbodies.utility

import it.unibo.pap.nbodies.model.body.Body
import java.awt.geom.Point2D

object PhysicalEngine {
  val g = 9.81

  /**
   * Calculates the force between two bodies
   */
  def getForce(ma: Double, pa:Point2D.Double, mb: Double, pb:Point2D.Double): Double = {
    g * ma * mb / math.pow(getDistance(pa, pb), 2)
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