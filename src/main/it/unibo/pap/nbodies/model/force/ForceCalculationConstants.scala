package main.it.unibo.pap.nbodies.model.force

object ForceCalculationConstants {
  val bodiesWindow = 15
  val bodiesWindowCondition = (bodies: Int) => bodies > bodiesWindow
}