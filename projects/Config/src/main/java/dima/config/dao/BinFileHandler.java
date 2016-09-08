package dima.config.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.NodeDeviceCfg;
import dima.config.common.models.NodeMessage;
import dima.config.common.models.NodeVL;
import dima.config.common.models.SwitchDevice;
import dima.config.common.models.SwitchMonitor;
import dima.config.common.models.SwitchMonitorCfg;
import dima.config.common.models.SwitchMonitorPort;
import dima.config.common.models.SwitchVL;
import dima.config.common.models.SwitchVLCfg;
import dima.config.common.models.SwitchVLPlan;

public class BinFileHandler {

	public static void main(String[] args) throws Exception{
		List<Integer> list1=bitsetIntToList(0x01020304, 0);
		List<Integer> list2 = bitsetIntToList(0x01010101, 32);
		list2.addAll(list1);
		int b[] = listToIntBitSet(list2);
		System.out.println(list2+"b="+Integer.toHexString(b[0])+" "+Integer.toHexString(b[1]));
	}
	
	public static SwitchDevice readSwitch(String swName) throws Exception{
		String fileName=ConfigUtils.getSwitchConfigFileName(swName);
		FileInputStream in = new FileInputStream(fileName);
		SwitchDevice sw=new SwitchDevice(swName);
		try {
			int data=readInt(in);
			sw.setVersion(data);
        	System.out.println("version:"+data);
        	
        	data=readInt(in);
        	sw.setDate(data);
        	System.out.println("date:"+data);
        	
        	data=readInt(in);
        	sw.setFileNo(data);
        	System.out.println("file id:"+data);
        	
        	int tableNumber=readInt(in);
        	System.out.println("table total number:"+tableNumber);
        	
        	byte[] names=new byte[16];
        	for(int i=0; i<16; i++){
        		names[i]=readByte(in);
        	}
        	String switchName=bytesToString(names);
        	System.out.println("switch name:"+switchName);
        	
        	data=readInt(in);
        	sw.setLocationId(data);
        	System.out.println("location ID:"+data);
        	
        	data=readInt(in);
        	sw.setPortNumber(data);
        	System.out.println("port number:"+data);
        	
        	data=readInt(in);
        	sw.setLocalDomainID(data);
        	System.out.println("local ID:"+data);
        	
        	data=readInt(in);
        	sw.setEportNumber(data);
        	System.out.println("eport number:"+data);
        	
        	StringBuilder sb1=new StringBuilder();
        	int a1=0;
        	for(int i=0; i<6; i++){
        		data=readInt(in); //0x10004, 65540
        		if(data>0){
        			String hexPortId=Integer.toHexString(data); //10004
        			if(a1==0){
        				sb1.append(hexPortId);
        			}else{
        				sb1.append(",").append(hexPortId);
        			}
        			a1++;
        		}
        	}
        	sw.setEportFEPortNos(sb1.toString());
        	System.out.println("eport FE:"+sw.getEportFEPortNos());
        	
        	data=readInt(in);
        	sw.setEnableTimeSyncVL(data==1);
        	System.out.println("Time sync VL enable:"+data);
        	
        	data=readInt(in);
        	sw.setTimeSyncVL(data);
        	System.out.println("Time sync VL:"+data);
        	
        	data=readInt(in);
        	sw.setTimeSyncRole(data);
        	System.out.println("Time sync role(0-SM 1-SC):"+data);
        	
        	data=readInt(in);
        	sw.setOverallInterval(data);
        	System.out.println("interval:"+data);
        	
        	data=readInt(in);
        	sw.setClusterInterval(data);
        	System.out.println("cluster interval:"+data);
        	
        	for(int i=0; i<ConfigContext.MAX_NUM_CFGTABLE; i++){
        		data=readInt(in);
        		System.out.println("file offset:"+data);
        	}
        	
        	List<SwitchVLCfg> vlCfgs=sw.getVlCfgs();
        	if(vlCfgs==null){
        		vlCfgs=new ArrayList<>();
        		sw.setVlCfgs(vlCfgs);
        	}
        	
        	int[] cfgPlanNums=new int[tableNumber];
        	for(int i=0; i<tableNumber; i++){ 
        		SwitchVLCfg vlCfg=new SwitchVLCfg();
        		vlCfgs.add(vlCfg);
        		
        		data=readInt(in);
        		System.out.println("table id:"+data);
        		vlCfg.setCfgTableId(data);
        		
        		cfgPlanNums[i]=readInt(in);
        		System.out.println("cfgNum of plan:"+cfgPlanNums[i]);
        		vlCfg.setPlanNum(cfgPlanNums[i]);
        		
        		data=readInt(in);
        		System.out.println("default plan id:"+data);
        		vlCfg.setDefaultPlanId(data);
        	}
        	
        	int[][] vlNum=new int[tableNumber][]; // table1: 3 plans, table2: 4 plans
        	for(int i=0; i<tableNumber; i++){
        		vlNum[i]=new int[cfgPlanNums[i]];
        		SwitchVLCfg vlCfg = vlCfgs.get(i);
        		List<SwitchVLPlan> plans=vlCfg.getPlans();
        		if(plans==null){
        			plans=new ArrayList<>();
        			vlCfg.setPlans(plans);
        		}
        		
        		for(int k=0; k<cfgPlanNums[i]; k++){
        			SwitchVLPlan plan=new SwitchVLPlan();
        			plans.add(plan);
        			
        			data=readInt(in);
        			System.out.println("plan id:"+data);
        			plan.setPlanId(data);
        			
        			vlNum[i][k]=readInt(in);
            		System.out.println("VL number:"+vlNum[i][k]);
            		plan.setVlNum(vlNum[i][k]);
        		}
        	}
 
        	for(int i=0; i<tableNumber; i++){
        		
        		SwitchVLCfg vlCfg = vlCfgs.get(i);
        		List<SwitchVLPlan> plans = vlCfg.getPlans();
        		
        		for(int k=0; k<cfgPlanNums[i]; k++){
        			
        			SwitchVLPlan plan = plans.get(k);
        			
        			for(int j=0; j<vlNum[i][k]; j++){
        				List<SwitchVL> vls=plan.getVls();
        				if(vls==null){
        					vls=new ArrayList<>();
        					plan.setVls(vls);
        				}
        				
        				data=readInt(in);
        				SwitchVL vl=new SwitchVL(swName, vlCfg.getCfgTableId(), plan.getPlanId(), data);
        				vls.add(vl);
        				System.out.println("VL id:"+data);
        				
        				data=readInt(in);
        				vl.setType(data);
        				System.out.println("VL type:"+data);
        				
        				data=readInt(in);
        				vl.setInputPortNo(data);
        				System.out.println("input port:"+data);
        				
        				data=readInt(in);
        				List<Integer> highOutPorts = bitsetIntToList(data, 32);
        				System.out.println("high output ports:"+highOutPorts);
        				
        				data=readInt(in);
        				List<Integer> lowOutPorts = bitsetIntToList(data, 0);
        				System.out.println("low output ports:"+lowOutPorts);
        				lowOutPorts.addAll(highOutPorts);
        				vl.setOutputPortNos(lowOutPorts); //low+high
        				System.out.println("final output ports:"+lowOutPorts);
        				
        				data=readInt(in);
        				System.out.println("reserved:"+data);
        				
        				data=readInt(in);
        				vl.setBag(data);
        				System.out.println("BAG:"+data);
        				
        				data=readInt(in);
        				vl.setJitter(data);
        				System.out.println("Jitter:"+data);
        				
        				data=readInt(in);
        				vl.setTtInterval(data);
        				System.out.println("tt send interval:"+data);
        				
        				data=readInt(in);
        				vl.setTtWindow(data);
        				System.out.println("tt send window:"+data);
        				
        				data=readInt(in);
        				System.out.println("reserved:"+data);
        				
        				data=readInt(in);
        				System.out.println("reserved:"+data);
        			}
        		}
        	}
        }catch(Exception ex){
        	ex.printStackTrace();
        }finally{
        	in.close();
        }
		return sw;
	}
	
