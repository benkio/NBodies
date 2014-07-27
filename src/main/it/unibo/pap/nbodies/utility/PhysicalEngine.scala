package main.it.unibo.pap.nbodies.utility

import main.it.unibo.pap.nbodies.model.body.Body
import java.awt.geom.Point2D

object PhysicalEngine {
  val g = 9.81

  /**
   * Calculates the force between two bodies
   */
  def getForce(a: Body, b: Body): Double = {
    g * a.mass * b.mass / math.pow(getDistance(a.coordinate, b.coordinate), 2)
  }

  /**
   * Calculate the distance between two body
   */
  def getDistance(a: Point2D.Double, b: Point2D.Double): Double = {
    math.sqrt(
      math.pow(b.getX() - a.getX(), 2) +
        math.pow(b.getY() - a.getY(), 2))
  }
}