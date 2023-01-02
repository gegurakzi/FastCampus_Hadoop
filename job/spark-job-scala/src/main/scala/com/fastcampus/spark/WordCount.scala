package com.fastcampus.spark

import scala.io.Source
import java.io.PrintWriter

object WordCount {
  def main(args: Array[String]): Unit = {
    println(">>> WordCountScala...")

    var input = args(0)
    var output = args(1)
    var delimiter = " "

    val arr = Source.fromFile(input).getLines().toArray
    println(">>> Line Count : " + arr.count(line => true))

    val arr2 = arr.flatMap(line => line.split(delimiter))

    val arr3 = arr2.map(word => (word, 1))

    val arr4 = arr3.groupBy(tuple => tuple._1).toArray

    val arr5 = arr4.map(tuple_grouped => (tuple_grouped._1, tuple_grouped._2.map(tuple => tuple._2)))

    val arr6 = arr5.map(tuple_grouped => (tuple_grouped._1, tuple_grouped._2.reduce((v1, v2) => v1 + v2)))

    val arr7 = arr6.sortBy(tuple => tuple._2)

    arr7.foreach(word_count => println(">>> word count (count desc) : " + word_count))

    val ordering = new Ordering[(String, Int)] {
      override def compare(x: (String, Int), y: (String, Int)): Int = {
        if((x._2 compare y._2) == 0) (x._1 compare y._1) else -(x._2 compare y._2)
      }
    }

    val arr8 = arr6.sortBy(tuple => tuple)(ordering)

    arr8.foreach(word_count => println(">>> word count (count desc, word asc) : " + word_count))

    new PrintWriter(output) {
      write(arr8.mkString("\n"))
      close()
    }
  }
}
