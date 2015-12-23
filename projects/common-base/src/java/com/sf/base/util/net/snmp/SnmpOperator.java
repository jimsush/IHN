/*
 * $Id: SnmpOperator.java, 2015-11-12 上午11:40:47 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.net.snmp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.StopWatch;

import com.adventnet.snmp.beans.DataException;
import com.adventnet.snmp.beans.ErrorMessages;
import com.adventnet.snmp.beans.SnmpTarget;
import com.adventnet.snmp.snmp2.SnmpVar;
import com.adventnet.snmp.snmp2.SnmpVarBind;
import com.sf.base.util.net.snmp.model.SnmpProtocalLoginParam;
import com.sf.base.util.net.snmp.model.SfSnmpException;

/**
 * <p>
 * Title: SnmpOperator
 * </p>
 * <p>
 * Description: SNMP操作工具类
 * </p>
 * 
 * @author sufeng
 * created 2015-11-12 上午11:40:47
 * modified [who date description]
 * check [who date description]
 */
public class SnmpOperator {

    public static final int MAX_OID_NUM = 50;

    private SnmpProtocalLoginParam loginParams;

    public SnmpOperator(SnmpProtocalLoginParam loginParams) {
        this.loginParams = loginParams;
    }

    private SnmpTarget getSnmpTargetFromPool() {
        SnmpTarget snmpSession = new SnmpTarget();
        snmpSession.setTimeout(loginParams.getTimeout());
        snmpSession.setRetries(loginParams.getReties());
        snmpSession.setSnmpVersion(loginParams.getSnmpVer());
        snmpSession.setTargetHost(loginParams.getIp());
        snmpSession.setCommunity(loginParams.getReadCommunity());
        snmpSession.setWriteCommunity(loginParams.getWriteCommunity());
        snmpSession.setTargetPort(loginParams.getSnmpPort());
        return snmpSession;
    }

    /**
     * 取得mib表中的多个结点的值，必须有index
     * @param oids
     * @return
     */
    public SnmpVarBind[] getMultiOidValues(String... oids) {
        SnmpVarBind[] resu = new SnmpVarBind[oids.length];
        if (oids.length <= 0)
            return resu;
        if (oids.length < MAX_OID_NUM)
            resu = getMultiOidValue(oids);
        else {
            String[][] doubleOids = new String[(oids.length + MAX_OID_NUM - 1) / MAX_OID_NUM][];
            for (int i = 0; i < doubleOids.length - 1; i++) {
                doubleOids[i] = new String[MAX_OID_NUM];
                for (int j = 0; j < MAX_OID_NUM; j++)
                    doubleOids[i][j] = oids[i * MAX_OID_NUM + j];
            }
            doubleOids[doubleOids.length - 1] = new String[oids.length - (doubleOids.length - 1)
                    * MAX_OID_NUM];
            for (int i = 0; i < doubleOids[doubleOids.length - 1].length; i++)
                doubleOids[doubleOids.length - 1][i] = oids[(doubleOids.length - 1) * MAX_OID_NUM + i];

            List<SnmpVarBind> resuList = new ArrayList<SnmpVarBind>();
            for (int i = 0; i < doubleOids.length; i++) {
                SnmpVarBind[] tempResu = getMultiOidValue(doubleOids[i]);
                for (int j = 0; j < tempResu.length; j++)
                    resuList.add(tempResu[j]);
            }
            resu = resuList.toArray(new SnmpVarBind[0]);
        }
        return resu;
    }

    /**
     * 取得mib表中的多个结点的值，必须有index
     * @param oids
     * @return
     */
    private SnmpVarBind[] getMultiOidValue(String... oids) {
        SnmpTarget snmpSession = getSnmpTargetFromPool();
        try {
            SnmpVarBind[] resu = new SnmpVarBind[oids.length];
            if (oids.length <= 0)
                return resu;
            snmpSession.setObjectIDList(oids);
            SnmpVarBind[] binds = snmpSession.snmpGetVariableBindings();
            if (binds == null)
                throw new SfSnmpException(SfSnmpException.SBI_GET_ERROR, snmpSession.getErrorString()+","+SnmpOidUtil.getOidDisplayName(oids));
            if (snmpSession.getExceptionCode() == ErrorMessages.NOSUCHINSTANCEEXP)
                throw new SfSnmpException(SfSnmpException.SBI_NOSUCHINSTANCE, snmpSession.getErrorString()+","+SnmpOidUtil.getOidDisplayName(oids));
            for (int i = 0; i < binds.length; i++) {
                resu[i] = binds[i];
            }
            return resu;
        } finally {
            snmpSession = null;
        }
    }
    
