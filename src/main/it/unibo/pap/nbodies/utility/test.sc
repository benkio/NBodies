package it.unibo.pap.nbodies.utility

import java.awt.geom.Point2D

object test {
  val a = new TestBody(new Point2D.Double(1, 3), 10, 3)
                                                  //> a  : it.unibo.pap.nbodies.utility.TestBody = p[1.0, 3.0], [m, r][10.0, 3.0]
  val b = new TestBody(new Point2D.Double(3, 3), 10, 3)
                                                  //> b  : it.unibo.pap.nbodies.utility.TestBody = p[3.0, 3.0], [m, r][10.0, 3.0]
  val c = new TestBody(new Point2D.Double(6,13), 10, 3)
                                                  //> c  : it.unibo.pap.nbodies.utility.TestBody = p[6.0, 13.0], [m, r][10.0, 3.0]
                                                  //| 
	PhysicalEngine.getDistance(a.position , b.position)
                                                  //> res0: Double = 2.0
	PhysicalEngine.getDistance(a.position , c.position)
                                                  //> res1: Double = 11.180339887498949
  
  PhysicalEngine.getDistance(b.position , c.position)
                                                  //> res2: Double = 10.44030650891055
                                                  
  PhysicalEngine.getForce(a.mass, a.position , b.mass, b.position )
                                                  //> res3: java.awt.geom.Point2D.Double = Point2D.Double[0.2043239327261077, 0.97
                                                  //| 89033305262257]
   PhysicalEngine.getForce(a.mass, a.position , c.mass, c.position )
                                                  //> res4: java.awt.geom.Point2D.Double = Point2D.Double[0.9999821100808395, 0.00
                                                  //| 5981598304124604]
   PhysicalEngine.getForce(b.mass, b.position , c.mass, c.position )
                                                  //> res5: java.awt.geom.Point2D.Double = Point2D.Double[0.4121184852417566, -0.9
                                                  //| 111302618846769]
}

class TestBody(p: Point2D.Double, m: Double, r: Double) {
	val position = p;
	val mass = m
	
	override def toString = "p[" + p.getX() + ", " + p.getY() + "], [m, r][" + m + ", " + r + "]"
}