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
    
    

2. A
