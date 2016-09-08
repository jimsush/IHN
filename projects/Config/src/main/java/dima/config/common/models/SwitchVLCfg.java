package dima.config.common.models;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class SwitchVLCfg implements Comparable{

	private int cfgTableId;
	private int planNum;
	private int defaultPlanId;
	private List<SwitchVLPlan> plans;
	
	public SwitchVLCfg(){}
	public SwitchVLCfg(int cfgTableId,int planNum,int defaultPlanId){
		this.cfgTableId=cfgTableId;
		this.planNum=planNum;
		this.defaultPlanId=defaultPlanId;
	}
	
	@Override
	public int compareTo(Object o) {
		if(o!=null && (o instanceof SwitchVLCfg)){
			SwitchVLCfg cfg=(SwitchVLCfg)o;
			return cfgTableId-cfg.cfgTableId;
		}
		return 1;
	}
	

	public int getCfgTableId() {
		return cfgTableId;
	}

	public void setCfgTableId(int cfgTableId) {
		this.cfgTableId = cfgTableId;
	}

	public int getPlanNum() {
		return planNum;
	}

	public void setPlanNum(int planNum) {
		this.planNum = planNum;
	}

	public int getDefaultPlanId() {
		return defaultPlanId;
	}

	public void setDefaultPlanId(int defaultPlanId) {
		this.defaultPlanId = defaultPlanId;
	}

	@Override
	public String toString(){
		return cfgTableId+" planN:"+planNum+" defaultPlan:"+defaultPlanId;
	}


	/**
	 * @return the plans
	 */
	public List<SwitchVLPlan> getPlans() {
		if(plans==null){
			plans=new ArrayList<>();
		}
		return plans;
	}

	public void addPlan(SwitchVLPlan plan){
		List<SwitchVLPlan> plans2 = getPlans();
		plans2.add(plan);
	}
	
	/**
	 * @param plans the plans to set
	 */
	public void setPlans(List<SwitchVLPlan> plans) {
		this.plans = plans;
	}

}
