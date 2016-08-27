import scala.collection.mutable
import scala.util.Random

object ImmutableMapBenchmark extends App {

  def Test[A](f: => Int): (Int, Long) = {
    val start_time = System.currentTimeMillis()
    val result = f
    val end_time = System.currentTimeMillis()
    (result, end_time - start_time)
  }

  val data_size = 1000000

  def theFuncs: Map[String, MapTester] = Map(
    "mutable" -> new MutableTester,
    "immu-var" -> new ImmutableVarTester,
    "immu-fold" -> new ImmutableFoldTester
  )

  val results = for {
    dataSize <- List(10000, 100000, 1000000, 10000000).iterator
    iteration <- (0 to 10).iterator
    (name, func) <- theFuncs.iterator
  } yield {
      val (result, time) = Test(func.apply(dataSize))
      f"$dataSize%09d $name%9s $time%05d $iteration%02d $result%04d"
    }

  results foreach println
}

trait MapTester {
  def genData(n: Int) = Iterator.fill(n)(rand_string)

  def rand_string = (for (x <- 1 to 6) yield rand_char).mkString

  def rand_char = (Random.nextInt(5) + 'a'.asInstanceOf[Int]).asInstanceOf[Char]

  def apply(n: Int): Int
}

class MutableTester extends MapTester {
  def apply(n: Int): Int = {
    val acc = mutable.Map[String, Int]()
    genData(n) foreach { s =>
      acc(s) = acc.getOrElse(s, 0) + 1
    }
    acc.values.take(3).min
  }
}

class ImmutableVarTester extends MapTester {
  def apply(n: Int): Int = {
    var acc = Map[String, Int]()
    genData(n) foreach { s =>
      acc = acc + (s -> (acc.getOrElse(s, 0) + 1))
    }
    acc.values.take(3).min
  }
}

class ImmutableFoldTester extends MapTester {
  def apply(n: Int): Int = {
    val acc = genData(n).foldLeft(Map.empty[String, Int]) { (a, str) =>
      a + (str -> (1 + a.getOrElse(str, 0)))
    }
    acc.values.take(3).min
  }
}