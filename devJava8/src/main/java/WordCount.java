import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class WordCount {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName("wordCount")
                .setMaster("local")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");

        conf.registerKryoClasses(new Class[]{
                String.class
        });

        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> input = sc.textFile("/Users/chenguo/Documents/spark/downloads/spark/README.md");

        JavaRDD<String> words = input.flatMap(line -> Arrays.asList(line.split(" ")));

        JavaPairRDD<String, Integer> counts = words.mapToPair(w -> new Tuple2<>(w, 1))
                .reduceByKey((x, y) -> x + y);

        counts.saveAsTextFile("output");
    }
}
