package dima.config.common.models;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class SwitchVLPlan implements Comparable{

	private int planId;
	private int vlNum=0;
	private List<SwitchVL> vls;
	
	public SwitchVLPlan(){}
	public SwitchVLPlan(int planId){
		this.planId=planId;
	}
	
	@Override
	public int compareTo(Object o) {
		if(o!=null && (o instanceof SwitchVLPlan)){
			SwitchVLPlan plan=(SwitchVLPlan)o;
			return planId-plan.planId;
		}
		return 1;
	}
	
	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public int getVlNum() {
		return vlNum;
	}

	public void setVlNum(int vlNum) {
		this.vlNum = vlNum;
	}

	@Override
	public String toString(){
		return planId+" vlN:"+vlNum;
	}

	/**
	 * @return the vls
	 */
	public List<SwitchVL> getVls() {
		if(vls==null){
			vls=new ArrayList<>();
		}
		return vls;
	}
	
	public void addVL(SwitchVL vl){
		List<SwitchVL> vls2 = getVls();
		vls2.add(vl);
	}

	/**
	 * @param vls the vls to set
	 */
	public void setVls(List<SwitchVL> vls) {
		this.vls = vls;
	}

}
