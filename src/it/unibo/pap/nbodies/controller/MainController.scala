package it.unibo.pap.nbodies.controller

import akka.actor.Actor
import akka.actor.ActorDSL
import akka.actor.Props
import java.awt.geom.Point2D
import java.awt.Point
import akka.actor.ActorRef

class MainController(bodiesNumber: Int, deltaTime: Int) extends Actor with IMainController {
  val initialBodiesNumber = bodiesNumber
  val initialDeltaTime = deltaTime

  //TODO: Creation of body Actors
  var bodies = List[ActorRef]()

  def removeBody(body: ActorRef) = {
    val (left, right) = bodies.span(_ != body)
    bodies = left ::: right.drop(1)
  }
  /**
   * TODO: Implement
   */
  override def receive = {
    case "Start Button Pressed" => println("Start Button Pressed")
    case "Stop Button Pressed" => println("Stop Button Pressed")
    case "Reset Button Pressed" => println("Reset Button Pressed")
    case "Delta Time Changed" => println("Delta Time Changed")
  }
}
