package dima.config.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dima.config.common.ConfigContext;
import dima.config.common.ConfigUtils;
import dima.config.common.models.NodeBin;
import dima.config.common.models.NodeDevice;
import dima.config.common.models.NodeMessage;
import dima.config.common.models.NodeVL;
import dima.config.common.models.SwitchBin;
import dima.config.common.models.SwitchDevice;
import dima.config.common.models.SwitchMonitor;
import dima.config.common.models.SwitchMonitorPort;
import dima.config.common.models.SwitchVL;
import dima.config.common.services.ConfigDAO;
import dima.config.common.services.ServiceFactory;

public class BinFileHandler {

	public static void main(String[] args) throws Exception{
		List<Integer> list1=ConfigUtils.bitsetIntToList(0x01020304, 0);
		List<Integer> list2 = ConfigUtils.bitsetIntToList(0x01010101, 32);
		list2.addAll(list1);
		int b[] = ConfigUtils.listToIntBitSet(list2);
		System.out.println(list2+"b="+Integer.toHexString(b[0])+" "+Integer.toHexString(b[1]));
	}
	
	
	
	
	public static SwitchDevice readSwitch(String swName) throws Exception{
		String fileName=ConfigUtils.getSwitchConfigFileName(swName);
		FileInputStream in = new FileInputStream(fileName);
		SwitchBin bin=new SwitchBin();
		
		try {
			byte[] bs=new byte[16];
        	for(int i=0; i<16; i++){
        		bs[i]=readByte(in);
        	}
        	String version=bytesToString(bs);		
        	bin.setVersion(version);
        	if(version==null || version.equals("")){
        		bin.setVersion(ConfigContext.version);
        	}
        	System.out.println("");
			System.out.println("version:"+version);
        	
			for(int i=0; i<16; i++){
        		bs[i]=readByte(in);
        	}
        	String date=bytesToString(bs);
        	bin.setDate(date);
        	if(date==null || date.equals("")){
        		bin.setDate(ConfigContext.date);
        	}
			System.out.println("date:"+date);
			
			int data=readInt(in); //3
			System.out.println("file length:"+data+" for "+fileName);
			
        	short fileNo=readShort(in);
        	bin.setFileNo(fileNo);  //4
        	//System.out.println("file id:"+fileNo);
        	
        	short switchNumber=readShort(in); //5
        	System.out.println("switch total number:"+switchNumber);
        	for(short i=0; i<switchNumber; i++){
        		int locationId=readInt(in);
        		System.out.println("switch "+i+" location id:"+locationId);
        		int startPosition=readInt(in);
        		System.out.println("switch "+i+" startPosition:"+startPosition);
        	}
        	
        	for(short x=0; x<switchNumber; x++){
        		byte[] names=new byte[16];
            	for(int i=0; i<16; i++){
            		names[i]=readByte(in);
            	}
            	String switchName=bytesToString(names);
            	System.out.println("switch name:"+switchName);
            	
            	SwitchDevice sw=new SwitchDevice(swName); //8
        		bin.addSwitchConfig(sw);
        		sw.setVersion(version);
        		sw.setDate(date);
        		sw.setFileNo(fileNo);
        		
        		int locationId=readInt(in); //9
        		System.out.println("switch "+x+" location id:"+locationId);
        		sw.setLocationId(locationId);
        		
        		byte domainId=readByte(in); //10
        		System.out.println("switch "+x+" local domain id:"+domainId);
        		sw.setLocalDomainID(domainId);
        		
        		short portNum=readShort(in); //11
        		System.out.println("switch "+x+" portNum:"+portNum);
        		sw.setPortNumber(portNum);
        		
        		byte eportNum=readByte(in); //12
        		System.out.println("switch "+x+" eportNum:"+eportNum);
        		sw.setEportNumber(eportNum);
        		
        		List<Integer> eportNos=new ArrayList<>();
        		for(int i=0; i<6; i++){
        			int eportNo=readInt(in); //13, eports, 4*6
        			if(eportNo>0){
        				eportNos.add(eportNo); //high 2 bytes(switch id), low 2 bytes(port number)
        			}
        		}
        		sw.setEportNos(eportNos);
        		System.out.println("switch "+x+" eports: "+eportNos);
        		
        		byte vlEnabled=readByte(in); //14
        		System.out.println("switch "+x+" vlEnabled:"+vlEnabled);
        		sw.setEnableTimeSyncVL(vlEnabled==1);
        		
        		short vl1=readShort(in); //15
        		System.out.println("switch "+x+" time sync vl1:"+vl1);
        		sw.setTimeSyncVL1(vl1);
        		
        		short vl2=readShort(in); //16
        		System.out.println("switch "+x+" time sync vl2:"+vl2);
        		sw.setTimeSyncVL2(vl2);
        		
        		short pcfvl=readShort(in); //17
        		System.out.println("switch "+x+" PCF vl:"+pcfvl);
        		sw.setPcfVL(pcfvl);
        		
        		byte roleOfTimeSync=readByte(in); //18
        		System.out.println("switch "+x+" roleOfTimeSync:"+roleOfTimeSync);
        		sw.setTimeSyncRole(roleOfTimeSync);
        		
        		int interval=readInt(in); //19
        		System.out.println("switch "+x+" overall interval:"+interval);
        		sw.setOverallInterval(interval);
        		
        		short clusterNumber=readShort(in); //20
        		System.out.println("switch "+x+" clusterNumber:"+clusterNumber);
        		sw.setClusterInterval(clusterNumber);
        		
        		short defaultPlanNo=readShort(in); //21
        		System.out.println("switch "+x+" defaultPlanNo:"+defaultPlanNo);
        		sw.setDefaultPlanNo(defaultPlanNo);
        		short planNumber=readShort(in); //22
        		System.out.println("switch "+x+" planNumber:"+planNumber);
        		sw.setPlanNum(planNumber);
        		short planNo=readShort(in); //23
        		System.out.println("switch "+x+" planNo:"+planNo);
        		sw.setPlanNo(planNo);
        		
        		short vlForwardNumber=readShort(in); //24
        		System.out.println("switch "+x+" vlForwardNumber:"+vlForwardNumber);
        		
        		int portsStatus=readInt(in); //25
            	System.out.println("switch "+x+" portStatus:"+portsStatus);
            	sw.setPortsStatus(portsStatus);
            	
        		int vlNumber=readInt(in); //26
        		System.out.println("switch "+x+" vlNumber:"+vlNumber);
        		
        		List<SwitchVL> vls = sw.getVls();
        		for(int j=0; j<vlNumber; j++){
        			int vlID=readShort2(in); //27
            		System.out.println("switch "+x+" vlID:"+vlID);
            		
            		SwitchVL vl=new SwitchVL(sw.getSwitchName(), vlID);
            		vls.add(vl);
            		
            		short type=readShort(in); //28
            		System.out.println("switch "+x+" vl type:"+type);
            		vl.setType(type);
            		
            		short inputPort=readShort(in); //29
            		System.out.println("switch "+x+" vl inputPort:"+inputPort);
            		vl.setInputPortNo(inputPort);
            		
            		int outputPorts=readInt(in); //30
            		System.out.println("switch "+x+" vl outputPorts:"+outputPorts);
            		List<Integer> portList = ConfigUtils.bitsetIntToList(outputPorts, 0);
            		vl.setOutputPortNos(portList);
            		
            		int bag=readShort2(in); //31
            		System.out.println("switch "+x+" vl bag:"+bag);
            		vl.setBag(bag);
            		
            		int jitter=readShort2(in); //31
            		System.out.println("switch "+x+" vl jitter:"+jitter);
            		vl.setJitter(jitter);
            		
            		short priority=readShort(in); //32
            		System.out.println("switch "+x+" vl priority:"+priority);
            		vl.setPriority(priority);
            		
            		int ttStart=readInt(in); //33
            		vl.setTtInterval(ttStart);
            		int ttSentInterval=readInt(in); //34
            		vl.setTtSentInterval(ttSentInterval);
            		int ttWindowOffset=readInt(in); //35
            		vl.setTtWindowOffset(ttWindowOffset);
            		int ttWindowStart=readInt(in); //36
            		vl.setTtWindowStart(ttWindowStart);
            		int ttWindowEnd=readInt(in); //37
            		vl.setTtWindowEnd(ttWindowEnd);
        		}
        		
        		// below is for NMU
            	byte[] bs1=new byte[16];
                for(int i=0; i<16; i++){
                	bs1[i]=readByte(in); //38
                }
                String nmuversion=bytesToString(bs1);
                	
                bs1=new byte[16];
                for(int i=0; i<16; i++){
                	bs1[i]=readByte(in); //39
                }
                String nmudate=bytesToString(bs1);
                	
                int nmuFileNo=readInt(in); //40
                short nmuConfigNumber=readShort(in); //41
                System.out.println("NMU nmuConfigNumber: "+nmuConfigNumber);
                	
                bs1=new byte[16];
                for(int i=0; i<16; i++){
                	bs1[i]=readByte(in); //42
                }
                String nmuName=bytesToString(bs1);
                NodeDevice nmu=new NodeDevice(nmuName, ConfigUtils.TYPE_NMU);
                sw.setNmu(nmu);
                nmu.setVersion(nmuversion);
                nmu.setDate(nmudate);
                nmu.setFileNo((short)nmuFileNo);
                	
                int nmuLocationId=readInt(in); //43
                nmu.setLocationId(nmuLocationId);
                	
                int nmuPortId=readInt(in);//44
                nmu.setPortNo(nmuPortId);
                	
                byte roleOfNM=readByte(in); //45
                nmu.setRoleOfNM(roleOfNM);
                byte roleOfNetworkLoad=readByte(in); //46
                nmu.setRoleOfNetworkLoad(roleOfNetworkLoad);
                byte nmuRoleOfTimeSync=readByte(in); //47
                nmu.setRoleOfTimeSync(nmuRoleOfTimeSync);
                	
                int rtcSentInterval=readInt(in);  //48
                nmu.setRtcSendInterval(rtcSentInterval);
                	
                int sendMsgNum=readInt(in);  //49
                data=readInt(in);  //50
                System.out.println("NMU send msg offset: "+data);
                
                int receiveMsgNum=readInt(in);  //51
                data=readInt(in);  //52
                System.out.println("NMU receive msg offset: "+data);
                
                int sendVLNum=readInt(in);  //53
                data=readInt(in);  //54
                System.out.println("NMU send vl offset: "+data);
                
                int receiveVLNum=readInt(in);  //55
                data=readInt(in);  //56
                System.out.println("NMU receive vl offset: "+data);
                	
                List<NodeMessage> txMsgs = nmu.getTxMsgs();
                for(int i=0; i<sendMsgNum; i++){
                	int msgId=readInt(in);  //57
                	NodeMessage msg=new NodeMessage(nmu.getNodeName(), msgId);
                	txMsgs.add(msg);
                		
                	bs1=new byte[16];
                    for(int m=0; m<16; m++){
                    	bs1[m]=readByte(in); //58
                    }
                    String msgName=bytesToString(bs1);
                    msg.setMessageName(msgName);
                    	
                    int vlid1=readShort2(in);  //59
                    msg.setVl(vlid1);
                    System.out.println("VL ID: "+vlid1);
                    	
                    int msgMaxOfLen=readInt(in);  //60
                    msg.setMaxOfLen(msgMaxOfLen);
                    short msgType=readShort(in);  //61
                    msg.setType(msgType);
                    int sid=readInt(in);  //62
                    msg.setSID(sid);
                    int did=readInt(in);  //63
                    msg.setDID(did);
                    	
                    short use=readShort(in);  //64
                    msg.setUseOfMessage(use);
                    short snmpId=readShort(in);  //65
                    msg.setSnmpID(snmpId);
                    short loaderId=readShort(in);  //66
                    msg.setLoadID(loaderId);
                }
                	
                List<NodeMessage> rxMsgs = nmu.getRxMsgs();
                for(int i=0; i<receiveMsgNum; i++){
                	int msgId=readInt(in);  //67
                	NodeMessage msg=new NodeMessage(nmu.getNodeName(), msgId);
                	rxMsgs.add(msg);
                		
                	bs1=new byte[16];
                    for(int m=0; m<16; m++){
                    	bs1[m]=readByte(in); //68
                    }
                    String msgName=bytesToString(bs1);
                    msg.setMessageName(msgName);
                    	
                    int vlid1=readShort2(in);  //69
                    msg.setVl(vlid1);
                    System.out.println("VL ID: "+vlid1);
                    	
                    int msgMaxOfLen=readInt(in);  //70
                    msg.setMaxOfLen(msgMaxOfLen);
                    short msgType=readShort(in);  //71
                    msg.setType(msgType);
                    int sid=readInt(in);  //72
                    msg.setSID(sid);
                    int did=readInt(in);  //73
                    msg.setDID(did);
                    	
                    short use=readShort(in);  //74
                    msg.setUseOfMessage(use);
                    short snmpId=readShort(in);  //75
                    msg.setSnmpID(snmpId);
                    short loaderId=readShort(in);  //76
                    msg.setLoadID(loaderId);
                }
                	
                List<NodeVL> txVls = nmu.getTxVls();
                for(int i=0; i<sendVLNum; i++){
                	int vlid=readShort2(in);  //77
                	NodeVL svl=new NodeVL(nmu.getNodeName(), vlid);
                    txVls.add(svl);
                    
                    short vltype=readShort(in);  //78
                    svl.setType(vltype);
                    
                    int vlbag=readShort2(in);  //79
                    svl.setBag(vlbag);
                    int vljitter=readShort2(in);  //79
                    svl.setJitter(vljitter);
                    	
                    int vlTtStart=readInt(in);  //80
                    svl.setTtInterval(vlTtStart);
                    int vlTtSentInterval=readInt(in);  //81
                    svl.setTtSentInterval(vlTtSentInterval);
                    	
                    int vlTtWindowOffset=readInt(in);  //82
                    svl.setTtWindowOffset(vlTtWindowOffset);
                    int vlTtWindowStart=readInt(in);  //83
                    svl.setTtWindowStart(vlTtWindowStart);
                    int vlTtWindowEnd=readInt(in);  //84
                    svl.setTtWindowEnd(vlTtWindowEnd);
                    	
                    short vlnetwork=readShort(in);  //85
                    svl.setNetworkType(vlnetwork);
                    short vlRedudancy=readShort(in);  //86
                    svl.setRedudanceType(vlRedudancy);
                    short vlUseOfLink=readShort(in);  //87
                    svl.setUseOfLink(vlUseOfLink);
                    	
                    int vlRtcInterval=readInt(in);  //88
                    svl.setRtcInterval(vlRtcInterval);
                    int vlSwitchPortNos=readInt(in);  //89
                    svl.setSwitchPortNo(vlSwitchPortNos);
                }
                	
                List<NodeVL> rxVls = nmu.getRxVls();
                for(int i=0; i<receiveVLNum; i++){
                	int vlid=readShort2(in);  //90
                	NodeVL svl=new NodeVL(nmu.getNodeName(), vlid);
                    rxVls.add(svl);
                    	
                    short vltype=readShort(in);  //91
                    svl.setType(vltype);
                    short vlComplete=readShort(in);  //92
                    svl.setCompleteCheck(vlComplete);
                    	
                    short vlRedudancy=readShort(in);  //93
                    svl.setRedudanceType(vlRedudancy);
                    	
                    short vlUseOfLink=readShort(in);  //94
                    svl.setUseOfLink(vlUseOfLink);
                    int vlSwitchPortNos=readInt(in);  //95
                    svl.setSwitchPortNo(vlSwitchPortNos);
                }
        	}
        	
        	int checksum=readInt(in);  //95 checksum is for whole file
            System.out.println("switch checksum:"+checksum);
		}catch(Exception ex){
        	ex.printStackTrace();
        }finally{
        	in.close();
        }
		
		if(bin.getSwCfgs().size()>0){
			return bin.getSwCfgs().get(0);
		}else{
			return null;
		}
	}
	
