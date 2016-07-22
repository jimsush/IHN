package com.citi.dsp.client.my;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class WriteMyObject {
	
	public static void main(String[] args) throws Exception{
		writeObj("SW_01");
		readObj("SW_01");
		
		/*
		test(new int[]{0});
		test(new int[]{1});
		test(new int[]{1,2});
		test(new int[]{1,3});
		test(new int[]{1,2,3});
		test(new int[]{16});
		test(new int[]{16,17});
		*/
	}
	
	private static void readObj(String swName) throws Exception{
		if(swName==null){
			swName="SW_01";
		}
		String fileName="switch_"+swName+".bin";
		FileInputStream inputStream = new FileInputStream("C:\\software\\"+fileName);
		ObjectInputStream in = null;
        try {
        	in = new ObjectInputStream(inputStream);
        	System.out.println("version:"+in.readInt());
        	System.out.println("date:"+in.readInt());
        	System.out.println("file id:"+in.readInt());
        	
        	int tableNumber=in.readInt();
        	System.out.println("table total number:"+tableNumber);
        	
        	byte[] names=new byte[16];
        	for(int i=0; i<16; i++){
        		names[i]=in.readByte();
        	}
        	String name=bytesToString(names);
        	System.out.println("switch name:"+name);
        	
        	System.out.println("location:"+in.readInt());
        	System.out.println("port number:"+in.readInt());
        	System.out.println("local ID:"+in.readInt());
        	System.out.println("eport number:"+in.readInt());
        	
        	for(int i=0; i<6; i++){
        		System.out.println("eport FE:"+in.readInt());
        	}
        	
        	System.out.println("VL enable:"+in.readInt());
        	System.out.println("VL:"+in.readInt());
        	
        	System.out.println("time sync role:"+in.readInt());
        	System.out.println("interval:"+in.readInt());
        	System.out.println("cluster interval:"+in.readInt());
        	
        	for(int i=0; i<4; i++){
        		System.out.println("file offset:"+in.readInt());
        	}
        	
        	int[] cfgPlanNums=new int[tableNumber];
        	for(int i=0; i<tableNumber; i++){
        		System.out.println("table id:"+in.readInt());
        		cfgPlanNums[i]=in.readInt();
        		System.out.println("cfgNum of plan:"+cfgPlanNums[i]);
        		System.out.println("default plan id:"+in.readInt());
        	}
        	
        	int[][] vlNum=new int[tableNumber][]; // table1: 3 plans, table2: 4 plans
        	for(int i=0; i<tableNumber; i++){
        		vlNum[i]=new int[cfgPlanNums[i]];
        		for(int k=0; k<cfgPlanNums[i]; k++){
        			System.out.println("plan id:"+in.readInt());
        			vlNum[i][k]=in.readInt();
            		System.out.println("VL number:"+vlNum[i][k]);
        		}
        	}
        	
        	for(int i=0; i<tableNumber; i++){
        		for(int k=0; k<cfgPlanNums[i]; k++){
        			for(int j=0; j<vlNum[i][k]; j++){
        				System.out.println("VL id:"+in.readInt());
        				System.out.println("VL type:"+in.readInt());
        				System.out.println("input port:"+in.readInt());
        				System.out.println("high output port:"+bitsetIntToList(in.readInt()));
        				List<Integer> outputPorts = bitsetIntToList(in.readInt());
        				System.out.println("low output port:"+outputPorts);
        				
        				System.out.println("reserved:"+in.readInt());
        				System.out.println("BAG:"+in.readInt());
        				System.out.println("Jitter:"+in.readInt());
        				System.out.println("tt send interval:"+in.readInt());
        				System.out.println("tt send window:"+in.readInt());
        				System.out.println("reserved:"+in.readInt());
        				System.out.println("reserved:"+in.readInt());
        				
        			}
        		}
        	}
        	
        	
        	
        }catch(Exception ex){
        	ex.printStackTrace();
        }finally{
        	in.close();
        	inputStream.close();
        }
	}
	
	private static void writeObj(String swName) throws Exception{
		if(swName==null){
			swName="SW_01";
		}
		String fileName="switch_"+swName+".bin";
		
		OutputStream outputStream = new FileOutputStream("C:\\software\\"+fileName); 
		ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(outputStream);
            out.writeInt(1);
            out.writeInt(20160722);
            out.writeInt(1);
            out.writeInt(1);

            byte[] bytes1 = stringToBytes(swName, 16);
            for(int i=0; i<16; i++){
            	out.writeByte(bytes1[i]);
            }
            
            out.writeInt(0);
            out.writeInt(16);
            out.writeInt(1);
            
            out.writeInt(6);
            for(int i=0; i<6; i++){
            	out.writeInt(i+1);
            }
            
            out.writeInt(1);
            out.writeInt(1);
            out.writeInt(0);
            
            out.writeInt(50);
            out.writeInt(50);
            
            for(int i=0; i<4; i++){
            	out.writeInt(0);
            }
            
            for(int i=0; i<1; i++){
            	out.writeInt(1);
            	out.writeInt(1);
            	out.writeInt(1);
            }
            
            for(int i=0; i<1; i++){
            	for(int k=0; k<1; k++){
            		out.writeInt(1);
                	out.writeInt(2);
            	}
            }
            
            for(int i=0; i<1; i++){
            	for(int k=0; k<1; k++){
            		for(int j=0; j<2; j++){
	            		out.writeInt(i);
	                	out.writeInt(1);
	                	out.writeInt(5);
	                	
	                	out.writeInt(0); 
	                	int outputPorts=listToIntBitSet(array2List(new int[]{1,3,8,16}));
	                	out.writeInt(outputPorts);
	                	
	                	out.writeInt(0);
	                	
	                	out.writeInt(500);
	                	out.writeInt(500);
	                	
	                	out.writeInt(60);
	                	out.writeInt(60);
	                	
	                	out.writeInt(0);
	                	out.writeInt(0);
            		}
            	}
            }
            
        }catch(Exception ex){
        	ex.printStackTrace();
        }finally{
        	out.close();
        	outputStream.close();
        }
	}
	
	private static int listToIntBitSet(List<Integer> portNos){
		int code=0;
		for(Integer portNo : portNos){
			code += 1<<(portNo-1);  // port no is from 1-17
		}
		return code;
	}
	
	private static List<Integer> bitsetIntToList(int d){
		List<Integer> list=new ArrayList<Integer>();
		BitSet bs=getBitSet(d);
		for(int i=0; i<32; i++){
			if(bs.get(i)){
				list.add((16-i));
			}
		}
		return list;
	}
	
	private static List<Integer> array2List(int[] array){
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0; i<array.length; i++){
			list.add(array[i]);
		}
		return list;
	}
	
	private static byte[] stringToBytes(String str, int bytesLen){
		char[] names = str.toCharArray();
		byte[] result=new byte[bytesLen];

        for(int i=0; i<names.length; i++){
        	result[i]=(byte)names[i];
        }
        return result;
	}
	
	private static String bytesToString(byte[] names){
		int nameLen=-1;
    	for(int i=0; i<names.length; i++){
    		if(names[i]==0 && nameLen==-1){
    			nameLen=i;
    		}
    	}
    	if(nameLen==-1)
    		nameLen=names.length;
    	
    	byte[] newnames=new byte[nameLen];
    	for(int i=0; i<nameLen; i++){
    		newnames[i]=names[i];
    	}
    	return new String(newnames);
	}
	
	private static BitSet getBitSet(int num){
	    char[] bits = Integer.toBinaryString(num).toCharArray();  
	    BitSet bitSet = new BitSet(bits.length);  
	    for(int i = 0; i < bits.length; i++){  
	        if(bits[i] == '1'){
	            bitSet.set(i, true);
	        }
	        else{
	            bitSet.set(i, false);
	        }                
	    }
	    return bitSet;
	}  
	

}