	public static void writeSwitch(SwitchDevice sw) throws Exception{
		String fileName=ConfigUtils.getSwitchConfigFileName(sw.getSwitchName());
		OutputStream out = new FileOutputStream(fileName); 
		int curPos=0;
		try {
			if(sw.getVersion()==0){
				out.write(toBytes(ConfigContext.version));
			}else{
				out.write(toBytes(sw.getVersion())); //version
			}
			curPos+=4;
			
            if(sw.getDate()==0){
            	if(ConfigContext.date>0){
            		out.write(toBytes(ConfigContext.date));
            	}else{
		            String today=getToday();
		            // 8421 encoding
		            out.write(toBytes(Integer.valueOf(today, 16)));
            	}
            }else{
            	out.write(toBytes(sw.getDate()));
            }
            curPos+=4;
            
            if(sw.getFileNo()==0){
            	out.write(toBytes(ConfigContext.fileNo));
            }else{
            	out.write(toBytes(sw.getFileNo()));
            }
            curPos+=4;
            
            out.write(toBytes(sw.getNumberOfConfigTable()));
            curPos+=4;
            
            byte[] bytes1 = stringToBytes(sw.getSwitchName(), 16);
            for(int i=0; i<16; i++){
            	out.write(bytes1[i]);
            }
            curPos+=16;
            
            out.write(toBytes(sw.getLocationId()));
            curPos+=4;
            out.write(toBytes(sw.getPortNumber()));
            curPos+=4;
            out.write(toBytes(sw.getLocalDomainID()));
            curPos+=4;
            
            out.write(toBytes(sw.getEportNumber()));
            curPos+=4;
            
            List<Integer> portIds=sw.getEportFEs();
            int portSize=portIds.size();
            for(int i=0; i<6; i++){
            	if(portSize>i){
            		int portNo=portIds.get(i);
            		out.write(toBytes(portNo)); //0x10004, 65540
            	}else{
            		out.write(toBytes(0));
            	}
            }
            curPos+=(4*6);
            
            out.write(toBytes(sw.isEnableTimeSyncVL()?1:0));
            curPos+=4;
            out.write(toBytes(sw.getTimeSyncVL()));
            curPos+=4;
            out.write(toBytes(sw.getTimeSyncRole()));
            curPos+=4;
            
            out.write(toBytes(sw.getOverallInterval()));
            curPos+=4;
            out.write(toBytes(sw.getClusterInterval()));
            curPos+=4;
            
            List<SwitchVLCfg> vlCfgs = sw.getVlCfgs();
            int cfgSize=vlCfgs.size();
            for(int i=0; i<ConfigContext.MAX_NUM_CFGTABLE; i++){
            	if(cfgSize<=i){
            		out.write(toBytes(0));
            	}else{
            		out.write(toBytes(curPos+4*ConfigContext.MAX_NUM_CFGTABLE+4*3*i));
            	}
            }
            curPos+=(4*ConfigContext.MAX_NUM_CFGTABLE);
  
            for(SwitchVLCfg cfg : vlCfgs){
            	out.write(toBytes(cfg.getCfgTableId()));
            	curPos+=4;
            	out.write(toBytes(cfg.getPlanNum()));
            	curPos+=4;
            	out.write(toBytes(cfg.getDefaultPlanId()));
            	curPos+=4;
            }
            
            for(SwitchVLCfg cfg : vlCfgs){
            	List<SwitchVLPlan> plans = cfg.getPlans();
            	for(SwitchVLPlan plan : plans){
            		out.write(toBytes(plan.getPlanId()));
            		curPos+=4;
            		out.write(toBytes(plan.getVlNum()));
            		curPos+=4;
            	}
            }

            for(SwitchVLCfg cfg : vlCfgs){
            	List<SwitchVLPlan> plans = cfg.getPlans();
            	for(SwitchVLPlan plan : plans){
            		List<SwitchVL> vls = plan.getVls();
            		for(SwitchVL vl : vls){
            			out.write(toBytes(vl.getVLID()));
            			curPos+=4;
            			out.write(toBytes(vl.getType()));
            			curPos+=4;
            			out.write(toBytes(vl.getInputPortNo()));
            			curPos+=4;
	                	int[] outputPorts=listToIntBitSet(vl.getOutputPortNos());
	                	out.write(toBytes(outputPorts[0]));
	                	curPos+=4;
	                	out.write(toBytes(outputPorts[1]));
	                	curPos+=4;
	                	
	                	out.write(toBytes(0));
	                	curPos+=4;
	                	
	                	out.write(toBytes(vl.getBag()));
	                	curPos+=4;
	                	out.write(toBytes(vl.getJitter()));
	                	curPos+=4;
	                	
	                	out.write(toBytes(vl.getTtInterval()));
	                	curPos+=4;
	                	out.write(toBytes(vl.getTtWindow()));
	                	curPos+=4;
	                	
	                	out.write(toBytes(0));
	                	curPos+=4;
	                	out.write(toBytes(0));
	                	curPos+=4;
            		}
            	}
            }
            System.out.println("write "+curPos+" for "+sw.getSwitchName() +" to "+fileName);
        }catch(Exception ex){
        	ex.printStackTrace();
        }finally{
        	out.close();
        }
	}
	
