package com.webwino.actors

import akka.actor._

import com.webwino.evolution.{Evolvable, Population}
import akka.routing.RoundRobinRouter

case class HandleGeneration(val population:Population)

class GenerationHandler extends Actor {
  private val nbWorkers:Int = 10
  private val workerRouter = context.actorOf(Props[GenerationHandler].withRouter(RoundRobinRouter(nbWorkers)), name = "generationHandler")

  def receive = {
    case HandleGeneration(population) => {
      Population.apply(population)
    }
  }
}

object GenerationHandler {
  private val system = ActorSystem("GenerationSystem")

  def sendMessage(msg:HandleGeneration) {
    val handler = system.actorOf(Props[GenerationHandler], name = "generation")
    handler ! msg
  }
}