	public static void writeSwitch(SwitchDevice sc0) throws Exception{
		SwitchBin sw=new SwitchBin();
		sw.addSwitchConfig(sc0);
		List<SwitchDevice> cfgs = sw.getSwCfgs();
		
		String fileName=ConfigUtils.getSwitchConfigFileName(sc0.getSwitchName());
		OutputStream out = new FileOutputStream(fileName); 
		
		Map<Integer, Integer[]> switchConfigOffsets=new HashMap<>();
		Map<Integer, Integer[]> txMsgOffsets=new HashMap<>();
        Map<Integer, Integer[]> rxMsgOffsets=new HashMap<>();
        Map<Integer, Integer[]> txVLOffsets=new HashMap<>();
        Map<Integer, Integer[]> rxVLOffsets=new HashMap<>();
        
		List<Byte> list=new ArrayList<Byte>();
		int curPos=0;

		try {
			String ver=sw.getVersion();
			if(ver==null || "".equals(ver)){
				ver=ConfigContext.version;
			}
			byte[] bytes1 = stringToBytes(ver, 16);
			outWriteBytes(list, bytes1);
            curPos+=16;
			
            String dat=sw.getDate();
			if(dat==null || "".equals(dat)){
				dat=ConfigContext.date;
			}
			bytes1 = stringToBytes(dat, 16);
            outWriteBytes(list, bytes1);
            curPos+=16;
            
            outWriteBytes(list, toBytes(0)); //3, file length 
            curPos+=4;
            
            if(sw.getFileNo()==0){
            	outWriteBytes(list, toBytes(ConfigContext.fileNo));
            }else{
            	outWriteBytes(list, toBytes(sw.getFileNo()));
            }
            curPos+=2;
            
            int cfgNum = cfgs.size();
            outWriteBytes(list, toBytes((short)cfgNum));
            curPos+=2;
  
            int swIdx=0;
            for(SwitchDevice sc : cfgs){	
            	outWriteBytes(list, toBytes(sc.getLocationId())); //location ID, 6
                curPos+=4;
                
                outWriteBytes(list, toBytes(0)); //file offset, 7
                switchConfigOffsets.put(swIdx, new Integer[]{curPos, 0});
                curPos+=4;
                
                swIdx++;
            }
            
            // for each switch configuration
            swIdx=0;
            for(SwitchDevice sc : cfgs){
            	Integer[] swpos = switchConfigOffsets.get(swIdx);
            	swpos[1]=curPos;
            	
            	bytes1 = stringToBytes(sc.getSwitchName(), 16); 
            	outWriteBytes(list, bytes1); // 8
            	curPos+=16;
            
            	outWriteBytes(list, toBytes(sc.getLocationId()));
            	curPos+=4;
            
            	outWriteBytes(list, (byte)sc.getLocalDomainID());
                curPos+=1;
                
                outWriteBytes(list,toBytes((short)sc.getPortNumber()));
                curPos+=2;

                outWriteBytes(list, (byte)sc.getEportNumber());
                curPos+=1;
            
	            List<Integer> eportNos=sc.getEportNos(); //13, eports, 4*6
	            for(Integer eportNo : eportNos){
	            	outWriteBytes(list, toBytes(eportNo));
	            	curPos+=4;
	            }
	            int realEportNum=eportNos.size();
	            for(int n=realEportNum; n<6; n++){
	            	outWriteBytes(list, toBytes(0)); // fill 0
	            	curPos+=4;
	            }
	            
	            outWriteBytes(list, sc.isEnableTimeSyncVL()? (byte)1:(byte)0);
	            curPos+=1;
            
	            outWriteBytes(list, toBytes(sc.getTimeSyncVL1()));
	            curPos+=2;
	            
	            outWriteBytes(list, toBytes(sc.getTimeSyncVL2()));
	            curPos+=2;
	            
	            outWriteBytes(list, toBytes(sc.getPcfVL())); 
	            curPos+=2;
	            
	            outWriteBytes(list, (byte)sc.getTimeSyncRole());
	            curPos+=1;
            
	            outWriteBytes(list,toBytes(sc.getOverallInterval()));
	            curPos+=4;
	            outWriteBytes(list,toBytes((short)sc.getClusterInterval()));
	            curPos+=2;
	            
	            outWriteBytes(list,toBytes(sc.getDefaultPlanNo()));//TODO 21 default plan ID
	            curPos+=2;
            
	            outWriteBytes(list,toBytes(sc.getPlanNum()));//TODO 22 plan number
	            curPos+=2;
	            
	            outWriteBytes(list,toBytes(sc.getPlanNo()));//TODO 23 plan number
	            curPos+=2;

	            outWriteBytes(list, toBytes(sc.getVlFwdConfigNum())); //TODO 24 forward VL number
	            curPos+=2;
	            
	            int portsStatus = sc.getPortsStatus();
	            outWriteBytes(list, toBytes(portsStatus)); //25 port status
	            curPos+=4;
	            
	            List<SwitchVL> vls = sc.getVls();
	            outWriteBytes(list, toBytes(vls.size())); //26 VL Number
	            curPos+=4;
	           
	            // switch forward VL
	            for(SwitchVL vl : vls){
	            	outWriteBytes(list, toBytes((short)vl.getVLID())); //27, vl_id
	            	curPos+=2;
	            	outWriteBytes(list, toBytes(vl.getType()));
	            	curPos+=2;
	            	outWriteBytes(list, toBytes(vl.getInputPortNo()));
	            	curPos+=2;
	            		
	            	int[] outputPorts=ConfigUtils.listToIntBitSet(vl.getOutputPortNos());
	            	outWriteBytes(list, toBytes(outputPorts[1]));
	            	curPos+=4;
	                
	            	outWriteBytes(list, toBytes((short)vl.getBag()));
	            	curPos+=2;
	            	outWriteBytes(list, toBytes((short)vl.getJitter()));
	            	curPos+=2;
	                
	            	outWriteBytes(list, toBytes(vl.getPriority()));
	            	curPos+=2;
	            			
	            	outWriteBytes(list,toBytes(vl.getTtInterval()));
		            curPos+=4;
		            outWriteBytes(list,toBytes(vl.getTtSentInterval()));
		            curPos+=4;
		            outWriteBytes(list,toBytes(vl.getTtWindowOffset())); //35
		            curPos+=4;
		            outWriteBytes(list,toBytes(vl.getTtWindowStart())); //36
		            curPos+=4;
		            outWriteBytes(list,toBytes(vl.getTtWindowEnd())); //37
		            curPos+=4;
	            }
	            
	            // NMU
	            int startOfNmuPosition=curPos;
	            NodeDevice nmu=sc.getNmu();
	            if(nmu==null){
	            	nmu=new NodeDevice(sc.getSwitchName(), ConfigUtils.TYPE_NMU);
	            	sc.setNmu(nmu);
	            	
	            	ConfigDAO dao= ServiceFactory.getService(ConfigDAO.class);
	            	dao.updateNMUCache(nmu);
	            }
	            String nmuver=nmu.getVersion();
				if(nmuver==null || "".equals(nmuver)){
					nmuver=ConfigContext.version;
				}
				bytes1 = stringToBytes(nmuver, 16);
				outWriteBytes(list, bytes1);
				curPos+=16;
				
				String nmudate=nmu.getDate();
				if(nmudate==null || "".equals(nmudate)){
					nmudate=ConfigContext.date;
				}
				bytes1 = stringToBytes(nmudate, 16);
				outWriteBytes(list, bytes1);
				curPos+=16;
				
				int nmuFileNo=nmu.getFileNo();
				if(nmuFileNo==0){
					nmuFileNo=ConfigContext.fileNo;
				}
				outWriteBytes(list, toBytes(nmuFileNo)); //TODO 40, file no.
				curPos+=4;
				
				outWriteBytes(list, toBytes((short)1)); //TODO 41, number of configuration table
				curPos+=2;
				
				String nmuname=nmu.getNodeName();
				if(nmuname==null){
					nmuname="";
				}
				bytes1 = stringToBytes(nmuname, 16);
				outWriteBytes(list, bytes1); //42
				curPos+=16;
				
				outWriteBytes(list, toBytes(nmu.getLocationId())); //43
				curPos+=4;
				outWriteBytes(list, toBytes(nmu.getPortNo())); //44
				curPos+=4;
				outWriteBytes(list, (byte)nmu.getRoleOfNM());
				curPos+=1;
				outWriteBytes(list, (byte)nmu.getRoleOfNetworkLoad());
				curPos+=1;
				outWriteBytes(list, (byte)nmu.getRoleOfTimeSync());
				curPos+=1;
				outWriteBytes(list, toBytes(nmu.getRtcSendInterval()));//48
				curPos+=4;
				
				List<NodeMessage> txMsgs = nmu.getTxMsgs();
				outWriteBytes(list, toBytes(txMsgs.size()));//49
				curPos+=4;
				
				Integer[] txMsgStartPosition=new Integer[]{curPos, 0};
				txMsgOffsets.put(swIdx, txMsgStartPosition);
				outWriteBytes(list, toBytes(0));//TODO 50 tx msg start position
				curPos+=4;
				
				List<NodeMessage> rxMsgs = nmu.getRxMsgs();
				outWriteBytes(list, toBytes(rxMsgs.size()));//51
				curPos+=4;
				
				Integer[] rxMsgStartPosition=new Integer[]{curPos, 0};
				rxMsgOffsets.put(swIdx, rxMsgStartPosition);
				outWriteBytes(list, toBytes(0));//TODO 52 rx msg start position
				curPos+=4;
				
				List<NodeVL> txVls = nmu.getTxVls();
				outWriteBytes(list, toBytes(txVls.size()));//53
				curPos+=4;

				Integer[] txVLStartPosition=new Integer[]{curPos, 0};
				txVLOffsets.put(swIdx, txVLStartPosition);
				outWriteBytes(list, toBytes(0));//TODO 54, send vl start position
				curPos+=4;
	
				List<NodeVL> rxVls = nmu.getRxVls();
				outWriteBytes(list, toBytes(rxVls.size()));//55
				curPos+=4;
				
				Integer[] rxVLStartPosition=new Integer[]{curPos, 0};
				rxVLOffsets.put(swIdx, rxVLStartPosition);
				outWriteBytes(list, toBytes(0));//TODO 56, receive vl start position
				curPos+=4;
				
				// send messages
				txMsgStartPosition[1]=curPos-startOfNmuPosition;
				for(NodeMessage msg : txMsgs){
					outWriteBytes(list, toBytes(msg.getMessageID()));//57
					curPos+=4;
					
					String msgName=msg.getMessageName();
					if(msgName==null){
						msgName="";
					}
					bytes1 = stringToBytes(msgName, 16);
					outWriteBytes(list, bytes1); //58
					curPos+=16;
					
					outWriteBytes(list, toBytes((short)msg.getVl()));//59
					curPos+=2;
					
					outWriteBytes(list, toBytes(msg.getMaxOfLen()));//60
					curPos+=4;

					outWriteBytes(list, toBytes(msg.getType()));//61
					curPos+=2;
					outWriteBytes(list, toBytes(msg.getSID()));//62
					curPos+=4;
					outWriteBytes(list, toBytes(msg.getDID()));//63
					curPos+=4;
					
					outWriteBytes(list, toBytes(msg.getUseOfMessage()));//64
					curPos+=2;
					
					outWriteBytes(list, toBytes(msg.getSnmpID()));//65
					curPos+=2;
					
					outWriteBytes(list, toBytes(msg.getLoadID()));//66
					curPos+=2;
				}
				
				rxMsgStartPosition[1]=curPos-startOfNmuPosition;
				for(NodeMessage msg : rxMsgs){
					outWriteBytes(list, toBytes(msg.getMessageID()));//67
					curPos+=4;
					
					String msgName=msg.getMessageName();
					if(msgName==null){
						msgName="";
					}
					bytes1 = stringToBytes(msgName, 16);
					outWriteBytes(list, bytes1); //68
					curPos+=16;
					
					outWriteBytes(list, toBytes((short)msg.getVl()));//69
					curPos+=2;
					
					outWriteBytes(list, toBytes(msg.getMaxOfLen()));//70
					curPos+=4;

					outWriteBytes(list, toBytes(msg.getType()));//71
					curPos+=2;
					outWriteBytes(list, toBytes(msg.getSID()));//72
					curPos+=4;
					outWriteBytes(list, toBytes(msg.getDID()));//73
					curPos+=4;
					
					outWriteBytes(list, toBytes(msg.getUseOfMessage()));//74
					curPos+=2;
					
					outWriteBytes(list, toBytes(msg.getSnmpID()));//75
					curPos+=2;
					
					outWriteBytes(list, toBytes(msg.getLoadID()));//76
					curPos+=2;
				}

				txVLStartPosition[1]=curPos-startOfNmuPosition;
				for(NodeVL vl : txVls){
					outWriteBytes(list, toBytes((short)vl.getVLID())); //77
					curPos+=2;
					outWriteBytes(list, toBytes(vl.getType())); //78
					curPos+=2;
					
					outWriteBytes(list, toBytes((short)vl.getBag())); //79
					curPos+=2;
					outWriteBytes(list, toBytes((short)vl.getJitter())); //TODO 79???
					curPos+=2;
					
					outWriteBytes(list, toBytes(vl.getTtInterval())); 
					curPos+=4;
					outWriteBytes(list, toBytes(vl.getTtSentInterval())); 
					curPos+=4;
					outWriteBytes(list, toBytes(vl.getTtWindowOffset())); //82 
					curPos+=4;
					outWriteBytes(list, toBytes(vl.getTtWindowStart())); //83
					curPos+=4;
					outWriteBytes(list, toBytes(vl.getTtWindowEnd())); //84
					curPos+=4;
					
					outWriteBytes(list, toBytes(vl.getNetworkType())); //85
					curPos+=2;
					outWriteBytes(list, toBytes(vl.getRedudanceType())); //86
					curPos+=2;
					outWriteBytes(list, toBytes(vl.getUseOfLink())); //87
					curPos+=2;
					outWriteBytes(list, toBytes(vl.getRtcInterval())); //88
					curPos+=4;
					outWriteBytes(list, toBytes(vl.getSwitchPortNo())); //TODO: 89, input port nos
					curPos+=4;
				}
				
				rxVLStartPosition[1]=curPos-startOfNmuPosition;
				for(NodeVL vl : rxVls){
					outWriteBytes(list, toBytes((short)vl.getVLID())); //90
					curPos+=2;
					outWriteBytes(list, toBytes(vl.getType())); //91
					curPos+=2;
					
					outWriteBytes(list, toBytes(vl.getCompleteCheck())); //92
					curPos+=2;
					outWriteBytes(list, toBytes(vl.getRedudanceType())); //93
					curPos+=2;
					
					outWriteBytes(list, toBytes(vl.getUseOfLink())); //94
					curPos+=2;
					
					outWriteBytes(list, toBytes(vl.getSwitchPortNo())); //95
					curPos+=4;
				}

				swIdx++;
            }
 
            // set total length of file
            setBytesInList(list, 32, toBytes(curPos+4)); //add 4 bytes of checksum
            
            // set each switch's start position
            for(Map.Entry<Integer, Integer[]> entry: switchConfigOffsets.entrySet()){
            	Integer[] switchPosOffset = entry.getValue();
            	setBytesInList(list, switchPosOffset[0], toBytes(switchPosOffset[1]));
            }
            // set each NMU's start position of rx/tx messages and VLs
            for(Map.Entry<Integer, Integer[]> entry: txMsgOffsets.entrySet()){
            	Integer[] addressAndOffset = entry.getValue();
            	setBytesInList(list, addressAndOffset[0], toBytes(addressAndOffset[1]));
            }
            for(Map.Entry<Integer, Integer[]> entry: rxMsgOffsets.entrySet()){
            	Integer[] addressAndOffset = entry.getValue();
            	setBytesInList(list, addressAndOffset[0], toBytes(addressAndOffset[1]));
            }
            for(Map.Entry<Integer, Integer[]> entry: txVLOffsets.entrySet()){
            	Integer[] addressAndOffset = entry.getValue();
            	setBytesInList(list, addressAndOffset[0], toBytes(addressAndOffset[1]));
            }
            for(Map.Entry<Integer, Integer[]> entry: rxVLOffsets.entrySet()){
            	Integer[] addressAndOffset = entry.getValue();
            	setBytesInList(list, addressAndOffset[0], toBytes(addressAndOffset[1]));
            }
            
            int checksum=genChecksum(list);
			outWriteBytes(list, toBytes(checksum)); // 96, switch checksum for whole file
			curPos+=4;
			
            for(Byte b : list){
            	out.write(b);
            }
            
            System.out.println("write "+curPos+" to "+fileName);
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
		NodeBin bin=new NodeBin();
		
		try {
			byte[] bs=new byte[16];
        	for(int i=0; i<16; i++){
        		bs[i]=readByte(in);
        	}
        	String version=bytesToString(bs);
        	bin.setVersion(version);
        	if(version==null || version.equals("")){
        		bin.setVersion(ConfigContext.version);
        	}
        	System.out.println("");
			System.out.println("version:"+version);
        	
			for(int i=0; i<16; i++){
        		bs[i]=readByte(in);
        	}
        	String date=bytesToString(bs);
        	bin.setDate(date);
        	if(date==null || date.equals("")){
        		bin.setDate(ConfigContext.date);
        	}
			System.out.println("date:"+date);
        	
			int data=readInt(in);
			System.out.println("file length:"+data +" for "+fileName);
			
			short fileNo=readShort(in);
			bin.setFileNo(fileNo);  //4
        	System.out.println("file id:"+fileNo);
        	
        	short nodeNumber=readShort(in);//5
        	System.out.println("table total number:"+nodeNumber);
        	for(short i=0; i<nodeNumber; i++){
        		int locationId=readInt(in); //6
        		System.out.println("node "+i+" location id:"+locationId);
        		int startPosition=readInt(in); //7
        		System.out.println("node "+i+" startPosition:"+startPosition);
        	}
        	
        	for(short x=0; x<nodeNumber; x++){
            	byte[] names=new byte[16];
                for(int i=0; i<16; i++){
                	names[i]=readByte(in); //8
                }
               	String nodeName=bytesToString(names);
               	System.out.println("node name:"+nodeName);
               	
               	NodeDevice node=new NodeDevice(nodeName, ConfigUtils.TYPE_NODE);
        		bin.addNode(node);
        		
               	int locationId=readInt(in); //9
        		System.out.println("node "+x+" location id:"+locationId);
        		node.setLocationId(locationId);
        		
        		int portId=readInt(in); //10
        		System.out.println("node "+x+" portId:"+portId);
        		node.setPortNo(portId);
        		
        		byte roleOfNM=readByte(in); //11
        		System.out.println("node "+x+" roleOfNM:"+roleOfNM);
        		node.setRoleOfNM(roleOfNM);
        		byte roleOfNetworkLoad=readByte(in); //12
        		System.out.println("node "+x+" roleOfNetworkLoad:"+roleOfNetworkLoad);
        		node.setRoleOfNetworkLoad(roleOfNetworkLoad);
        		byte roleOfTimerSync=readByte(in); //13
        		System.out.println("node "+x+" roleOfTimerSync:"+roleOfTimerSync);
        		node.setRoleOfTimerSync(roleOfTimerSync);
        		
        		int rtcSendInterval=readInt(in); //14
        		System.out.println("node "+x+" rtcSendInterval:"+rtcSendInterval);
        		node.setRtcSendInterval(rtcSendInterval);
        		byte roleOfTimeSync=readByte(in); //15
        		System.out.println("node "+x+" roleOfTimeSync:"+roleOfTimeSync);
        		node.setRoleOfTimeSync(roleOfTimeSync);
        		
        		data=readInt(in); //16
        		System.out.println("node "+x+" overall interval:"+data);
        		node.setOverallInterval(data);
        		
        		short clusterInterval=readShort(in); //17
        		System.out.println("node "+x+" cluster interval:"+clusterInterval);
        		node.setClusterInterval(clusterInterval);
        		
        		int txMsgNum=readInt(in); //18
        		data=readInt(in); //19
        		int rxMsgNum=readInt(in); //20
        		data=readInt(in); //21
        		int txVLNum=readInt(in); //22
        		data=readInt(in); //23
        		int rxVLNum=readInt(in); //24
        		data=readInt(in); //25
        		
        		List<NodeMessage> txMsgs = node.getTxMsgs();
        		for(int i=0; i<txMsgNum; i++){
        			data=readInt(in); //26
        			NodeMessage msg=new NodeMessage(node.getNodeName(), data);
        			txMsgs.add(msg);
        			
        			names=new byte[16];
                	for(int k=0; k<16; k++){
                		names[k]=readByte(in); //27
                	}
                	String msgName=bytesToString(names);
                	msg.setMessageName(msgName);
                	
                	int vlid=readShort2(in); //28
                	msg.setVl(vlid);
                	System.out.println("VL ID: "+vlid);
                	
                	data=readInt(in); //29
                	msg.setMaxOfLen(data);
                	short msgtype=readShort(in); //30
                	msg.setType(msgtype);
                	data=readInt(in); //31
                	msg.setSID(data);
                	data=readInt(in); //32
                	msg.setDID(data);
                	short msgUse=readShort(in); //33
                	msg.setUseOfMessage(msgUse);
                	short snmpID=readShort(in); //34
                	msg.setSnmpID(snmpID);
                	short loadID=readShort(in);//35
                	msg.setLoadID(loadID);
        		}
        		List<NodeMessage> rxMsgs = node.getRxMsgs();
        		for(int i=0; i<rxMsgNum; i++){
        			data=readInt(in); //36
        			NodeMessage msg=new NodeMessage(node.getNodeName(), data);
        			rxMsgs.add(msg);
        			
        			names=new byte[16];
                	for(int k=0; k<16; k++){
                		names[k]=readByte(in); //37
                	}
                	String msgName=bytesToString(names);
                	msg.setMessageName(msgName);
                	
                	int vlid=readShort2(in); //38
                	msg.setVl(vlid);
                	System.out.println("VL ID:"+vlid);
                	
                	data=readInt(in); //39
                	msg.setMaxOfLen(data);
                	short msgtype=readShort(in); //40
                	msg.setType(msgtype);
                	data=readInt(in); //41
                	msg.setSID(data);
                	data=readInt(in); //42
                	msg.setDID(data);
                	short msgUse=readShort(in); //43
                	msg.setUseOfMessage(msgUse);
                	short snmpID=readShort(in); //44
                	msg.setSnmpID(snmpID);
                	short loadID=readShort(in);//45
                	msg.setLoadID(loadID);
        		}
        		
        		short sdata=0;
        		List<NodeVL> txVls = node.getTxVls();
        		for(int i=0; i<txVLNum; i++){
        			data=readShort2(in); //46, VL_ID
        			NodeVL vl=new NodeVL(node.getNodeName(), data);
        			txVls.add(vl);
        			
        			sdata=readShort(in); //47
        			vl.setType(sdata);
        			
        			data=readShort2(in); //48
        			vl.setBag(data);
        			data=readShort2(in); //48
        			vl.setJitter(data);
        			
        			data=readInt(in);// 49
        			vl.setTtInterval(data);
        			data=readInt(in);// 50
        			vl.setTtSentInterval(data);
        			data=readInt(in);// 51
        			vl.setTtWindowOffset(data);
        			data=readInt(in);// 52
        			vl.setTtWindowStart(data);
        			data=readInt(in);// 53
        			vl.setTtWindowEnd(data);
        			sdata=readShort(in); //54
        			vl.setNetworkType(sdata);
        			sdata=readShort(in); //55
        			vl.setRedudanceType(sdata);
        			sdata=readShort(in); //56
        			vl.setUseOfLink(sdata);
        			data=readInt(in);// 57
        			vl.setRtcInterval(data);
        			data=readInt(in);// 58
        			vl.setSwitchPortNo(data);
        		}
        		List<NodeVL> rxVls = node.getRxVls();
        		for(int i=0; i<rxVLNum; i++){
        			data=readShort2(in); //59, VL_ID
        			NodeVL vl=new NodeVL(node.getNodeName(), data);
        			rxVls.add(vl);
        			
        			sdata=readShort(in); //60
        			vl.setType(sdata);
        			sdata=readShort(in); //61
        			vl.setCompleteCheck(sdata);
        			sdata=readShort(in); //62
        			vl.setRedudanceType(sdata);
        			sdata=readShort(in); //63
        			vl.setUseOfLink(sdata);
        			data=readInt(in);// 64
        			vl.setSwitchPortNo(data);
        		}
        	}
        	
        	data=readInt(in);
    		System.out.println("node bin file checksum: "+data);
    		
        }catch(Exception ex){
        	ex.printStackTrace();
        }finally{
        	in.close();
        }
		
		if(bin.getNodeCfgs()!=null && bin.getNodeCfgs().size()>0){
			return bin.getNodeCfgs().get(0);
		}
		return null;
	}
	
	public static void writeNMUOrNode(NodeDevice nodeDev) throws Exception{
		String fileName=null;
		
		int portNo=nodeDev.getPortNo();		
		if(ConfigUtils.TYPE_NMU==nodeDev.getType()){
			//fileName=ConfigUtils.getNMUConfigFileName(nodeDev.getNodeName());
			ConfigDAO dao= ServiceFactory.getService(ConfigDAO.class);
			SwitchDevice sw = dao.readSwitchDevice(nodeDev.getNodeName(), true);
			sw.setNmu(nodeDev);
			writeSwitch(sw);
			return;
		}else{
			fileName=ConfigUtils.getNodeConfigFileName(portNo+"");
		}
		
		NodeBin nb=new NodeBin();
		nb.addNode(nodeDev);
		List<NodeDevice> nodeCfgs = nb.getNodeCfgs();

		Map<Integer, Integer[]> nodeConfigOffsets=new HashMap<>();
		Map<Integer, Integer[]> txMsgOffsets=new HashMap<>();
        Map<Integer, Integer[]> rxMsgOffsets=new HashMap<>();
        Map<Integer, Integer[]> txVLOffsets=new HashMap<>();
        Map<Integer, Integer[]> rxVLOffsets=new HashMap<>();
        
        int curPos=0;
		List<Byte> list=new ArrayList<Byte>();
		OutputStream out = new FileOutputStream(fileName); 
		
		try {
			String ver=nodeDev.getVersion();
			if(ver==null || "".equals(ver)){
				ver=ConfigContext.version;
			}
			byte[] bytes1 = stringToBytes(ver, 16);
            outWriteBytes(list, bytes1);
            curPos+=16;
			
            String dat=nodeDev.getDate();
			if(dat==null || "".equals(dat)){
				dat=ConfigContext.date;
			}
			bytes1 = stringToBytes(dat, 16);
            outWriteBytes(list, bytes1);
            curPos+=16;
            
            outWriteBytes(list, toBytes(0)); //3, file length 
            curPos+=4;
            
            if(nb.getFileNo()==0){
            	outWriteBytes(list, toBytes(ConfigContext.fileNo));
            }else{
            	outWriteBytes(list, toBytes(nb.getFileNo())); //4
            }
            curPos+=2;
            
            int cfgNum = nodeCfgs.size();
            outWriteBytes(list, toBytes((short)cfgNum)); //5
            curPos+=2;
            
            //for each switch configuration
            int nodeIdx=0;
            for(NodeDevice nd1 : nodeCfgs){	
            	outWriteBytes(list, toBytes(nd1.getLocationId())); //location ID, 6
                curPos+=4;
                
                outWriteBytes(list, toBytes(0)); //file offset, 7
                nodeConfigOffsets.put(nodeIdx, new Integer[]{curPos, 0});
                curPos+=4;
                
                nodeIdx++;
            }
            
            // for each Node configuration
            nodeIdx=0;
            int nodeStartPosition=0;
            for(NodeDevice nd1 : nodeCfgs){	
            	Integer[] pos = nodeConfigOffsets.get(nodeIdx);
            	pos[1]=curPos;
            	nodeStartPosition=curPos;

            	bytes1 = stringToBytes(nd1.getNodeName(), 16); 
            	outWriteBytes(list, bytes1); // 8
            	curPos+=16;
            
            	outWriteBytes(list, toBytes(nd1.getLocationId()));//9
            	curPos+=4;
            	outWriteBytes(list, toBytes(nd1.getPortNo()));//10
            	curPos+=4;
            	outWriteBytes(list, (byte)nd1.getRoleOfNM());//11
            	curPos+=1;
            	outWriteBytes(list, (byte)nd1.getRoleOfNetworkLoad());//12
            	curPos+=1;
            	outWriteBytes(list, (byte)nd1.getRoleOfTimerSync());//13
            	curPos+=1;
            	outWriteBytes(list, toBytes(nd1.getRtcSendInterval()));//14
            	curPos+=4;
            	outWriteBytes(list, (byte)nd1.getRoleOfTimeSync());//15
            	curPos+=1;
            	outWriteBytes(list, toBytes(nd1.getOverallInterval()));//16
            	curPos+=4;
            	outWriteBytes(list, toBytes(nd1.getClusterInterval()));// 17
            	curPos+=2;
            	
            	List<NodeMessage> txMsgs = nd1.getTxMsgs();
            	outWriteBytes(list, toBytes(txMsgs.size()));// 18
            	curPos+=4;
            	
            	Integer[] txMsgStartPosition=new Integer[]{curPos, 0};
            	txMsgOffsets.put(nodeIdx, txMsgStartPosition);
            	outWriteBytes(list, toBytes(0)); // 19, send msg offset
            	curPos+=4;
            	
            	List<NodeMessage> rxMsgs = nd1.getRxMsgs();
            	outWriteBytes(list, toBytes(rxMsgs.size()));// 20
            	curPos+=4;
            	
            	Integer[] rxMsgStartPosition=new Integer[]{curPos, 0};
            	rxMsgOffsets.put(nodeIdx, rxMsgStartPosition);
            	outWriteBytes(list, toBytes(0));// 21 receive msg offset
            	curPos+=4;
            	
            	List<NodeVL> txVls = nd1.getTxVls();
            	outWriteBytes(list, toBytes(txVls.size()));// 22
            	curPos+=4;
            	
            	Integer[] txVLStartPosition=new Integer[]{curPos, 0};
            	txVLOffsets.put(nodeIdx, txVLStartPosition);
            	outWriteBytes(list, toBytes(0));// 23 send VL offset
            	curPos+=4;
            	
            	List<NodeVL> rxVls = nd1.getRxVls();
            	outWriteBytes(list, toBytes(rxVls.size()));// 24
            	curPos+=4;
            	
            	Integer[] rxVLStartPosition=new Integer[]{curPos, 0};
            	rxVLOffsets.put(nodeIdx, rxVLStartPosition);
            	outWriteBytes(list, toBytes(0));// 25 receive VL offset
            	curPos+=4;
            	
            	txMsgStartPosition[1]=curPos-nodeStartPosition;
            	for(NodeMessage msg : txMsgs){
            		outWriteBytes(list, toBytes(msg.getMessageID()));// 26
                	curPos+=4;
                	
                	bytes1 = stringToBytes(msg.getMessageName(), 16);
                	outWriteBytes(list, bytes1); //27
                	curPos+=16;
                	
                	outWriteBytes(list, toBytes((short)msg.getVl()));// 28
                	curPos+=2;
                	outWriteBytes(list, toBytes(msg.getMaxOfLen()));// 29
                	curPos+=4;
                	outWriteBytes(list, toBytes(msg.getType()));// 30
                	curPos+=2;
                	outWriteBytes(list, toBytes(msg.getSID()));// 31
                	curPos+=4;
                	outWriteBytes(list, toBytes(msg.getDID()));// 32
                	curPos+=4;
                	outWriteBytes(list, toBytes(msg.getUseOfMessage()));// 33
                	curPos+=2;
                	outWriteBytes(list, toBytes(msg.getSnmpID()));// 34
                	curPos+=2;
                	outWriteBytes(list, toBytes(msg.getLoadID()));// 35
                	curPos+=2;
            	}
            	
            	rxMsgStartPosition[1]=curPos-nodeStartPosition;
            	for(NodeMessage msg : rxMsgs){
            		outWriteBytes(list, toBytes(msg.getMessageID()));// 36
                	curPos+=4;
                	
                	bytes1 = stringToBytes(msg.getMessageName(), 16);//37 
                	outWriteBytes(list, bytes1); // 8
                	curPos+=16;
                	
                	outWriteBytes(list, toBytes((short)msg.getVl()));// 38
                	curPos+=2;
                	outWriteBytes(list, toBytes(msg.getMaxOfLen()));// 39
                	curPos+=4;
                	outWriteBytes(list, toBytes(msg.getType()));// 40
                	curPos+=2;
                	outWriteBytes(list, toBytes(msg.getSID()));// 41
                	curPos+=4;
                	outWriteBytes(list, toBytes(msg.getDID()));// 42
                	curPos+=4;
                	outWriteBytes(list, toBytes(msg.getUseOfMessage()));// 43
                	curPos+=2;
                	outWriteBytes(list, toBytes(msg.getSnmpID()));// 44
                	curPos+=2;
                	outWriteBytes(list, toBytes(msg.getLoadID()));// 45
                	curPos+=2;
            	}
            	
            	txVLStartPosition[1]=curPos-nodeStartPosition;
            	for(NodeVL vl : txVls){
            		outWriteBytes(list, toBytes((short)vl.getVLID()));// 46
                	curPos+=2;
                	outWriteBytes(list, toBytes(vl.getType()));// 47
                	curPos+=2;
                	outWriteBytes(list, toBytes((short)vl.getBag()));// 48
                	curPos+=2;
                	outWriteBytes(list, toBytes((short)vl.getJitter()));// 48
                	curPos+=2;
                	
                	outWriteBytes(list, toBytes(vl.getTtInterval()));// 49
                	curPos+=4;
                	outWriteBytes(list, toBytes(vl.getTtSentInterval()));// 50
                	curPos+=4;
                	outWriteBytes(list, toBytes(vl.getTtWindowOffset()));// 51
                	curPos+=4;
                	outWriteBytes(list, toBytes(vl.getTtWindowStart()));// 52
                	curPos+=4;
                	outWriteBytes(list, toBytes(vl.getTtWindowEnd()));// 53
                	curPos+=4;
                	
                	outWriteBytes(list, toBytes(vl.getNetworkType()));// 54
                	curPos+=2;
                	outWriteBytes(list, toBytes(vl.getRedudanceType()));// 55
                	curPos+=2;
                	outWriteBytes(list, toBytes(vl.getUseOfLink()));// 56
                	curPos+=2;
                	outWriteBytes(list, toBytes(vl.getRtcInterval()));// 57
                	curPos+=4;
                	
                	outWriteBytes(list, toBytes(vl.getSwitchPortNo()));// 58
                	curPos+=4;
            	}
            	
            	rxVLStartPosition[1]=curPos-nodeStartPosition;
            	for(NodeVL vl : rxVls){
            		outWriteBytes(list, toBytes((short)vl.getVLID()));// 59
                	curPos+=2;
                	outWriteBytes(list, toBytes(vl.getType()));// 60
                	curPos+=2;
                	outWriteBytes(list, toBytes(vl.getCompleteCheck()));// 61
                	curPos+=2;
                	
                	outWriteBytes(list, toBytes(vl.getRedudanceType()));// 62
                	curPos+=2;
                	outWriteBytes(list, toBytes(vl.getUseOfLink()));// 63
                	curPos+=2;
                	outWriteBytes(list, toBytes(vl.getSwitchPortNo()));// 64
                	curPos+=4;
            	}
            	
            	nodeIdx++;
            }
            
            // set file length
            setBytesInList(list, 32, toBytes(curPos+4)); //add checksum
            
            for(Map.Entry<Integer, Integer[]> entry: nodeConfigOffsets.entrySet()){
            	Integer[] nodePosOffset = entry.getValue();
            	setBytesInList(list, nodePosOffset[0], toBytes(nodePosOffset[1]));
            }
            for(Map.Entry<Integer, Integer[]> entry: txMsgOffsets.entrySet()){
            	Integer[] nodePosOffset = entry.getValue();
            	setBytesInList(list, nodePosOffset[0], toBytes(nodePosOffset[1]));
            }
            for(Map.Entry<Integer, Integer[]> entry: rxMsgOffsets.entrySet()){
            	Integer[] nodePosOffset = entry.getValue();
            	setBytesInList(list, nodePosOffset[0], toBytes(nodePosOffset[1]));
            }
            for(Map.Entry<Integer, Integer[]> entry: txVLOffsets.entrySet()){
            	Integer[] nodePosOffset = entry.getValue();
            	setBytesInList(list, nodePosOffset[0], toBytes(nodePosOffset[1]));
            }
            for(Map.Entry<Integer, Integer[]> entry: rxVLOffsets.entrySet()){
            	Integer[] nodePosOffset = entry.getValue();
            	setBytesInList(list, nodePosOffset[0], toBytes(nodePosOffset[1]));
            }
            
            int checksum=genChecksum(list);
        	outWriteBytes(list, toBytes(checksum)); // 65, checksum for whole bin file
        	curPos+=4;

            for(Byte b : list){
            	out.write(b);
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
			byte[] bs=new byte[16];
        	for(int i=0; i<16; i++){
        		bs[i]=readByte(in);
        	}
        	String version=bytesToString(bs);
        	mon.setVersion(version);
        	if(version==null || version.equals("")){
        		mon.setVersion(ConfigContext.version);
        	}
        	System.out.println("");
			System.out.println("monitor version:"+version);
        	
			for(int i=0; i<16; i++){
        		bs[i]=readByte(in);
        	}
        	String date=bytesToString(bs);
        	mon.setDate(date);
        	if(date==null || date.equals("")){
        		mon.setDate(ConfigContext.date);
        	}
			System.out.println("date:"+date);
        	
			int data=readInt(in);
			System.out.println("file length: "+data+" "+fileName);
			
			int fileNo=readInt(in);
        	mon.setFileNo(fileNo);
        	System.out.println("file id:"+fileNo);
        	
        	short tableNumber=readShort(in);
        	System.out.println("monitor table total number:"+tableNumber);
        	
        	List<Integer> planNos=new ArrayList<>();
        	for(int i=0; i<tableNumber; i++){
        		data=readInt(in);
        		planNos.add(data);
        		data=readInt(in);
        	}
        	
        	List<SwitchMonitorPort> monPorts = mon.getMonitorPorts();
        	Iterator<Integer> it=planNos.iterator();
        	for(int i=0; i<tableNumber; i++){
        		Integer planNo = it.next();
        		
        		int locationId=readShort2(in);
        		short portNo=readShort(in);  //7
        		SwitchMonitorPort port=new SwitchMonitorPort(mon.getSwitchName(), planNo, portNo);
        		port.setLocationId(locationId);
        		monPorts.add(port);
        		
        		System.out.println("monitor "+i+" port No "+portNo);
        		
        		short status=readShort(in); //8
        		port.setPortEnableMonitor(status);
        		
        		short mode=readShort(in); //9
        		port.setPortMonitorMode(mode);
        		
        		short inputPortNum=readShort(in); //10
        		System.out.println("monitor "+i+" inputPortNum "+inputPortNum);
        		
        		int inputPort=readInt(in); //11
        		List<Integer> inputPorts = ConfigUtils.bitsetIntToList(inputPort, 0);
        		port.setPortInputPortList(inputPorts);
        		System.out.println("monitor "+i+" input ports "+inputPorts);
        		
        		short outputPortNum=readShort(in); //12
        		System.out.println("monitor "+i+" outputPortNum "+outputPortNum);
        		
        		int outputPort=readInt(in); //13
        		List<Integer> outputPorts = ConfigUtils.bitsetIntToList(outputPort, 0);
        		port.setPortOutputPortList(outputPorts);
        		System.out.println("monitor "+i+" output ports "+outputPorts);
        		
        		int vlNum=readShort2(in); //14
        		List<Integer> vls=port.getPortVLList();
        		for(int n=0; n<vlNum; n++){
        			int vl=readShort2(in); //15, VL, 2*N
        			vls.add(vl);
        		}
        		System.out.println("monitor "+i+" VL "+vls);
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
		List<Byte> list=new ArrayList<>();
		
		try {
			String ver=mon.getVersion();
			if(ver==null || "".equals(ver)){
				ver=ConfigContext.version;
			}
			byte[] bytes1 = stringToBytes(ver, 16);
            outWriteBytes(list, bytes1); //1
            curPos+=16;
			
            String date=mon.getDate();
			if(date==null || "".equals(date)){
				date=ConfigContext.date;
			}
			bytes1 = stringToBytes(date, 16);
            outWriteBytes(list, bytes1); //2
            curPos+=16;
            
            outWriteBytes(list, toBytes(0)); //3, length of file, will set in the end 
            curPos+=4;
            
            if(mon.getFileNo()==0){ //4
            	outWriteBytes(list, toBytes((int)ConfigContext.fileNo));
            }else{
            	outWriteBytes(list, toBytes(mon.getFileNo()));
            }
            curPos+=4;
            
            List<SwitchMonitorPort> monPorts = mon.getMonitorPorts();
            outWriteBytes(list, toBytes((short)monPorts.size())); //5
            curPos+=2;
            
            Map<Integer, Integer[]> offsets=new HashMap<>();//key is the index of monitor item
            int i=0;
            for(SwitchMonitorPort monPort : monPorts){
            	outWriteBytes(list, toBytes(monPort.getConfigTableId()));
                curPos+=4;
                
                outWriteBytes(list, toBytes(0)); //7, will set in the end
                offsets.put(i, new Integer[]{curPos, 0});
                curPos+=4;
                
                i++;
            }
            
            i=0;
            for(SwitchMonitorPort monPort : monPorts){
            	Integer[] locs = offsets.get(i);
            	locs[1]=curPos; //start position for current monitor item
            	
            	outWriteBytes(list, toBytes((short)monPort.getLocationId()));//6
                curPos+=2;
                outWriteBytes(list, toBytes((short)monPort.getPortId()));//7
                curPos+=2;
                outWriteBytes(list, toBytes((short)monPort.getPortEnableMonitor()));//8
                curPos+=2;
                outWriteBytes(list, toBytes((short)monPort.getPortMonitorMode()));//9
                curPos+=2;
                
                List<Integer> inputPorts = monPort.getPortInputPortList();
                outWriteBytes(list, toBytes((short)inputPorts.size()));//10
                curPos+=2;
                
                int[] inputPortsBitset=ConfigUtils.listToIntBitSet(inputPorts);
                outWriteBytes(list, toBytes(inputPortsBitset[1]));//11
                curPos+=4;
                
                List<Integer> outputPorts = monPort.getPortOutputPortList();
                outWriteBytes(list, toBytes((short)outputPorts.size()));//12
                curPos+=2;
                
                int[] outputPortsBitset=ConfigUtils.listToIntBitSet(outputPorts);
                outWriteBytes(list, toBytes(outputPortsBitset[1]));//13
                curPos+=4;
                
                List<Integer> vls = monPort.getPortVLList();
                outWriteBytes(list, toBytes((short)vls.size())); //14
                curPos+=2;
                
                Collections.sort(vls);
                for(Integer vlid : vls){
                	outWriteBytes(list, toBytes(vlid.shortValue()));//15, 2*N
                	curPos+=2;
                }
                
                i++;
            }
            
            // TODO no checksum?
            
            setBytesInList(list, 32, toBytes(curPos));
            
            for(Map.Entry<Integer, Integer[]> entry: offsets.entrySet()){
            	Integer[] offset = entry.getValue();
            	setBytesInList(list, offset[0], toBytes(offset[1]));
            }
            
            for(Byte b : list){
            	out.write(b);
            }
            
            System.out.println("write "+curPos+" to "+fileName);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			out.close();
		}
	}
	
	/** [0] is high code, [1] is low code; port no is from 0-16, 16 is NMU */

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
    	if(nameLen==-1){
    		nameLen=names.length;
    	}
    	
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
	        }else{
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
	
	public static byte[] toBytes(short i) {
		byte[] result = new byte[2];
		result[0] = (byte) ((i >> 8) & 0xFF);
		result[1] = (byte) (i & 0xFF);
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
	
	public static short toShort(byte[] b) {
		int value = 0;
	    for (int i = 0; i < 2; i++) {
	        int shift = (2 - 1 - i) * 8;
	        value += (b[i] & 0x00FF) << shift;
	    }
	    return (short)value;
	}
	
	public static int toShort2(byte[] b) {
		int value = 0;
	    for (int i = 0; i < 2; i++) {
	        int shift = (2 - 1 - i) * 8;
	        value += (b[i] & 0x00FF) << shift;
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
	
	private static short readShort(FileInputStream in){
		byte[] bs=new byte[2];
		try{
			in.read(bs, 0, 2);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return toShort(bs);
	}
	
	private static int readShort2(FileInputStream in){
		byte[] bs=new byte[2];
		try{
			in.read(bs, 0, 2);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return toShort2(bs);
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

	private static void outWriteBytes(List<Byte> list, byte[] bs){
		if(bs!=null){
			for(byte b : bs){
				list.add(b);
			}
		}
	}
	
	private static void outWriteBytes(List<Byte> list, byte b){
		list.add(b);
	}
	
	/*** update list in place */
	private static void setBytesInList(List<Byte> list, int pos, byte[] bs){
		if(bs==null){
			return;
		}
		if(list==null){
			return;
		}
		
		for(int i=0; i<bs.length; i++){
			list.set(pos+i, bs[i]);
		}
	}
	
	private static int genChecksum(List<Byte> bs){
		int sum=0;
		for(Byte b : bs){
			sum+=b;
		}
		return sum;
	}

}
