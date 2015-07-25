package org.example

import annotation.tailrec
import com.google.caliper.Param

import scala.collection.mutable
import scala.util.Random

// a caliper benchmark is a class that extends com.google.caliper.Benchmark
// the SimpleScalaBenchmark trait does it and also adds some convenience functionality
class Benchmark extends SimpleScalaBenchmark {

  // to make your benchmark depend on one or more parameterized values, create fields with the name you want
  // the parameter to be known by, and add this annotation (see @Param javadocs for more details)
  // caliper will inject the respective value at runtime and make sure to run all combinations 
  @Param(Array("8000", "40000"))//, "200000", "1000000", "5000000"))
  val length: Int = 0

  override def setUp() {
    // set up all your benchmark data here
  }

  def timeMuta(reps: Int) = repeat(reps) {
    val acc = mutable.Map[String, Int]()
    genData(length) foreach { s =>
      acc(s) = acc.getOrElse(s, 0) + 1
    }
    acc.values.take(3).min
  }

  def timeVar(reps: Int) = repeat(reps) {
    var acc = Map[String, Int]()
    genData(length) foreach { s =>
      acc = acc + (s -> (acc.getOrElse(s, 0) + 1))
    }
    acc.values.take(3).min
  }

  def timeFold(reps: Int) = repeat(reps) {
    val acc = genData(length).foldLeft(Map.empty[String, Int]) { (a, str) =>
      a + (str -> (1 + a.getOrElse(str, 0)))
    }
    acc.values.take(3).min
  }

  override def tearDown() {
    // clean up after yourself if required
  }

  def genData(n: Int) = Iterator.fill(n)(rand_string)

  def rand_string = (for (x <- 1 to 6) yield rand_char).mkString

  def rand_char = (Random.nextInt(5) + 'a'.asInstanceOf[Int]).asInstanceOf[Char]
}