	public static NodeDevice readNMUOrNode(String nameOrOrPortNo, int type) throws Exception{
		String fileName=null;
		if(type==ConfigUtils.TYPE_NMU){
			fileName=ConfigUtils.getNMUConfigFileName(nameOrOrPortNo);
		}else{
			fileName=ConfigUtils.getNodeConfigFileName(nameOrOrPortNo); 
		}
		FileInputStream in = new FileInputStream(fileName);
		NodeDevice nodeDev=null;
		try {
			int data=readInt(in);
			int version=data;
        	System.out.println("version:"+data);
        	
        	data=readInt(in);
        	int date=data;
        	System.out.println("date:"+data);
        	
        	data=readInt(in);
        	int fileNo=data;
        	System.out.println("file id:"+data);
        	
        	int tableNumber=readInt(in);
        	System.out.println("table total number:"+tableNumber);
        	
        	byte[] names=new byte[16];
        	for(int i=0; i<16; i++){
        		names[i]=readByte(in);
        	}
        	String nodeName=bytesToString(names);
        	System.out.println("node name:"+nodeName);
        	
        	nodeDev=new NodeDevice(nodeName, type);
        	nodeDev.setVersion(version);
        	nodeDev.setDate(date);
        	nodeDev.setFileNo(fileNo);
        	
        	data=readInt(in);
        	nodeDev.setLocationId(data);
        	System.out.println("location ID:"+data);
        	
        	data=readInt(in);
        	nodeDev.setPortNo(data);
        	System.out.println("port no:"+data);
        	
        	data=readInt(in);
        	nodeDev.setRoleOfNM(data);
        	System.out.println("Role of NM:"+data);
        	
        	data=readInt(in);
        	nodeDev.setRoleOfNetworkLoad(data);
        	System.out.println("Role of net loading:"+data);
        	
        	data=readInt(in);
        	nodeDev.setRoleOfTimeSync(data);
        	System.out.println("Role of time sync:"+data);
        	
        	data=readInt(in);
        	nodeDev.setRtcSendInterval(data);
        	System.out.println("RTC send interval:"+data);
        	
        	for(int i=0; i<ConfigContext.MAX_NUM_CFGTABLE; i++){
        		data=readInt(in);
        		System.out.println("file offset:"+data);
        	}
        	
        	int[] txNums=new int[tableNumber];
        	int[] rxNums=new int[tableNumber];
        	int[] txVLNums=new int[tableNumber];
        	int[] rxVLNums=new int[tableNumber];
        	
        	List<NodeDeviceCfg> cfgs = nodeDev.getCfgs();
        	for(int i=0; i<tableNumber; i++){
        		NodeDeviceCfg cfg=new NodeDeviceCfg();
        		cfgs.add(cfg);
        		
        		data=readInt(in);
        		System.out.println("table id:"+data);
        		cfg.setCfgTableId(data);
        		
        		data=readInt(in);
        		txNums[i]=data;
        		System.out.println("tx Number:"+data);
        		
        		data=readInt(in);
        		rxNums[i]=data;
        		System.out.println("rx Number:"+data);
        		
        		data=readInt(in);
        		txVLNums[i]=data;
        		System.out.println("tx VL Number:"+data);
        		
        		data=readInt(in);
        		rxVLNums[i]=data;
        		System.out.println("rx VL Number:"+data);
        	}
        	
        	for(int i=0; i<tableNumber; i++){
        		NodeDeviceCfg cfg = cfgs.get(i);
        		
        		List<NodeMessage> txMessages=new ArrayList<>();
        		cfg.setTxMessages(txMessages);
        		for(int j=0; j<txNums[i]; j++){
        			data=readInt(in);
        			NodeMessage msg=new NodeMessage(nodeName, data);
        			txMessages.add(msg);
            		System.out.println("tx message id:"+data);
            		
            		byte[] names2=new byte[16];
                	for(int x=0; x<16; x++){
                		names2[x]=readByte(in);
                	}
                	String txMsgName=bytesToString(names2);
                	msg.setMessageName(txMsgName);
                	System.out.println("tx message name:"+txMsgName);
            		
            		data=readInt(in);
            		msg.setVl(data);
            		System.out.println("tx VL:"+data);
            		
            		data=readInt(in);
            		msg.setMaxOfLen(data);
            		System.out.println("tx max of length:"+data);
            		
            		data=readInt(in);
            		msg.setUseOfMessage(data);
            		System.out.println("tx use:"+data);
            		
            		data=readInt(in);
            		msg.setSnmpID(data);
            		System.out.println("tx snmp id:"+data);
            		
            		data=readInt(in);
            		msg.setLoadID(data);
            		
            		System.out.println("tx loader id:"+data);
        		}
        	}
        	
        	for(int i=0; i<tableNumber; i++){
        		NodeDeviceCfg cfg = cfgs.get(i);
        		List<NodeMessage> rxMessages=new ArrayList<>();
            	cfg.setRxMessages(rxMessages);
        		
        		for(int j=0; j<rxNums[i]; j++){
        			data=readInt(in);
        			NodeMessage msg=new NodeMessage(nodeName, data);
        			rxMessages.add(msg);
            		System.out.println("rx message id:"+data);
            		
            		byte[] names2=new byte[16];
                	for(int x=0; x<16; x++){
                		names2[x]=readByte(in);
                	}
                	String rxMsgName=bytesToString(names2);
                	msg.setMessageName(rxMsgName);
                	System.out.println("rx message name:"+rxMsgName);
            		
            		data=readInt(in);
            		msg.setVl(data);
            		System.out.println("rx VL:"+data);
            		
            		data=readInt(in);
            		msg.setMaxOfLen(data);
            		System.out.println("rx max of length:"+data);
            		
            		data=readInt(in);
            		msg.setUseOfMessage(data);
            		System.out.println("rx use:"+data);
            		
            		data=readInt(in);
            		msg.setSnmpID(data);
            		System.out.println("rx snmp id:"+data);
            		
            		data=readInt(in);
            		msg.setLoadID(data);
            		System.out.println("rx loader id:"+data);
        		}
        	}

        	for(int i=0; i<tableNumber; i++){
        		NodeDeviceCfg cfg = cfgs.get(i);
        		List<NodeVL> txVLs=new ArrayList<>();
            	cfg.setTxVLs(txVLs);
            	
        		for(int j=0; j<txVLNums[i]; j++){
        			data=readInt(in);
        			
        			NodeVL vl=new NodeVL(nodeName, data);
        			txVLs.add(vl);
            		System.out.println("tx VL id:"+data);
  
            		data=readInt(in);
            		vl.setType(data);
            		System.out.println("tx VL type:"+data);
            		
            		data=readInt(in);
            		vl.setBag(data);
            		System.out.println("tx VL BAG:"+data);
            		
            		data=readInt(in);
            		vl.setJitter(data);
            		System.out.println("tx VL Jitter:"+data);
            		
            		data=readInt(in);
            		vl.setTtInterval(data);
            		System.out.println("tx VL TT sending frequency:"+data);
            		
            		data=readInt(in);
            		vl.setTtWindow(data);
            		System.out.println("tx VL TT sending window:"+data);
            		
            		data=readInt(in);
            		data=readInt(in);
            		
            		data=readInt(in);
            		vl.setNetworkType(data);
            		System.out.println("tx VL network choosen:"+data);
            		
            		data=readInt(in);
            		vl.setRedudanceType(data);
            		System.out.println("tx VL redundancy:"+data);
            		
            		data=readInt(in);
            		vl.setUseOfLink(data);
            		System.out.println("tx VL use of Link:"+data);
            		
            		data=readInt(in);
            		vl.setRtcInterval(data);
            		System.out.println("tx VL RTC frequency:"+data);
            		
            		data=readInt(in);
        		}
        	}
        	
        	
        	for(int i=0; i<tableNumber; i++){
        		NodeDeviceCfg cfg = cfgs.get(i);
        		List<NodeVL> rxVLs=new ArrayList<>();
            	cfg.setRxVLs(rxVLs);
            	
        		for(int j=0; j<rxVLNums[i]; j++){
        			data=readInt(in);
        			NodeVL vl=new NodeVL(nodeName, data);
        			rxVLs.add(vl);
            		System.out.println("rx VL id:"+data);
  
            		data=readInt(in);
            		vl.setType(data);
            		System.out.println("rx VL type:"+data);
            		
            		data=readInt(in);
            		vl.setCompleteCheck(data);
            		System.out.println("rx VL complete check:"+data);
            		
            		data=readInt(in);
            		vl.setRedudanceType(data);
            		System.out.println("rx VL redundancy:"+data);
            		
            		data=readInt(in);
            		vl.setUseOfLink(data);
            		System.out.println("rx VL use of Link:"+data);
            		
            		data=readInt(in);
        		}
        	}
        }catch(Exception ex){
        	ex.printStackTrace();
        }finally{
        	in.close();
        }
		return nodeDev;
	}
	
