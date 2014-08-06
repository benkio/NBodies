package it.unibo.pap.nbodies.model

import java.awt.geom.Point2D
import java.lang.Double

class BodyDetails(c: Point2D.Double, m: Double) {
  var coordinate = new Point2D.Double(c.getX(), c.getY())
  var mass = new Double(m)
  var radius = () => Math.pow(mass * ModelConstants.bodyDensity, (1.0 / 3.0))
  var isCollided = false
}