    /**
     * 获取同一索引对应的多个节点
     * @param index
     * @param columnOids
     * @return
     */
    public SnmpVarBind[] getMultiOids(String index, String... columnOids) {
        int columnSize = columnOids.length;
        String[] oids = new String[columnSize];
        for (int i = 0; i < columnSize; i++) {
            oids[i] = columnOids[i] + index;
        }
        return getMultiOidValues(oids);
    }


    /**
     * 取得单个mib结点的内容，需要有index
     * @param oid
     * @param timeoutInSecond
     * @return
     */
    public SnmpVarBind getSingleValue(String oid,int... timeoutInSecond) {
        SnmpTarget snmpSession = getSnmpTargetFromPool();
        try {
            if(timeoutInSecond.length>0)
                snmpSession.setTimeout(timeoutInSecond[0]);
            SnmpVarBind var = null;
            snmpSession.setObjectID(oid);
            var = snmpSession.snmpGetVariableBinding();
            if (var == null)
                throw new SfSnmpException(SfSnmpException.SBI_GET_ERROR, snmpSession.getErrorString()+","+oid);
            return var;
        } finally {
            snmpSession = null;
        }
    }
    

    /**
     * 取得单个MIB节点，自动补索引".0"
     * @param oid
     * @param timeoutInSecond
     * @return
     */
    public SnmpVarBind getSingleValue_NoIndex(String oid,int... timeoutInSecond){
        String totalOid=oid+".0";
        return getSingleValue(totalOid,timeoutInSecond);
    }


    /**
     * 设置单个mib结点的内容
     * @param oid
     * @param var
     * @param timeoutInSecond
     */
    public void setSingleValue(String oid, SnmpVar var,int... timeoutInSecond) {
        String[] oids = new String[]{oid};
        SnmpVar[] vars = new SnmpVar[]{var};
        setValues(oids, vars,timeoutInSecond);
    }
    
    /**
     * 设置单个MIB节点，自动补索引".0"
     * @param oid
     * @param var
     * @param timeoutInSecond
     */
    public void setSingleValue_NoIndex(String oid, SnmpVar var,int... timeoutInSecond) {
        String totalOid=oid+".0";
        setSingleValue(totalOid, var,timeoutInSecond);
    }    


    /**
     * 批量设置多个mib结点
     * @param oids
     * @param vars
     * @param timeoutInSecond
     */
    public void setValues(String[] oids, SnmpVar[] vars,int... timeoutInSecond) {
        SnmpTarget snmpSession = getSnmpTargetFromPool();
        try {
            if (oids.length > 0 && vars.length > 0) {
                if(timeoutInSecond.length>0)
                    snmpSession.setTimeout(timeoutInSecond[0]);
                snmpSession.setObjectIDList(oids);
                SnmpVarBind[] binds = snmpSession.snmpSetVariableList(vars);
                if (binds == null)
                    throw new SfSnmpException(SfSnmpException.SBI_SET_ERROR, snmpSession.getErrorString()+","+SnmpOidUtil.getOidDisplayName(oids));
            }
        } catch (DataException e) {
            throw new SfSnmpException(SfSnmpException.SBI_SET_ERROR, e, snmpSession.getErrorString()+","+SnmpOidUtil.getOidDisplayName(oids));
        } finally {
            snmpSession = null;
        }
    }

    /**
     * 批量设置多个mib结点内容，包含重试参数
     * @param oids
     * @param vars
     * @param retryTimes
     */
    public void setValuesWithRetry(String[] oids, SnmpVar[] vars, int retryTimes) {
        SnmpTarget snmpSession = getSnmpTargetFromPool();
        try {
            if (oids.length > 0 && vars.length > 0) {
                snmpSession.setRetries(retryTimes);
                snmpSession.setObjectIDList(oids);
                SnmpVarBind[] binds = snmpSession.snmpSetVariableList(vars);
                if (binds == null)
                    throw new SfSnmpException(SfSnmpException.SBI_SET_ERROR, snmpSession.getErrorString()+","+SnmpOidUtil.getOidDisplayName(oids));
            }
        } catch (DataException e) {
            throw new SfSnmpException(SfSnmpException.SBI_SET_ERROR, e, snmpSession.getErrorString()+","+SnmpOidUtil.getOidDisplayName(oids));
        } finally {
            snmpSession = null;
        }
    }