	public static void writeNMUOrNode(NodeDevice nodeDev) throws Exception{
		String fileName=null;
		
		int portNo=nodeDev.getPortNo();		
		if(ConfigUtils.TYPE_NMU==nodeDev.getType()){
			fileName=ConfigUtils.getNMUConfigFileName(nodeDev.getNodeName());
		}else{
			fileName=ConfigUtils.getNodeConfigFileName(portNo+"");
		}
		
		int curPos=0;
		OutputStream out = new FileOutputStream(fileName); 
		try {
			if(nodeDev.getVersion()==0){
				out.write(toBytes(ConfigContext.version));
			}else{
				out.write(toBytes(nodeDev.getVersion())); //version
			}
			curPos+=4;
			
            if(nodeDev.getDate()==0){
            	if(ConfigContext.date>0){
            		out.write(toBytes(ConfigContext.date));
            	}else{
		            String today=getToday();
		            // 8421 encoding
		            out.write(toBytes(Integer.valueOf(today, 16)));
            	}
            }else{
            	out.write(toBytes(nodeDev.getDate()));
            }
            curPos+=4;
            
            if(nodeDev.getFileNo()==0){
            	out.write(toBytes(ConfigContext.fileNo));
            }else{
            	out.write(toBytes(nodeDev.getFileNo()));
            }
            curPos+=4;
            
            List<NodeDeviceCfg> cfgs = nodeDev.getCfgs();
            out.write(toBytes(cfgs.size()));
            curPos+=4;
            
            byte[] bytes1 = stringToBytes(nodeDev.getNodeName(), 16);
            for(int i=0; i<16; i++){
            	out.write(bytes1[i]);
            }
            curPos+=16;
            
            out.write(toBytes(nodeDev.getLocationId()));
            curPos+=4;
            out.write(toBytes(portNo));
            curPos+=4;
            
            out.write(toBytes(nodeDev.getRoleOfNM()));
            curPos+=4;
            out.write(toBytes(nodeDev.getRoleOfNetworkLoad()));
            curPos+=4;
            out.write(toBytes(nodeDev.getRoleOfTimeSync()));
            curPos+=4;
            out.write(toBytes(nodeDev.getRtcSendInterval()));
            curPos+=4;
            
            int cfgSize=cfgs.size();
            for(int i=0; i<ConfigContext.MAX_NUM_CFGTABLE; i++){
            	if(cfgSize<=i){
            		out.write(toBytes(0));
            	}else{
            		out.write(toBytes(curPos+4*ConfigContext.MAX_NUM_CFGTABLE+4*5*i));
            	}
            }
            curPos+=(4*ConfigContext.MAX_NUM_CFGTABLE);
            
			for(NodeDeviceCfg cfg : cfgs){
            	out.write(toBytes(cfg.getCfgTableId()));
            	curPos+=4;
            	out.write(toBytes(cfg.getTxMessages().size()));
            	curPos+=4;
            	out.write(toBytes(cfg.getRxMessages().size()));
            	curPos+=4;
            	out.write(toBytes(cfg.getTxVLs().size()));
            	curPos+=4;
            	out.write(toBytes(cfg.getRxVLs().size()));
            	curPos+=4;
            }
            
			for(NodeDeviceCfg cfg : cfgs){
            	for(NodeMessage msg : cfg.getTxMessages()){
            		out.write(toBytes(msg.getMessageID()));
            		curPos+=4;
            		
            		byte[] b1 = stringToBytes(msg.getMessageName(), 16);
                    for(int x=0; x<16; x++){
                    	out.write(b1[x]);
                    }
                    curPos+=16;
                    
                    out.write(toBytes(msg.getVl()));
                    curPos+=4;
                    out.write(toBytes(msg.getMaxOfLen()));
                    curPos+=4;
                    out.write(toBytes(msg.getUseOfMessage()));
                    curPos+=4;
                    out.write(toBytes(msg.getSnmpID()));
                    curPos+=4;
                    out.write(toBytes(msg.getLoadID()));
                    curPos+=4;
            	}
            }

			for(NodeDeviceCfg cfg : cfgs){
            	for(NodeMessage msg : cfg.getRxMessages()){
            		out.write(toBytes(msg.getMessageID()));
            		curPos+=4;
            		
            		byte[] b1 = stringToBytes(msg.getMessageName(), 16);
                    for(int x=0; x<16; x++){
                    	out.write(b1[x]);
                    }
                    curPos+=16;
                    
                    out.write(toBytes(msg.getVl()));
                    curPos+=4;
                    out.write(toBytes(msg.getMaxOfLen()));
                    curPos+=4;
                    out.write(toBytes(msg.getUseOfMessage()));
                    curPos+=4;
                    out.write(toBytes(msg.getSnmpID()));
                    curPos+=4;
                    out.write(toBytes(msg.getLoadID()));
                    curPos+=4;
            	}
            }

			for(NodeDeviceCfg cfg : cfgs){
            	for(NodeVL vl : cfg.getTxVLs()){
            		out.write(toBytes(vl.getVLID()));
            		curPos+=4;
            		out.write(toBytes(vl.getType()));
            		curPos+=4;
            		out.write(toBytes(vl.getBag()));
            		curPos+=4;
            		out.write(toBytes(vl.getJitter()));
            		curPos+=4;
            		out.write(toBytes(vl.getTtInterval()));
            		curPos+=4;
            		out.write(toBytes(vl.getTtWindow()));
            		curPos+=4;
            		out.write(toBytes(0));
            		curPos+=4;
            		out.write(toBytes(0));
            		curPos+=4;
            		out.write(toBytes(vl.getNetworkType()));
            		curPos+=4;
            		out.write(toBytes(vl.getRedudanceType()));
            		curPos+=4;
            		out.write(toBytes(vl.getUseOfLink()));
            		curPos+=4;
            		out.write(toBytes(vl.getRtcInterval()));
            		curPos+=4;
            		out.write(toBytes(0));
            		curPos+=4;
            	}
            }
            
			for(NodeDeviceCfg cfg : cfgs){
            	for(NodeVL vl : cfg.getRxVLs()){
            		out.write(toBytes(vl.getVLID()));
            		curPos+=4;
            		out.write(toBytes(vl.getType()));
            		curPos+=4;
            		out.write(toBytes(vl.getCompleteCheck()));
            		curPos+=4;
            		out.write(toBytes(vl.getRedudanceType()));
            		curPos+=4;
            		out.write(toBytes(vl.getUseOfLink()));
            		curPos+=4;
            		out.write(toBytes(0));
            		curPos+=4;
            	}
            }
			System.out.println("write "+curPos+" to "+fileName);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			out.close();
		}
	}
	
