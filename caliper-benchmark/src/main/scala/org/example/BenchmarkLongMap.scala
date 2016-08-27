package org.example

import com.google.caliper.Param

import scala.collection.mutable
import scala.collection.immutable
import scala.util.Random

// a caliper benchmark is a class that extends com.google.caliper.Benchmark
// the SimpleScalaBenchmark trait does it and also adds some convenience functionality
class BenchmarkLongMap extends SimpleScalaBenchmark {

  // to make your benchmark depend on one or more parameterized values, create fields with the name you want
  // the parameter to be known by, and add this annotation (see @Param javadocs for more details)
  // caliper will inject the respective value at runtime and make sure to run all combinations 
  @Param(Array("10000", "40000", "160000")) //, "200000", "1000000", "5000000"))
  val length: Int = 0

  override def setUp() {
    // set up all your benchmark data here
  }

  def timeMutaInt(reps: Int) = repeat(reps) {
    val acc = mutable.Map[Long, Int]()
    genDataLong(length) foreach { s =>
      acc(s) = acc.getOrElse(s, 0) + 1
    }
    acc.values.take(3).min
  }

  def timeMutaLong(reps: Int) = repeat(reps) {
    val acc = mutable.Map[Int, Int]()
    genDataInt(length) foreach { s =>
      acc(s) = acc.getOrElse(s, 0) + 1
    }
    acc.values.take(3).min
  }

  def timeVarHashI(reps: Int) = repeat(reps) {
    var acc = immutable.HashMap[Int, Int]()
    genDataInt(length) foreach { s =>
      acc = acc + (s -> (acc.getOrElse(s, 0) + 1))
    }
    acc.values.take(3).min
  }

  def timeVarHashL(reps: Int) = repeat(reps) {
    var acc = immutable.HashMap[Long, Int]()
    genDataLong(length) foreach { s =>
      acc = acc + (s -> (acc.getOrElse(s, 0) + 1))
    }
    acc.values.take(3).min
  }

  override def tearDown() {
    // clean up after yourself if required
  }

  def genDataInt(n: Int) = Iterator.fill(n)(rand_int)

  def genDataLong(n: Int) = Iterator.fill(n)(rand_long)

  def rand_int = Random.nextInt(20000)

  def rand_long = Random.nextInt(20000).toLong
}