    /**
     * 设置多个索引相同的节点内容
     * @param oids
     * @param vars
     * @param index
     */
    public void setValues(String[] oids, SnmpVar[] vars, String index) {
        int length = oids.length;//此处需要注意，使用新的String[]，否则可能影响调用
        String[] totalOids = new String[length];
        for (int i = 0; i < length; i++) {
            totalOids[i] = oids[i] + index;
        }
        setValues(totalOids, vars);
    }

    /**
     * 批量下发多个节点，包含是否逐条下发
     * 主要用于16B0的语音配置下发极慢，而且不支持批量下发，因此每个值下发前先进行比较，如果不同再下发到设备
     * @param oids
     * @param vars
     * @param stepSet
     */
    public void setValuesByStep(String[] oids, SnmpVar[] vars, boolean stepSet) {
        if (stepSet) {
            SnmpVarBind[] tmpvars = this.getMultiOidValues(oids);
            for (int i = 0; i < oids.length; i++) {
                if (oids[i] != null && oids[i].length() > 1) {
                    if (vars[i].equals(tmpvars[i].getVariable()))
                        continue;
                    setSingleValue(oids[i], vars[i]);
                }
            }
        } else {
            setValues(oids, vars);
        }
    }

    /**
     * 批量设置多个节点，包含比较，和设备不同的节点才下发
     * @param oids
     * @param vars
     * @param compair
     */
    public void setValuesWithCompair(String[] oids, SnmpVar[] vars, boolean compair) {
        if (compair) {
            SnmpVarBind[] tmpvars = this.getMultiOidValues(oids);
            List<String> oidList = new ArrayList<String>();
            List<SnmpVar> varList = new ArrayList<SnmpVar>();
            for (int i = 0; i < oids.length; i++) {
                if (oids[i] != null && oids[i].length() > 1) {
                    if (vars[i].equals(tmpvars[i].getVariable()))
                        continue;
                    oidList.add(oids[i]);
                    varList.add(vars[i]);
                }
            }
            if (CollectionUtils.isNotEmpty(oidList)) {
                setValues(oidList.toArray(new String[0]), varList.toArray(new SnmpVar[0]));
            }
        } else {
            setValues(oids, vars);
        }
    }


    /**
     * 采用BULK方式取全表
     * @param entryOid
     * @param timeouts
     * @return
     */
    public SnmpVarBind[][] getAllTableValues_Bulk(String entryOid,int... timeouts) {
        if (false)
            return getAllTableValues_NotBulk(entryOid);
        StopWatch timeWatch = new StopWatch();
        timeWatch.start();
        SnmpTarget snmpSession = getSnmpTargetFromPool();
        if(timeouts.length>0){
            snmpSession.setTimeout(timeouts[0]);
        }
        SnmpVarBind[][] resu = null;
        try {
            String firstColumnOid = null;
            String nextOid = entryOid;
            boolean lineNumberGot = false;
            boolean firstColumnOidGot = false;
            int lineNumber = 0;
            List<SnmpVarBind> resList = new ArrayList<SnmpVarBind>();
            boolean hasRet = false;
            boolean meetEnd = false;

            while (true) {
                snmpSession.setObjectID(nextOid);
                SnmpVarBind[][] binds = snmpSession.snmpGetBulkVariableBindings();
                if (binds == null)
                    throw new SfSnmpException(SfSnmpException.SBI_GET_ERROR, snmpSession.getErrorString()+",table:"+entryOid);

OUT:            for (int i = 0; i < binds.length; i++) {
                    for (int j = 0; j < binds[i].length; j++) {
                        hasRet = true;
                        nextOid = binds[i][j].getObjectID().toString();
                        if (nextOid.indexOf(entryOid) < 0) {
                            if (resList.isEmpty()) {
                                lineNumberGot = true;
                                lineNumber = 0;
                            }
                            meetEnd = true;
                            break OUT;
                        }
                        resList.add(binds[i][j]);
                        if (!lineNumberGot) {
                            if (!firstColumnOidGot) {
                                String s = nextOid.substring(entryOid.length() + 1);
                                String s2 = s.substring(0, s.indexOf("."));
                                firstColumnOid = entryOid + "." + s2;
                                lineNumber++;
                                firstColumnOidGot = true;
                                continue;
                            }
                            if (nextOid.indexOf(firstColumnOid) >= 0)
                                lineNumber++;
                            else {
                                lineNumberGot = true;
                            }
                        }
                    }
                }

                if (!hasRet || meetEnd) {
                    if (!lineNumberGot)
                        throw new SfSnmpException(SfSnmpException.SBI_GET_ERROR, snmpSession.getErrorString()+",table"+entryOid);
                    int clomnNumber = 0;
                    if (!resList.isEmpty())
                        clomnNumber = resList.size() / lineNumber;
                    resu = new SnmpVarBind[lineNumber][clomnNumber];
                    for (int i = 0; i < resList.size(); i++)
                        resu[i % lineNumber][i / lineNumber] = (SnmpVarBind) resList.get(i);
                    break;
                }
            }
        } finally {
            snmpSession = null;
        }
        timeWatch.stop();
        System.out.println("****************************get all table " + entryOid + " used " + timeWatch.getTime()
                + " milliseconds.");
        return resu;
    }

