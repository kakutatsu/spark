import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]) {

    val conf: SparkConf = new SparkConf().setAppName("wordCount").setMaster("local")
    val sc = new SparkContext(conf)
    val input = sc.textFile("/Users/chenguo/Documents/spark/downloads/spark/README.md")
    val words = input.flatMap(line => line.split(" "))
    val counts = words.map(word => (word, 1)).reduceByKey { case (x, y) => x + y }

    counts.saveAsTextFile("output")
  }
}
