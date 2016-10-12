ml algorithm

1, logistic regression
Logistic regression  - for classification, 二分预测正反例
   http://52opencourse.com/125/coursera%E5%85%AC%E5%BC%80%E8%AF%BE%E7%AC%94%E8%AE%B0-%E6%96%AF%E5%9D%A6%E7%A6%8F%E5%A4%A7%E5%AD%A6%E6%9C%BA%E5%99%A8%E5%AD%A6%E4%B9%A0%E7%AC%AC%E5%85%AD%E8%AF%BE-%E9%80%BB%E8%BE%91%E5%9B%9E%E5%BD%92-logistic-regression
    multi-dimensional feature 判断相关性  yes or no (spam or non-spam)
    可以用MR对同样的dataset进行重复处理, so Spark benefits from caching the input in RAM across iterations.

    sigmoid function, S形函数: g(x)=1/(1+e的-x次方)    http://youess.github.io/blog/luo-ji-hui-gui.html
    g(x)>= 0.5时, 结果分类为1  (decision boundary)
    g(x)<0.5, 结果为分类0
    
    logistic regression核心就是对s形函数的参数θ进行随机的回归拟合regression fitting.

   有监督的学习,有training set训练集
  
val points = spark.textFile(...).map(parsePoint).cache()
var w = Vector.random(D) // current separating plane, 此向量不是List那么实现
for (i <- 1 to ITERATIONS) {
  val gradient = points.map(p =>
    (1 / (1 + exp(-p.y*(w dot p.x))) - 1) * p.y * p.x   //exp在scala里表示e的power幂 dot表示2个向量的点积A*B  https://zh.wikipedia.org/wiki/%E6%95%B0%E9%87%8F%E7%A7%AF
  ).reduce(_ + _)
  w -= gradient
}
println("Final separating plane: " + w)



Loss function J: θ把1个事物映射到1个实数上的map函数.  一般用来计算预测与实际的偏离差值(loss)
一般用来数据建模，以此来提高生产效率。
Loss function就是来判断θ与实际结果的效果.

gradient梯度:  一个立体空间, 垂直距离/水平面距离 vertical distance/horizontal distance
Gradient descent梯度下降:  每次取1个值,按照步长来迭代计算,如果某个结果值算出来与上1个结果值相差很小(收敛),那么可以认为找到了这个值.  http://www.zhizhihu.com/html/y2011/3632.html

Train: 通过训练集(input)和模型(算法)来优化最佳的parameter.   http://www.zhihu.com/question/29271217
假定房屋定价的模型为y=x  => h(x),那么如何让5个features分量(e.x. x1面积,x2朝向,x3楼层,x4位置,x5学区)能够匹配到这个直线上了?   x=θ0 + θ1*x1 + θ2*x2 + θ3*x3 + θ4*x4 + θ5*x5
我们train的工作就是调整θ参数

Regression回归: 分析自变量与应变量间的关系(方程)，一般通过数理统计的方法进行分析. 

 fitting拟合:  把参数,模型与data进行模拟匹配, 如果这个过程做得不够, 太多的data不符合你的模型(直线-线形回归，曲线-二次回归/二项式回归或其他方程)则是under-fitting, 如果为了使所有的data都在curve上则可能是牵强附会的over-fitting, 此时可能计算复杂,效率低下.  合适就好.


噪声:


https://jaceklaskowski.gitbooks.io/mastering-apache-spark/content/spark-sql-whole-stage-codegen.html

multiple operators (as a subtree of plans that support codegen) together into a single Java function

It collapses a query into a single optimized function that eliminates virtual function calls and leverages CPU registers for intermediate data.

Compare to hand-written code


Why:
	1. Performance
	2. 内存与CPU场景
	
发展历史:
	• No
	• Inspired by ...
	• Limited CG
	• Whole stage CG

How:
	• Transparent to developers
	• SQL/DF/DS (catalyst)
	• https://databricks.com/blog/2015/04/13/deep-dive-into-spark-sqls-catalyst-optimizer.html

$ ds.join(broadcast(ds)).explain(extended=true)

import org.apache.spark.sql.execution.debug._
scala> spark.range(10).sample(false, 0.4).debug

scala> spark.range(10).sample(false, 0.4).debugCodegen

