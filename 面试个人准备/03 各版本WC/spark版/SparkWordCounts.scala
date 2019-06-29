package sparkday01
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object SparkWordCounts {
  def main(args: Array[String]): Unit = {
    //创建一个配置信息对象
    //设置的应用程序名称
    val conf:SparkConf = new SparkConf()
      .setAppName("scala_wordcount")
      .setMaster("local[*]")
    //local 使用本地一个线程模拟机群执行任务
    //local[num]:使用num个线程去模拟集群执行任务
    //local[*] 使用所有空闲的线程去模拟集群执行任务
    //spark上下文对象,执行spark程序的入口
    val sc:SparkContext = new SparkContext(conf)
    //读取数据源
    //通过参数设置源文件路径,HDFS文件/本地文件
    //RDD集合里的每一个元素即为文件里的一行
    //textFile对于一个或者多个文件
    val lines: RDD[String] =  sc.textFile(args(0))
    //单词的rdd集合
    val words: RDD[String]=  lines.flatMap(_.split(" "))
    val tupleWords: RDD[(String,Int)] =  words.map((_,1))
    //先根据Word进行分组,然后对每组的value集合元素进行reduce聚合
    val reduced:RDD[(String,Int)] = tupleWords.reduceByKey(_ + _)
    //根据每一个单词出现的次数进行排序
    val res: RDD[(String,Int)] = reduced.sortBy(_._2,false) //逆序 ,默认是升序
    //结果打印,将结果从executor汇聚到driver端
    println(res.collect().toBuffer)
    //结果保存到文件系统
    res.saveAsTextFile(args(1))
    //资源释放
    sc.stop()
  }
}
