import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

object EmpDept {

  def main(args: Array[String]) {

    val conf: SparkConf = new SparkConf().setAppName("LearnDataFrame").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val emp = sc.textFile("/Users/chenguo/Documents/learnSpark/data/emp.csv")
    val dept = sc.textFile("/Users/chenguo/Documents/learnSpark/data/dept.csv")


    def _2int = (s: String) => {
      if (s != null && !s.isEmpty) s.toInt else 0
    }
    def _2double = (s: String) => {
      if (s != null && !s.isEmpty) s.toDouble else 0
    }
    def _2string = (s: String) => {
      if (s != null && !s.isEmpty) s.toString else ""
    }
    val conversionMap = Map(IntegerType -> _2int, DoubleType -> _2double, StringType -> _2string)



    val empSchemaDesc = Array((0, "empno", IntegerType), (1, "ename", StringType), (2, "job", StringType), (3, "mgr", IntegerType), (4, "hiredate", StringType), (5, "sal", IntegerType), (6, "comm", IntegerType), (7, "deptno", IntegerType))
    val empTypeMapByIndex = empSchemaDesc.map(c => c._1 -> c._3).toMap
    val empTypeMapByName = empSchemaDesc.map(c => c._2 -> c._3).toMap


    val empheader = emp.first()
    val empSchema = StructType(empheader.split(",").map(fieldName => StructField(fieldName, empTypeMapByName(fieldName), true)))
    val empRDD = emp.filter(_ != empheader).map(r =>
      Row(r.split(",").zipWithIndex.map(c => conversionMap(empTypeMapByIndex(c._2))(c._1)): _*))
    val empDataFrame = sqlContext.createDataFrame(empRDD, empSchema)
    //Problem 2 - Method 1
    val maxSalByDeptDataFrame = empDataFrame.groupBy("deptno").agg(max("sal").as("sal"))
    maxSalByDeptDataFrame.show
    //Problem 2 - Method 2
    empDataFrame.registerTempTable("emp")
    sqlContext.sql("select deptno, max(sal) sal from emp group by deptno").show

    //sqlContext.cacheTable("emp")

    val deptSchemaDesc = Array((0, "deptno", IntegerType), (1, "dname", StringType), (2, "loc", StringType))
    val deptTypeMapByIndex = deptSchemaDesc.map(c => c._1 -> c._3).toMap
    val deptTypeMapByName = deptSchemaDesc.map(c => c._2 -> c._3).toMap


    val deptheader = dept.first()
    val deptSchema = StructType(deptheader.split(",").map(fieldName => StructField(fieldName, deptTypeMapByName(fieldName), true)))
    val deptRDD = dept.filter(_ != deptheader).map(r =>
      Row(r.split(",").zipWithIndex.map(c => conversionMap(deptTypeMapByIndex(c._2))(c._1)): _*))
    val deptDataFrame = sqlContext.createDataFrame(deptRDD, deptSchema)

    //Problem 1 - Method 1
    empDataFrame.join(deptDataFrame, "deptno").select(empDataFrame("empno"), empDataFrame("ename"), empDataFrame("deptno"), deptDataFrame("dname")).show
    //val joined = empDataFrame.join(deptDataFrame, empDataFrame("deptno") === deptDataFrame("deptno"))
    //Problem 1 - Method 2
    deptDataFrame.registerTempTable("dept")
    sqlContext.sql("SELECT e.empno, e.ename, d.deptno, d.dname FROM emp e LEFT OUTER JOIN dept d ON e.deptno = d.deptno").show


    //Problem 3 - Method 1
    empDataFrame.join(deptDataFrame, "deptno").join(maxSalByDeptDataFrame, Seq("deptno", "sal")).select(deptDataFrame("dname"), empDataFrame("empno"), empDataFrame("ename"), empDataFrame("sal")).sort(empDataFrame("sal").desc).show
    //Problem 3 - Method 2
    sqlContext.sql("select dept.dname, emp.empno, emp.ename, emp.sal from emp inner join dept on emp.deptno = dept.deptno inner join ( select emp.deptno, max(emp.sal) sal from emp group by emp.deptno) ss on emp.deptno = ss.deptno and emp.sal = ss.sal order by emp.sal desc").show

  }
}
