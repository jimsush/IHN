package org.sf

import scala.collection.JavaConverters._
import scala.collection.mutable._
import scala.io._
import javax.management.remote.JMXConnector
import javax.management.remote.rmi.RMIConnectorServer
import javax.naming.Context
import javax.management.remote.JMXServiceURL
import javax.management.remote.JMXConnectorFactory
import javax.management.ObjectName


object JMXClient1 {

  def main(args : Array[String]) : Unit={
    
    val fileName="url_subnodes.txt"
    var serverAndSubnodes=List[Array[String]]()
    try{
      for{line <- Source.fromFile(fileName).getLines()
        if line.length>0
        if !line.startsWith("#")
      }{
        serverAndSubnodes=line.split(",")::serverAndSubnodes
      }
    }catch{
      case ex : Exception => println(ex)
    }

    val env1=Map[String, Any]((Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.rmi.registry.RegistryContextFactory")
        ,(RMIConnectorServer.JNDI_REBIND_ATTRIBUTE, "true")
        ,(JMXConnector.CREDENTIALS, Array[String]("super","dsp4jmx"))).asJava
    
    //serverAndRegions.zipWithIndex.foreach{case (k,v)=>(v,k)}.foreach(item =>{ 
      //println(item._1(1), item._2)
    //})
    serverAndSubnodes.foreach( (serverSubnodes : Array[String]) => {
      var sUrl = new JMXServiceURL(serverSubnodes(0));
      var connector=JMXConnectorFactory.connect(sUrl, env1);
      var conn=connector.getMBeanServerConnection();
      for{subnode <- serverSubnodes
         if !subnode.startsWith("service")  
      }{
        val lastTime=conn.getAttribute(new ObjectName("type=Node, name="+subnode), "lastTime");
        val size=conn.getAttribute(new ObjectName("type=Node, name="+subnode), "size");
        println(subnode+"\t"+lastTime+"\t" +size)
      }
      connector.close();
    });
    
  } 
 
}
