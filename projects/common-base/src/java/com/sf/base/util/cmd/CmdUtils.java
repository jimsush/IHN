/*
 * $Id: CmdUtils.java, 2015-11-12 ����10:42:13 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

import com.sf.base.exception.ExceptionUtils;
import com.sf.base.exception.SfException;
import com.sf.base.util.cmd.impl.RunCmdService;
import com.sf.base.util.format.MacAddrFormatter;
import com.sf.base.util.sys.SysUtils;

/**
 * <p>
 * Title: CmdUtils
 * </p>
 * <p>
 * Description: �����й�����
 * </p>
 * 
 * @author sufeng
 * created 2015-11-12 ����10:42:13
 * modified [who date description]
 * check [who date description]
 */
public class CmdUtils {

    /**
     * ִ��һ���ⲿ��bat
     * @param cmd ��ִ�е����������
     * @return
     * @throws Exception
     */
    public static Process exeCmd(String cmd) throws Exception{
        Runtime runtime = Runtime.getRuntime();
        return runtime.exec(cmd);
    }
    
    /**
     * �ڵ�ǰ�߳��������ķ�ʽִ��һ������
     * @param cmd ��ִ�е����������
     * @param listener ��ִ�й����е�����������ش����listener������Ϊnull
     */
    public static void exeCmdInCurrentThread(String cmd,CmdStreamListener listener){
        RunCmdService runCmdService=new RunCmdService();
        runCmdService.setStartCmdLine(cmd);
        runCmdService.setStartupMode(RunCmdService.INSIDE_THREAD);
        runCmdService.setCmdStreamListener(listener);
        runCmdService.startService();
    }

    /**
     * ����һ���߳����첽��ʽִ��һ������
     * @param cmd ��ִ�е����������
     * @param listener ��ִ�й����е�����������ش����listener������Ϊnull
     */
    public static void exeCmdInNewThread(String cmd,CmdStreamListener listener){
        RunCmdService runCmdService=new RunCmdService();
        runCmdService.setStartCmdLine(cmd);
        runCmdService.setStartupMode(RunCmdService.OUTSIDE_THREAD);
        runCmdService.setCmdStreamListener(listener);
        runCmdService.startService();
    }
    
