package controller

import model.body.Body
import akka.actor.Actor
import akka.actor.ActorDSL
import akka.actor.Props

class MainController(bodiesNumber:Int,deltaTime:Int) extends Actor with IMainController {
  val initialBodiesNumber = bodiesNumber
  val initialDeltaTime = deltaTime
    
  //TODO: Creation of body Actors
  var bodies = List[Body]()
  
  def removeBody(body: Body) = {
    val (left, right) = bodies.span(_ != body)
    bodies = left ::: right.drop(1)
  }
  
  /**
   * TODO: Implement
   */
  override def receive = {
    case _ =>
  }
}

object MainController{
  def Props(bodiesNumber:Int, deltaTime:Int):Props = Props(bodiesNumber,deltaTime)
}
