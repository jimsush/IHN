package com.ihn.server.internal.algorithm.shortpath;

public class Dijkstra {
	
	IhnPoint root;
	IhnPoint[] points;
	IhnEdge[] edges;
	
	public IhnEdge[] walkRoute(double[] start, double[] end){
		String startId=getNearestPointId(start);
		String endId=getNearestPointId(end);
		return walkRoute(startId,endId);
	}
	
	public IhnEdge[] walkRoute(double[] start, String endId){
		String startId=getNearestPointId(start);
		return walkRoute(startId,endId);
	}
	
	public IhnEdge[] walkRoute(String startId, String nodeId){
		
		return null;
	}
	
	private String getNearestPointId(double[] sourcePoint){
		double nearestInstance=0;
		String nearestId=null;
		
		for(IhnPoint point : points){
			double instance=Math.sqrt(sourcePoint[0]-point.x)+Math.sqrt(sourcePoint[1]-point.y);
			if(instance<nearestInstance){
				nearestInstance=instance;
				nearestId=point.id;
			}
		}
		return nearestId;
	}

}
