package eval

import java.io.File
import java.util

import com.nomis.modelevaluation.commons.CsvFileReaderWriter
import com.nomis.modelevaluation.evaluation.NomisPMMLEvaluator

import scala.collection.JavaConversions._

object PMMLEval {
  def main(args: Array[String]) {
    val basePath: String = System.getProperty("user.dir")
    val pmmlFile: File = new File(basePath, "src/main/resources/basic_iris_model.pmml")
    val csvFile: File = new File(basePath, "src/main/resources/iris.csv")
    val eval = new NomisPMMLEvaluator(pmmlFile)

    val result: List[util.List[String]] =
      eval.evaluate(new CsvFileReaderWriter(), csvFile, NomisPMMLEvaluator.ResultType.All).toList
    result.foreach(row => println(row))

  }
}
