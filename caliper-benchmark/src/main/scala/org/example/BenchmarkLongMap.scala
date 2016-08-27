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
  @Param(Array("2500", "40000", "160000"))  //, "200000", "1000000", "5000000"))
  val length: Int = 0

  override def setUp() {
    // set up all your benchmark data here
  }

  def timeMuta(reps: Int) = repeat(reps) {
    val acc = mutable.LongMap[Int]()
    genDataLong(length) foreach { s =>
      acc(s) = acc.getOrElse(s, 0) + 1
    }
    acc.values.take(3).min
  }

  def timeVar(reps: Int) = repeat(reps) {
    var acc = immutable.LongMap[Int]()
    genDataLong(length) foreach { s =>
      acc = acc + (s -> (acc.getOrElse(s, 0) + 1))
    }
    acc.values.take(3).min
  }

  def timeVarInt(reps: Int) = repeat(reps) {
    var acc = immutable.IntMap[Int]()
    genDataInt(length) foreach { s =>
      acc = acc + (s -> (acc.getOrElse(s, 0) + 1))
    }
    acc.values.take(3).min
  }

  override def tearDown() {
    // clean up after yourself if required
  }

  def genDataInt(n: Int) = Iterator.fill(n)(rand_int)

  def genDataLong(n: Int) = Iterator.fill(n)(rand_long)

  def rand_int = Random.nextInt(10000)

  def rand_long = Random.nextInt(10000).toLong
}