	public static SwitchMonitor readMonitor(String swName) throws Exception{
		String fileName=ConfigUtils.getMonitorConfigFileName(swName);
		FileInputStream in = new FileInputStream(fileName);
		SwitchMonitor mon=new SwitchMonitor(swName);
		try {
			int data=readInt(in);
			mon.setVersion(data);
        	System.out.println("version:"+data);
        	
        	data=readInt(in);
        	mon.setDate(data);
        	System.out.println("date:"+data);
        	
        	data=readInt(in);
        	mon.setFileNo(data);
        	System.out.println("file id:"+data);
        	
        	int tableNumber=readInt(in);
        	mon.setNumberOfConfigTable(tableNumber);
        	System.out.println("table total number:"+tableNumber);
        	
        	data=readInt(in);
        	mon.setLocationId(data);
        	System.out.println("location id:"+data);
        	
        	for(int i=0; i<ConfigContext.MAX_NUM_CFGTABLE; i++){
        		data=readInt(in);
        		System.out.println("file "+i+" offset: "+data);
        	}
        	
        	int[] portNos=new int[tableNumber];
        	for(int i=0; i<tableNumber; i++){
        		data=readInt(in);
        		portNos[i]=data;
        		System.out.println("file "+i+" port No.: "+data);
        	}
        	
        	List<SwitchMonitorCfg> cfgs=mon.getMonitorCfgs();
        	if(cfgs==null){
        		cfgs=new ArrayList<>();
        		mon.setMonitorCfgs(cfgs);
        	}
        	
        	for(int i=0; i<tableNumber; i++){
        		int cfgTableId=i+1;
        		SwitchMonitorCfg cfg=new SwitchMonitorCfg(cfgTableId);
        		cfg.setMirrorPortNum(portNos[i]);
        		cfgs.add(cfg);
        		
        		for(int j=0; j<portNos[i]; j++){
        			int portId=j+1;
        			SwitchMonitorPort monPort=new SwitchMonitorPort(swName,cfgTableId,portId);
					cfg.addMonitorPort(monPort);
					
					data = readInt(in);
					monPort.setPortEnableMonitor(data);
					System.out.println("monitor status: " + data);

					data = readInt(in);
					monPort.setPortMonitorMode(data);
					System.out.println("monitor mode: " + data);

					data = readInt(in);
					List<Integer> inputPorts = bitsetIntToList(data, 0);
					monPort.setPortInputPortList(inputPorts);
					System.out.println("input ports: " + inputPorts);

					data = readInt(in);
					List<Integer> highOputPorts = bitsetIntToList(data, 32);
					data = readInt(in);
					List<Integer> lowOputPorts = bitsetIntToList(data, 0);
					lowOputPorts.addAll(highOputPorts);
					monPort.setPortOutputPortList(lowOputPorts);
					System.out.println("output ports: " + inputPorts);

					data = readInt(in);
					System.out.println("VL number: " + data);

					List<Integer> vls = new ArrayList<>();
					monPort.setPortVLList(vls);
					for (int x = 0; x < data; x++) {
						data = readInt(in);
						vls.add(data);
					}
					System.out.println("VL: " + vls);
        		}
        	}
        	
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			in.close();
		}
		return mon;
	}
	