    /**
     * 采用非BULK方式取全表
     * @param entryOid
     * @return
     */
    public SnmpVarBind[][] getAllTableValues_NotBulk(String entryOid) {
        StopWatch timeWatch = new StopWatch();
        timeWatch.start();
        SnmpTarget snmpSession = getSnmpTargetFromPool();
        snmpSession.setMaxNumRows(100000);
        SnmpVarBind[][] resu = null;
        try {
            String nextOid = entryOid;
            snmpSession.setObjectID(nextOid);
            SnmpVarBind[][] binds = snmpSession.snmpGetAllVariableBindings();
            if (binds == null)
                return new SnmpVarBind[0][];
            String firstOid = entryOid + ".1";
            int totalSize = binds.length;
            SnmpVarBind[] realBinds = new SnmpVarBind[totalSize];
            for (int i = 0; i < totalSize; i++) {
                realBinds[i] = binds[i][0];
            }
            int rowSize = 0;
            for (int j = 0; j < totalSize; j++) {
                String oid = realBinds[j].getObjectID().toString();
                if (SnmpOidUtil.isMibNodeByOid(oid, firstOid))
                    rowSize++;
            }
            int columnSize = totalSize / rowSize;
            resu = new SnmpVarBind[rowSize][columnSize];
            for (int k = 0; k < rowSize; k++) {
                for (int l = 0; l < columnSize; l++) {
                    resu[k][l] = realBinds[l * rowSize + k];
                }
            }
        } finally {
            snmpSession = null;
        }
        timeWatch.stop();
        System.out.println("****************************get all table " + entryOid + " used " + timeWatch.getTime()
                + " milliseconds.");
        return resu;
    }

    /**
     * 采用BULK方式取一列
     * @param columnOid
     * @param timeouts
     * @return
     */
    public SnmpVarBind[] getTableColumnValues_Bulk(String columnOid,int... timeouts) {
        if (false)
            return getTableColumnValues_NotBulk(columnOid);
        StopWatch timeWatch = new StopWatch();
        timeWatch.start();
        SnmpTarget snmpSession = getSnmpTargetFromPool();
        if(timeouts.length>0){
            snmpSession.setTimeout(timeouts[0]);
        }
        SnmpVarBind[] resu = null;
        try {
            String nextOid = columnOid;
            List<SnmpVarBind> resList = new ArrayList<SnmpVarBind>();
            boolean hasRet = false;
            boolean meetEnd = false;

            while (true) {
                snmpSession.setObjectID(nextOid);  
                SnmpVarBind[][] binds = snmpSession.snmpGetBulkVariableBindings();                
                if (binds == null)
                    throw new SfSnmpException(SfSnmpException.SBI_GET_ERROR, snmpSession.getErrorString()+",column:"+columnOid);

                OUT: for (int i = 0; i < binds.length; i++) {
                    for (int j = 0; j < binds[i].length; j++) {
                        hasRet = true;
                        nextOid = binds[i][j].getObjectID().toString();
                        if (nextOid.indexOf(columnOid) < 0) {
                            meetEnd = true;
                            break OUT;
                        }
                        resList.add(binds[i][j]);
                    }
                }
                if (!hasRet)
                    throw new SfSnmpException(SfSnmpException.SBI_GET_ERROR, snmpSession.getErrorString()+",column:"+columnOid);
                if (meetEnd) {
                    resu = new SnmpVarBind[resList.size()];
                    for (int i = 0; i < resu.length; i++)
                        resu[i] = (SnmpVarBind) resList.get(i);
                    
                    break;
                }
            }
        } finally {
            snmpSession = null;
        }
        timeWatch.stop();
        System.out.println("****************************get all column " + columnOid + " used "
                + timeWatch.getTime() + " milliseconds.");
        return resu;
    }
    
