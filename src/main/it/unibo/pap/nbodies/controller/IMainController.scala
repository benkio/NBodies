package main.it.unibo.pap.nbodies.controller

import main.it.unibo.pap.nbodies.model.body.Body
import akka.actor.ActorRef

trait IMainController {

  def removeBody(body: ActorRef)
}