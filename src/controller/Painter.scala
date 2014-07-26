package controller

import akka.actor.Actor
import view.NBodies.NBodiesCanvas
import akka.actor.ActorDSL
import model.body.Body
/**
 * Actor used to manage the paint of the bodies
 */
class Painter(viewCanvas: NBodiesCanvas) extends Actor {

  val canvas = viewCanvas
  
  /**
   * TODO: Implement
   */
  override def receive = {
    case _ => 
  }
  
  def paint(bodies: List[Body]) = {
    canvas.setBodiesList(bodies)
    canvas.repaint()
  }
}
