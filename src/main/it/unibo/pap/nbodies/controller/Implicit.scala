package main.it.unibo.pap.nbodies.controller

import scala.concurrent.ExecutionContext
import akka.util.Timeout
import java.util.concurrent.TimeUnit

object Implicit {
  implicit val ec = ExecutionContext.Implicits.global
  implicit lazy val timeout = Timeout(10, TimeUnit.SECONDS)
}