spark.range(10).where('id === 4).debug



scala> spark.range(10).sample(false, 0.4).explain
== Physical Plan == (只有physical plan才有wscg)
*Sample 0.0, 0.4, false, 1649641486367235675     => SampleExec
+- *Range (0, 10, splits=8)   => RangeExec, basicPhysicalOperators.scala

HashAggregateExec

*表示whole stage codegen
Range是个啥? Splits is numOfSlices
org.apache.spark.sql.catalyst.plans.logical.Range


spark.conf.set("spark.sql.codegen.wholeStage", "true");
spark.conf.set("supportCodegen", "true");

scala> spark.range(1000L * 1000 * 1000).selectExpr("sum(id)").explain(extended=true);
Scala> spark.range(1000).filter("id > 100").selectExpr("sum(id)").explain()


>sql("explain codegen select 'a' as a group by 1").head
 
https://github.com/apache/spark/blob/master/sql/core/src/main/scala/org/apache/spark/sql/execution/

Unary 一元操作，比如    ! Result
Binary 二元操作  3+2


Jira
https://issues.apache.org/jira/browse/SPARK-12795
See http://www.vldb.org/pvldb/vol4/p539-neumann.pdf

Execution flow:

SqlParser

RuleExecutor
	• Analyzer: 有很多Rule. batches
	• Optimizer: 里面有很多的Rule, pushdown… batches
	•  apply()

QueryPlanner 逻辑执行计划算子映射成物理执行计划算子
	• Strategies
	• SparkStrategies extends QueryPlanner 大量Operators


def range(
      start: Long,  变量名: 类型,
      end: Long,
      step: Long = 1,
      numSlices: Int = defaultParallelism): RDD[Long] = withScope {
=缺省值
:返回值[范型类型]


scala.collection.Seq

几篇官方文章:
https://databricks.com/blog/2015/04/13/deep-dive-into-spark-sqls-catalyst-optimizer.html
https://databricks.com/blog/2016/05/23/apache-spark-as-a-compiler-joining-a-billion-rows-per-second-on-a-laptop.html
https://databricks.com/blog/2014/06/02/exciting-performance-improvements-on-the-horizon-for-spark-sql.html
http://blog.csdn.net/wl044090432/article/details/52190736

QueryPlan
LogicalPlan extends QueryPlan[LogicalPlan]: Command, Unary, Binarry Operators
SparkPlan extends QueryPlan<SparkPlan> (physical)
SparkPlan extends QueryPlan[SparkPlan]   (sql.execution, SortExec…) 产生codegen pipelines

*Exec operators需要继承CodegenSupport
InternalRow
BufferedRowIterator  -- processNext()


Benchmark: (相差5倍)
https://databricks-prod-cloudfront.cloud.databricks.com/public/4027ec902e239c93eaaa8714f173bcfc/6122906529858466/293651311471490/5382278320999420/latest.html
spark.conf.set("spark.sql.codegen.wholeStage", false)
spark.range(1000L * 1000 * 1000).selectExpr("sum(id)").show()

spark.conf.set("spark.sql.codegen.wholeStage", true)
spark.range(1000L * 1000 * 1000).selectExpr("sum(id)").show()

不适合的场景:
	• String
	• 大量IO (不是CPU+memory)


几个code gen的例子:
	• Select a+b 虚函数, cpu pre-fetch streamline https://databricks.com/blog/2014/06/02/exciting-performance-improvements-on-the-horizon-for-spark-sql.html
	• Select count(*) from t where b=1000  Volcano style的开销, operator, iterator mode, cpu register, virtual functions, loop unrolling, SIMD
	• Order by http://blog.csdn.net/wl044090432/article/details/52190736  (any)

LLVM
In particular, we plan to investigate compilation to LLVM or OpenCL, so Spark applications can leverage SSE/SIMD instructions out of modern CPUs and the wide parallelism in GPUs to speed up operations in machine learning and graph computation.
目前还是没有用LLVM.

CG的代码能看懂吗?  spark.range(1000).filter("id > 100").selectExpr("sum(id)").debugCodegen
可以看懂.

CG的代码是怎么生成的?
sql/core/src/main/scala/org/apache/spark/sql/execution/WholeStageCodegen.scala   => doCodeGen(), doExecute(), CodeGenrator.compile()
所有的*Exec需要继承CodegenSupport (produce/consume)
有很多这样的Operator, 参考https://jaceklaskowski.gitbooks.io/mastering-apache-spark/content/spark-sql-whole-stage-codegen.html    basicPhysicalOperators.scala

Produce/consume模式
逐个调用hasNext(), processNext();

Scala programming:
	• Literal: 字面量，就是当前这个变量在代码中的字符串，但没有类型

	• scala中的 apply() function, 把一个object当作function来用
object  OO1{
 def apply(x:Int)=x+1
}
OO1是个Class,但是有个apply后，我们可以直接把OO1当成1个function来调用，OO1(100)相当于 new OO1().apply(100);

	• trait and multi-inherence diamond problem
	trait符合最右（最后）的深度优先遍历结果
	
	• Java 8 default methods in interface definition

	//
	//  http://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html
	//
	
	public static void main(String[] args){
		Stream.of("1","20","12").filter(a->Integer.valueOf(a)>10).forEach(a->System.out.println(a));
		List<String> l=new ArrayList<>();
		Object[] array = l.stream().toArray();
		List<Long> sourceLongList = Arrays.asList(1L, 10L, 50L, 80L, 100L, 120L, 133L, 333L);
		List<String> list = Stream.of("1","20","12").collect(Collectors.toList());
		//Collectors.toSet();
		System.out.println(list);
		
		Stream.of("1","20","12").map(Java8::method1).forEach(a->System.out.println(a));
		
		new Random().ints(100,200).forEach(System.out::println);
		
		Comparator<List> c = (a, b) -> Integer.compare(a.size(), b.size());
		
		//join
		//min, max, sort...
	}
	
	private static String method1(String item){
		return item+"aa";
	}