	public static void writeMonitor(SwitchMonitor mon) throws Exception{
		String fileName=ConfigUtils.getMonitorConfigFileName(mon.getSwitchName());
		OutputStream out = new FileOutputStream(fileName); 
		int curPos=0;
		try {
			if(mon.getVersion()==0){
				out.write(toBytes(ConfigContext.version));
			}else{
				out.write(toBytes(mon.getVersion())); // version
			}
			curPos+=4;
			
            if(mon.getDate()==0){
            	if(ConfigContext.date>0){
            		out.write(toBytes(ConfigContext.date));
            	}else{
            		String today=getToday();
		            // 8421 encoding
		            out.write(toBytes(Integer.valueOf(today, 16)));
            	}
            }else{
            	out.write(toBytes(mon.getDate()));
            }
            curPos+=4;
            
            if(mon.getFileNo()==0){
            	out.write(toBytes(ConfigContext.fileNo));
            }else{
            	out.write(toBytes(mon.getFileNo()));
            }
            curPos+=4;
            
            out.write(toBytes(mon.getNumberOfConfigTable()));
            curPos+=4;
            
            out.write(toBytes(mon.getLocationId()));
            curPos+=4;
            
            List<SwitchMonitorCfg> cfgs = mon.getMonitorCfgs();
            int cfgSize=cfgs.size();
            for(int i=0; i<ConfigContext.MAX_NUM_CFGTABLE; i++){ //offset
            	if(cfgSize<=i){
            		out.write(toBytes(0));
            	}else{
            		out.write(toBytes(curPos+4*ConfigContext.MAX_NUM_CFGTABLE+4*i));
            	}
            }
            curPos+=(4*ConfigContext.MAX_NUM_CFGTABLE); 
            
            for(SwitchMonitorCfg cfg : cfgs){
            	out.write(toBytes(cfg.getMirrorPortNum()));
            	curPos+=4;
            }
            
            for(SwitchMonitorCfg cfg : cfgs){
            	List<SwitchMonitorPort> ports = cfg.getMonitorPorts();
            	for(SwitchMonitorPort port : ports){
					out.write(toBytes(port.getPortEnableMonitor()));
					curPos+=4;
					out.write(toBytes(port.getPortMonitorMode()));
					curPos+=4;
					
					int[] intputPorts = listToIntBitSet(port.getPortInputPortList());
					out.write(toBytes(intputPorts[1]));
					curPos+=4;
					
					int[] outputPorts = listToIntBitSet(port.getPortOutputPortList());
					out.write(toBytes(outputPorts[0]));
					curPos+=4;
					out.write(toBytes(outputPorts[1]));
					curPos+=4;
					
					out.write(toBytes(port.getPortVLList().size()));
					curPos+=4;
					
					for (Integer vl : port.getPortVLList()) {
						out.write(toBytes(vl));
						curPos+=4;
					}
            	}
            }
            System.out.println("write "+curPos+" to "+fileName);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			out.close();
		}
	}
	
