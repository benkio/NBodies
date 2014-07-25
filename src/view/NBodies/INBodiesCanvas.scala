package view.NBodies

import model.body.Body

trait INBodiesCanvas {
	def addBody(body: Body)
	def removeBody(body: Body)
}