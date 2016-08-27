package org.example

import com.google.caliper

object RunStringMapBenchmark extends App {
    caliper.Runner.main(classOf[Benchmark], args: _*)
}