	/** [0] is high code, [1] is low code; port no is from 0-16, 16 is NMU */
	private static int[] listToIntBitSet(List<Integer> portNos){
		int lowCode=0;
		int highCode=0;
		for(Integer portNo : portNos){
			if(portNo>=32){
				highCode += 1<<(portNo-32);
			}else{
				lowCode += 1<<(portNo);  // port no is from 0-16, 16 is NMU
			}
		}
		int[] res=new int[]{highCode, lowCode};
		return res;
	}
	
	/** 
	 * port no is from 0 to 16, 16 is NMU port
	 */
	private static List<Integer> bitsetIntToList(int d, int offset){
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0; i<32; i++){
			int mask=1<<i;
			if((d & mask)!=0){
				list.add((offset+i)); 
			}
		}
		return list;
	}
	
	public static byte[] stringToBytes(String str, int bytesLen){
		byte[] result=new byte[bytesLen];
		if(str==null){
			return result;
		}
		
		char[] names = str.toCharArray();
        for(int i=0; i<names.length; i++){
        	result[i]=(byte)names[i];
        }
        return result;
	}
	
	public static String bytesToString(byte[] names){
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
	
	public static BitSet getBitSet(int num){
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

	public static byte[] toBytes(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}
	
	public static int toInt(byte[] b) {
		int value = 0;
	    for (int i = 0; i < 4; i++) {
	        int shift = (4 - 1 - i) * 8;
	        value += (b[i] & 0x000000FF) << shift;
	    }
	    return value;
	}
	
	private static int readInt(FileInputStream in){
		byte[] bs=new byte[4];
		try{
			in.read(bs, 0, 4);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return toInt(bs);
	}
	
	public static byte readByte(FileInputStream in){
		byte[] bs=new byte[1];
		try{
			in.read(bs, 0, 1);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bs[0];
	}

	@SuppressWarnings("deprecation")
	private static String getToday(){
		Date today=new Date();
        StringBuilder date1=new StringBuilder();
        date1.append(today.getYear()+1900);
        if(today.getMonth()<9){
        	date1.append("0").append(today.getMonth()+1);
        }else{
        	date1.append(today.getMonth()+1);
        }
        if(today.getDate()<10){
        	date1.append("0").append(today.getDate());
        }else{
        	date1.append(today.getDate());
        }
        return date1.toString();
	}

}
