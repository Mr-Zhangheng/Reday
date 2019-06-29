package scaladay02

object WorldCount {

  def main(args: Array[String]): Unit = {
    val lines = List("hello dog","hello xiaobai","Hello xiaoran")
    val words = lines.flatMap(_.split(" "))
    val tuple:List[(String,Int)] = words.map((_,1))
    val grouped:Map[String,List[(String,Int)]] = tuple.groupBy(_._1)
    //grouped.foreach(println)
    val sum:Map[String,Int] = grouped.mapValues(_.size)
    //sum.foreach(println)
    val res = sum.toList.sortWith(_._2>_._2)
    res.foreach(println)



  }
}






