package com.fastcampus.spark

import org.apache.spark.SparkContext
object WordCount {
  def main(args: Array[String]): Unit = {
    println(">>> WordCountScala...")

    var input = args(0)
    var output = args(1)
    var delimiter = " "

    val sc = new SparkContext()

    val rdd = sc.textFile(input)
    println(">>> Line Count : " + rdd.count())

    val rdd2 = rdd.flatMap(line => line.split(delimiter))

    val rdd3 = rdd2.map(word => (word, 1))

    val rdd4 = rdd3.groupBy(tuple => tuple._1)

    val rdd5 = rdd4.map(tuple_grouped => (tuple_grouped._1, tuple_grouped._2.map(tuple => tuple._2)))

    val rdd6 = rdd5.map(tuple_grouped => (tuple_grouped._1, tuple_grouped._2.reduce((v1, v2) => v1 + v2)))

    rdd6.foreach(word_count => println(">>> word count : " + word_count))

    val rdd7 = rdd6.sortBy(tuple => tuple._2)

    rdd7.foreach(word_count => println(">>> word count (count desc) : " + word_count))

    val ordering = new Ordering[(String, Int)] {
      override def compare(x: (String, Int), y: (String, Int)): Int = {
        if((x._2 compare y._2) == 0) (x._1 compare y._1) else -(x._2 compare y._2)
      }
    }

    val rdd8 = rdd6.sortBy(tuple => tuple)(ordering, implicitly[scala.reflect.ClassTag[(String, Int)]])

    rdd8.foreach(word_count => println(">>> word count (count desc, word asc) : " + word_count))

    rdd8.saveAsTextFile(output)

    sc.stop()
  }
}
