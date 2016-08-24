1. Apache Kylin, big data analytics engine on Hadoop, it provideds SQL and OLAP interfaces. It is originally invented by eBay.
    
    China Beijing mobile: 
    30B rows and 4TB per day, and total capacity is 400TB. 800 jobs are running per day, so the hadoop cluster is very busy
    flexible ad-hoc queries from clients, but Hive is slow and SparkSQL used too much memory
    data mart is based on MySQL, so the big data platform are seprated with data mart(front query service to clients)

    Meituan.com OLAP:
    
    Advantages:
    Kylin is very fast, 40X than Spark SQL
    easy to deploy
    
    process:
    create cube (MR jobs, hive, star schema, store to HBase)
    query on SQL
    
    http://kylin.apache.org/   Kylin1.3/1.5 (HBase multiple scans)
    
    other similar products: Presto(10+Billions), Druid(in-memory, suitable 10+Millions)
    typical use cases on cube: filter/aggregation on fact tables; joins; count distinct; sum; roll up; drill down;
    storage: HBase, HDFS
    
    

2. Apache Flink
3. Apache Flink: batch and stream processing
Like Spark Streaming, but Spark Streaming is based on micro-batching.
Flink core is a streaming data flow engine, it treats the batch with limited stream

consistency: best effort, at least once and exactly once. Flink is following exactly once.
stateless/state: FLink

http://www.zhihu.com/question/30151872?spm=5176.100239.blogcont57232.10.rKPXFg

RDD在运行时是表现为java objects的。通过引入Tungsten，这块有了些许的改变,在spark中，从1.5开始，所有的dataframe操作都是直接作用在tungsten的二进制数据上。
flink中的Dataset，对标spark中的Dataframe，在运行前会经过优化。
在spark 1.6，dataset API已经被引入spark了，也许最终会取代RDD 抽象。

spark把streaming看成是更快的批处理，而flink把批处理看成streaming的special case，它用的datastream，这与dataset是2个引擎。 flink提供了基于每个事件的流式处理机制，所以可以被认为是一个真正的流式计算。它非常像storm的model。
而spark，不是基于事件的粒度，而是用小批量来模拟流式，也就是多个事件的集合。所以spark被认为是近实时的处理系统。

Spark streaming 是更快的批处理，而Flink Batch是有限数据的流式计算。虽然大部分应用对准实时是可以接受的，但是也还是有很多应用需要event level的流式计算。这些应用更愿意选择storm而非spark streaming，现在，flink也许是一个更好的选择。

. 对 windowing 的支持
因为spark的小批量机制，spark对于windowing的支持非常有限。只能基于process time，且只能对batches来做window。
而Flink对window的支持非常到位，且Flink对windowing API的支持是相当给力的，允许基于process time,data time,record 来做windowing。

3. Aliyun ARMS PaaS(application real-time monitoring service)
It seems it is like ELK but stronger, it combined logging analysis, dashboard and data computation.


