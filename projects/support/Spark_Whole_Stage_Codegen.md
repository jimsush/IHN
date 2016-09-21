* 1. What is whole stage codegen?

Inspired by MPP(strong compiler and executor).

Put multiple operators (as a subtree of plans that support codegen) together into a single Java function.

It collapses a query into a single optimized function that eliminates virtual function calls and leverages CPU registers for intermediate data.

* 2. What is codegen?

Performance compare to hand-written code

CPU and memory are bottleneck for model data processng system(in memory computation)

Exmple:
a+b https://databricks.com/blog/2014/06/02/exciting-performance-improvements-on-the-horizon-for-spark-sql.htm

Select count(*) from t where b=1000 (filter,predicate,tuple read to CPU multiple times) https://databricks.com/blog/2015/04/13/deep-dive-into-spark-sqls-catalyst-optimizer.html 

Sort (Any type):  http://blog.csdn.net/wl044090432/article/details/52190736

* 3. Why need codegen?

Volcano style, iterator style

virtual function

boxing, unboxing

cpu register/memory access, cpu prefetch streamline failure(jmp/invoke, call EAX(dynamic address))

loop unrolling

SIMD (SSE instruction)

LLVM

pull vs. push(produce/consume)

* 4. Why need whole stage codegen?

No -> limited codegen (only for expression evaluation, projection, filter...) -> most of operators(aggregation,hash join...)


* 5. How

Transparent to developers

SQL/DF/DS (catalyst) 


* 6. Examples
ds.join(broadcast(ds)).explain(extended=true)

import org.apache.spark.sql.execution.debug._ 

spark.range(10).sample(false, 0.4).debug

spark.range(10).sample(false, 0.4).debugCodegen

spark.range(10).where('id === 4).debug

sql("explain codegen select 'a' as a group by 1").head

spark.range(1000L * 1000 * 1000).selectExpr("sum(id)").explain(extended=true)

spark.range(1000).filter("id > 100").selectExpr("sum(id)").explain()

'*' represents this step has whole stage codegen

HashAggregateExec  => codegen operator


* 7. Source code

SqlParser -> Analyzer(resolve, Rules...) -> LogicalPlan(Command, Unary, Binary Operators) -> Optimizer(Push down, pruning...) -> SparkPlan physical plan(SparkStrategies.batches) -> Cost selection -> Codegen pipelines(*Exec,HashAggregateExec...) -> RDD

WholeStageCodegen.scala => doCodeGen(), doExecute(), CodeGenrator.compile()

basicPhysicalOperators.scala

CodegenSupport

RangeExec

HashAggregateExec

BufferedRowIterator -- processNext()

spark.range(1000).filter("id > 100").selectExpr("sum(id)").debugCodegen  => How do the generated codes look like?

...

* 8. Benchmark (5x)

https://databricks-prod-cloudfront.cloud.databricks.com/public/4027ec902e239c93eaaa8714f173bcfc/6122906529858466/293651311471490/5382278320999420/latest.html

spark.conf.set("spark.sql.codegen.wholeStage", false) 

spark.range(1000L * 1000 * 1000).selectExpr("sum(id)").show()

spark.conf.set("spark.sql.codegen.wholeStage", true)

spark.range(1000L * 1000 * 1000).selectExpr("sum(id)").show()

* 9. Others

Not suitable for below sceanrios:

unbound data structure(String)

performance bottleneck is from IO

* 10. Reference

https://databricks.com/blog/2015/04/13/deep-dive-into-spark-sqls-catalyst-optimizer.html

https://databricks.com/blog/2016/05/23/apache-spark-as-a-compiler-joining-a-billion-rows-per-second-on-a-laptop.html

https://databricks.com/blog/2014/06/02/exciting-performance-improvements-on-the-horizon-for-spark-sql.html

http://blog.csdn.net/wl044090432/article/details/52190736

http://www.vldb.org/pvldb/vol4/p539-neumann.pdf

https://issues.apache.org/jira/browse/SPARK-12795

https://jaceklaskowski.gitbooks.io/mastering-apache-spark/content/spark-sql-whole-stage-codegen.html

https://github.com/apache/spark/blob/master/sql/core/src/main/scala/org/apache/spark/sql/execution

