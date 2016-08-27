package org.example

import com.google.caliper

object RunLongMapBenchmark extends App {
    caliper.Runner.main(classOf[BenchmarkLongMap], args: _*)
}