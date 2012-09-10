package com.webwino.evolution

import util.Random

object Population {
  def populationSize:Int = 100
  def mutationProbability:Double = 0.01

  private val rng = new Random

  def apply(population:Population) : Population = {
    val individualsAndTotalFitness = population.determineFitnesses
    val totalFitness = individualsAndTotalFitness._2
    val sortedIndividuals:IndexedSeq[(Evolvable,Double)] = (individualsAndTotalFitness._1 sortWith ( ( indiv1:(Evolvable,Double), indiv2:(Evolvable,Double) ) => {
      indiv1._2 > indiv2._2
    })).toIndexedSeq
    val topIndividual = sortedIndividuals.head._1
    val topIndividualCount = populationSize / 10
    val topIndividuals:IndexedSeq[Evolvable] = for (i <- Range(0, topIndividualCount)) yield(topIndividual.generateOffspring(rng.nextDouble() < mutationProbability))
    var sumProb:Double = 0
    val probabilities = individualsAndTotalFitness._1 map ( ind => {
      sumProb += ind._2 / totalFitness
      sumProb
    })
    val randomIndividuals:IndexedSeq[Evolvable] = for (i <- Range(topIndividualCount, populationSize)) yield {
      val indivIndex = probabilities.indexWhere( prob => prob < rng.nextDouble() )
      sortedIndividuals(indivIndex)._1.generateOffspring(rng.nextDouble() < mutationProbability)
    }
    val popIndividuals = (topIndividuals ++ randomIndividuals).toList
    new Population(popIndividuals)
  }
}
class Population(val individuals:List[Evolvable]) {
  def determineFitnesses: (List[(Evolvable,Double)], Double) = {
    var sumFitness:Double = 0
    ( individuals map ( (individual:Evolvable) => {
      val fitness = individual.determineFitness
      sumFitness += fitness
      (individual, fitness)
    }), sumFitness)
  }
}