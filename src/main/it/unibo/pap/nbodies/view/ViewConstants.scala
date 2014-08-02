package it.unibo.pap.nbodies.view

import java.awt.Dimension
import java.awt.Color
import scala.concurrent.duration._

object ViewConstants {
  val FrameDimension = new Dimension(1400, 1000)
  val CanvasDimension = new Dimension(1200, 800)
  val BodyColor = Color.WHITE
  val CanvasColor = Color.BLACK
  val FrameUpdateTime = 50 millisecond
}