    /**
     * 采用非BULK方式取一列
     * @param ColumnOid
     * @return
     */
    public SnmpVarBind[] getTableColumnValues_NotBulk(String columnOid){
        StopWatch timeWatch = new StopWatch();
        timeWatch.start();
        SnmpTarget snmpSession = getSnmpTargetFromPool();
        snmpSession.setMaxNumRows(100000);
        SnmpVarBind[] resu = null;
        try {
            String nextOid = columnOid;
            List<SnmpVarBind> resList = new ArrayList<SnmpVarBind>();
            snmpSession.setObjectID(nextOid);
            SnmpVarBind[][] binds = snmpSession.snmpGetAllVariableBindings();
            if (binds == null)
                return new SnmpVarBind[0];
            for (int i = 0; i < binds.length; i++) {
                resList.add(binds[i][0]);
            }
            resu = new SnmpVarBind[resList.size()];
            for (int i = 0; i < resu.length; i++)
                resu[i] = (SnmpVarBind) resList.get(i);
        } finally {
            snmpSession = null;
        }
        timeWatch.stop();
        System.out.println("****************************get all column " + columnOid + " used "
                + timeWatch.getTime() + " milliseconds.");
        return resu;
    }   

    /**
     * 取多列，索引相同
     * 
     * @param indexes
     * @param columnOids
     * @return
     */
    public SnmpVarBind[][] getColumnsByIndex(List<String> indexes, String... columnOids) {
        int size = indexes.size();
        int columnSize = columnOids.length;
        SnmpVarBind[][] binds = new SnmpVarBind[size][];
        for (int i = 0; i < size; i++) {
            String index = indexes.get(i);
            String[] oids = new String[columnSize];
            for (int j = 0; j < columnSize; j++) {
                oids[j] = columnOids[j] + index;
            }
            binds[i] = getMultiOidValue(oids);
        }
        return binds;
    }     
    
    public SnmpVarBind[] getColumnByIndex(List<String> indexes, String columnOid) {
        int size = indexes.size();
        String[] oids = new String[size];
        for (int i = 0; i < size; i++) {
            String index = indexes.get(i);
            oids[i] = columnOid + index;
        }
        SnmpVarBind[] binds = getMultiOidValue(oids);
        return binds;
    }

    /**
     * 根据entry+索引+最后oid号得到MIB表中的一行 
     * @param entry
     * @param index
     * @param maxOid
     * @return
     */
    public SnmpVarBind[] getLineByIndex(String entry, String index, int maxOid) {
        String[] oids = new String[maxOid];
        for (int i = 1; i <= maxOid; i++) {
            oids[i - 1] = entry + "." + i + index;
        }
        return getMultiOidValues(oids);
    }

    /**
     * 去一列的索引，可以选择是否用BULK方法
     * @param oid
     * @param useBulk
     * @return
     */
    public List<String> getIndexs(String oid, boolean useBulk) {
        List<String> indexList = new ArrayList<String>();
        SnmpVarBind[] binds = useBulk ? getTableColumnValues_Bulk(oid)
                : getTableColumnValues_NotBulk(oid);
        for (SnmpVarBind snmpVarBind : binds) {
            String tempOid = snmpVarBind.getObjectID().toString();
            String indexStr = tempOid.substring(oid.length());
            indexList.add(indexStr);
        }
        return indexList;
    }
}
