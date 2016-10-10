package dima.config.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import dima.config.common.models.vo.ADCItem;
import dima.config.common.models.vo.ConfigMessageItem;
import dima.config.common.models.vo.ReceiveInfo;
import dima.config.common.models.vo.SendInfo;
import dima.config.common.models.vo.TopoDeviceVo;
import dima.config.common.models.vo.TopoLinkVo;

public class XMLHandler {
	
	public static void main(String[] args) throws Exception{
		XMLHandler handler=new XMLHandler();
		handler.readAdc("ADC.xml");
		handler.readConfigCtrl("config_ctrl.xml");
		handler.readTopoCtrl("topo_ctrl.xml");
	}
	
	public List<ADCItem> readAdc(String fileName) throws Exception{
		SAXReader reader = new SAXReader();
		Document   document = reader.read(new File(fileName)); 
		Element root = document.getRootElement();
		List<ADCItem> result=new ArrayList<>();
		parseAdcElement(root, result);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private void parseAdcElement(Element element, List<ADCItem> result){
		if("AppCommCfg".equals(element.getName())){
			ADCItem adcItem=new ADCItem(element.attribute("AppName").getStringValue());
			result.add(adcItem);
			
			Element sendTable=element.element("SendTable");
			List<Element> sendInfos = sendTable.elements();
			for(Element sendInfo : sendInfos){
				SendInfo si=new SendInfo();
				adcItem.addSendInfo(si);
				
				si.setMessageID(Integer.valueOf(sendInfo.attribute("MessageID").getStringValue()));
				si.setMaxMessageLength(Integer.valueOf(sendInfo.attribute("MaxMessageLength").getStringValue()));
				si.setNetDevice(sendInfo.attribute("NetDevice").getStringValue());	
			}
			Element receivTable=element.element("ReceivTable");
			List<Element> receiveInfos = receivTable.elements();
			for(Element receiveInfo : receiveInfos){
				ReceiveInfo ri=new ReceiveInfo();
				adcItem.addReceiveInfo(ri);
				
				ri.setMessageID(Integer.valueOf(receiveInfo.attribute("MessageID").getStringValue()));
				ri.setMaxMessageLength(Integer.valueOf(receiveInfo.attribute("MaxMessageLength").getStringValue()));
				ri.setNetDevice(receiveInfo.attribute("NetDevice").getStringValue());
				ri.setMessageType(receiveInfo.attribute("MessageType").getStringValue());
				ri.setQueueDepth(Integer.valueOf(receiveInfo.attribute("QueueDepth").getStringValue()));
			}
			
		}else{
			List<Element> children = element.elements();
			if(children==null || children.size()==0){
				return;
			}
			for(Element child : children){
				parseAdcElement(child, result);
			}
		}
	}
	
	public List<ConfigMessageItem> readConfigCtrl(String fileName) throws Exception{
		SAXReader reader = new SAXReader();
		Document   document = reader.read(new File(fileName)); 
		Element root = document.getRootElement();
		
		List<ConfigMessageItem> result=new ArrayList<>();
		parseConfigCtrlElement(root, result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private void parseConfigCtrlElement(Element element, List<ConfigMessageItem> result){
		if("message".equals(element.getName())){
			ConfigMessageItem item=new ConfigMessageItem();
			result.add(item);
			
			item.setMessageName(element.attribute("name").getStringValue());
			item.setType(element.attribute("type").getStringValue());
			item.setMsgId(Integer.valueOf(element.attribute("msgId").getStringValue()));
			item.setRefSender(element.attribute("refSender").getStringValue());
			item.setRefReceiver(element.attribute("refReceiver").getStringValue());
			item.setPeroid(element.attribute("period").getStringValue());
			item.setMaxLength(Integer.valueOf(element.attribute("maxLength").getStringValue()));
			item.setMsgUse(Integer.valueOf(element.attribute("msgUse").getStringValue()));
		} else if("global_paras".equals(element.getName())){
			System.out.println(element.attribute("redunLevel"));
			System.out.println(element.attribute("intePer"));
			System.out.println(element.attribute("cyclePer"));
			System.out.println(element.attribute("nmg_NC"));
			System.out.println(element.attribute("nmg_BNC"));
			System.out.println(element.attribute("sync_SM"));
			System.out.println(element.attribute("sync_CM"));
			System.out.println(element.attribute("sync_Per"));
			System.out.println(element.attribute("loader_Server"));
		} else {
			List<Element> children = element.elements();
			if(children==null || children.size()==0){
				return;
			}
			for(Element child : children){
				parseConfigCtrlElement(child, result);
			}
		}
	}

	public Object[] readTopoCtrl(String fileName) throws Exception{
		SAXReader reader = new SAXReader();
		Document   document = reader.read(new File(fileName)); 
		Element root = document.getRootElement();
		List<TopoDeviceVo> devices=new ArrayList<>();
		List<TopoLinkVo> links=new ArrayList<>();
		parseTopoCtrlElement(root, devices, links);
		return new Object[]{devices, links};
	}

	@SuppressWarnings("unchecked")
	private void parseTopoCtrlElement(Element element, List<TopoDeviceVo> devices, List<TopoLinkVo> links){
		if("device".equals(element.getName())){
			TopoDeviceVo dev=new TopoDeviceVo();
			devices.add(dev);
			dev.setName(element.attribute("name").getStringValue());
			dev.setType(element.attribute("type").getStringValue());
			if(element.attribute("domainId")!=null){
				dev.setDomainId(Integer.valueOf(element.attribute("domainId").getStringValue()));
			}
			if(element.attribute("rtcSynEn")!=null){
				dev.setRtcSynEn(Integer.valueOf(element.attribute("rtcSynEn").getStringValue()));
			}
			List<Object[]> newPorts = dev.getPorts();
			
			List<Element> ports=element.elements();
			if(ports!=null){
				for(Element port : ports){
					Object[] p=new Object[]{
							Integer.valueOf(port.attribute("id").getStringValue()),
							port.attribute("name").getStringValue()};
					newPorts.add(p);
				}
			}
		}else if("link".equals(element.getName())){
			TopoLinkVo lk=new TopoLinkVo();
			links.add(lk);
			lk.setName(element.attribute("name").getStringValue());
			lk.setType(element.attribute("type").getStringValue());
			lk.setRefSender(element.attribute("refSender").getStringValue());
			lk.setRefReceiver(element.attribute("refReceiver").getStringValue());
		}else{
			List<Element> children = element.elements();
			if(children==null || children.size()==0){
				return;
			}
			for(Element child : children){
				parseTopoCtrlElement(child, devices, links);
			}
		}
	}
}
