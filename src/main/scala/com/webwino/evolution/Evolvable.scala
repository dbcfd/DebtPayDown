package com.webwino.evolution

/**
 * Trait which allows classes to Evolve in a population
 */
trait Evolvable {
  def generateOffspring(shouldMutate:Boolean): Evolvable

  def determineFitness: Double
}