    /** 
     * ���˿��Ƿ��
     * @param protocol TCP��UDP
     * @param ports ����ͬʱ������˿ں�
     */
    public static Boolean[] checkPorts(String protocol,String[] ports){
        int len=ports.length;
        Boolean[] usedFlag=new Boolean[len];
        for(int i=0;i<len;i++)
            usedFlag[i]=Boolean.FALSE;
        
        try{
            Process process = exeCmd("cmd /C netstat -an");
            //System.out.println("netstat exec finished,will start to parse");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while((line=br.readLine())!=null){
                //System.out.println("line..."+line);
                String tmp=line.trim();
                String[] tokens = tmp.split(" ");
                if(tokens != null && tokens.length > 2) {
                    String curProtocol=tokens[0];//TCP/UDP��һ�����ϵͳ������ΪTCP��Ȼ����UDP
                    
                    // ������ҵ���TCP���񣬶���ʱȴ��UDP��˵��TCP�Ѿ����������������˳��ˣ���������ȴ�line==null����ʱ�����
                    if("TCP".equals(protocol)){
                        if("UDP".equals(curProtocol))
                            break;
                    }
    
                    if(protocol.equals(curProtocol)){
                        String localAddr = tokens[4].trim();
                        String port = localAddr.substring(localAddr.indexOf(":") + 1);
                        for(int i=0;i<len;i++){
                            if(ports[i].equals(port)){
                                String status=tokens[tokens.length-1];
                                // tcp��Ҫ�������fin_wait2,time_wait
                                if("TCP".equals(protocol) && status.length()>0){
                                    if("LISTENING".equals(status) || "ESTABLISHED".equals(status)){ 
                                        if(localAddr.startsWith("[::]:")){
                                            break;
                                        }else{
                                            usedFlag[i]=Boolean.TRUE;
                                            break;
                                        }
                                    }
                                }else{
                                    // UDP����TCP���������
                                    usedFlag[i]=Boolean.TRUE;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
            br.close();
            isr.close();
            is.close();
        }catch(Exception ex){
            //System.out.println("checkPorts failed,ex="+ex.getMessage());
            ex.printStackTrace();
            return usedFlag;
        }
        
        //System.out.println("checkPorts finished");
        return usedFlag;
    }
    
    /**
     * ��ȡ������IP��ַ
     * @return
     */
    public static String[] getLocalIps(){
        // �жϲ���ϵͳ
        String osName = SysUtils.getPlatForm();
        if (osName == null)
            return null;

        if (SysUtils.OS_WIN.equals(osName)) {
            return getLocalIpsInWindows();
        } else if (SysUtils.OS_LINUX.equals(osName)) {
            return getLocalIpsInLinux();
        } 
        
        return null;
    }
    
    /**
     * ����(Windows)��ip
     * 
     * @return
     */
    private static String[] getLocalIpsInWindows() {
        List<String> ipList = new Vector<String>();
        try {
            Process pp = Runtime.getRuntime().exec("cmd /c ipconfig");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            BufferedReader bd = new BufferedReader(ir);
            String line = null;
            int pos = -1;
            String curIp = null;
            while ((line = bd.readLine()) != null) {
                if (line.equals(""))
                    continue;
                pos = line.indexOf("IP Address");
                if (pos == -1)
                    continue;
                curIp = line.substring(pos + 10);
                pos = curIp.indexOf(": ");
                curIp = curIp.substring(pos + 2);
                ipList.add(curIp);
            }
            bd.close();
            ir.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (ipList.size() == 0)
            return null;

        String[] ips = new String[ipList.size()];
        int i = 0;
        for (String ip : ipList) {
            ips[i] = ip;
            i++;
        }
        return ips;
    }

    /**
     * ��ȡlinux�»�����ip
     * 
     * @return
     */
    private static String[] getLocalIpsInLinux() {
        List<String> ipList = new Vector<String>();
        try {
            Process pp = Runtime.getRuntime().exec("sh ifconfig | grep addr");// sh
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            BufferedReader bd = new BufferedReader(ir);
            String line = null;
            int pos = -1;
            String curIp = null;
            while ((line = bd.readLine()) != null) {
                if (line.equals(""))
                    continue;
                pos = line.indexOf("inet addr:");
                if (pos == -1)
                    continue;
                curIp = line.substring(pos + 10);
                pos = curIp.indexOf(" ");
                curIp = curIp.substring(0, pos);
                ipList.add(curIp);
            }
            bd.close();
            ir.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (ipList.size() == 0)
            return null;

        String[] ips = new String[ipList.size()];
        int i = 0;
        for (String ip : ipList) {
            ips[i] = ip;
            i++;
        }
        return ips;
    }

    /**
     * ��ǰ��������������mac��ַ
     * <p>
     * ֧��windows,linux
     * 
     * @return ����ÿ��Ԫ�ظ�ʽΪ"00-15-C5-B6-81-DA",���û���򷵻�null
     */
    public static String[] getLocalMacs() {
        // �жϲ���ϵͳ
        String osName = SysUtils.getPlatForm();
        if (osName == null)
            return null;

        String cmd = null;
        if (SysUtils.OS_WIN.equals(osName)) {
            cmd = "cmd /c ipconfig /all";
        } else if (SysUtils.OS_LINUX.equals(osName)) {
            cmd = "sh ifconfig | grep addr";
        } else {
            return null;
        }

        // ��ȡmac
        List<String> macList = new Vector<String>();
        try {
            Process pp = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            BufferedReader bd = new BufferedReader(ir);
            String line = null;
            String lastMac = null;
            int pos = -1;
            while ((line = bd.readLine()) != null) {
                if (SysUtils.OS_WIN.equals(osName)) {
                    if (line.indexOf("Physical Address") != -1) {
                        pos = line.indexOf(": ");
                        if (pos >= 0) {
                            lastMac = line.substring(pos + 2);
                            lastMac = MacAddrFormatter.formatMacAddr(lastMac);
                            macList.add(lastMac);
                        }
                    }
                } else if (SysUtils.OS_LINUX.equals(osName)) {
                    // linux�Ĵ���
                    pos = line.indexOf("HWaddr ");
                    if (pos != -1) {
                        lastMac = line.substring(pos + 7);
                        // ��ʽת��
                        lastMac = MacAddrFormatter.formatMacAddr(lastMac);
                        macList.add(lastMac);
                    }
                }
            }
            bd.close();
            ir.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (macList.size() == 0)
            return null;

        String[] macs = new String[macList.size()];
        int i = 0;
        for (String macAddr : macList) {
            macs[i] = macAddr;
            i++;
        }
        return macs;
    }
    
    /**
     * ��ȡ������������
     * @return ����������ȡʧ�ܻ����쳣
     */
    public static String getLocalHostName(){
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostName();
        } catch (UnknownHostException e) {
            String info=ExceptionUtils.getCommonExceptionInfo(e);
            System.err.println("get this machine hostname failed,"+info);
            throw new SfException(SfException.INTERNAL_ERROR,e,info);
        }
    }
    
}
