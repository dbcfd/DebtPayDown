package com.webwino.actors

import akka.actor._

import com.webwino.models.DebtPlan
import akka.routing.RoundRobinRouter

case class SolveDebt(val plan:DebtPlan)

class DebtSolver extends Actor {
  private val nbWorkers:Int = 10
  private val workerRouter = context.actorOf(Props[DebtSolver].withRouter(RoundRobinRouter(nbWorkers)), name = "debtSolver")
  implicit val system:ActorSystem = DebtSolver.system

  def receive = {
    case SolveDebt(plan) => {
      while(!plan.isPlanComplete) {
        plan.applyStrategy
      }
    }
  }
}

object DebtSolver {
  private val system = ActorSystem("DebtSolverSystem")

  def sendMessage(msg:SolveDebt) {
    val handler = system.actorOf(Props[DebtSolver], name = "solver")
    handler ! msg
  }  
}