package it.unibo.pap.nbodies.controller

import it.unibo.pap.nbodies.model.body.Body
import akka.actor.ActorRef

trait IMainController {

  def removeBody(body: ActorRef)
}