package dima.config.view.netconfig.topo;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.lang.reflect.Field;

import dima.config.common.ConfigUtils;
import twaver.Link;
import twaver.TWaverConst;
import twaver.network.TNetwork;
import twaver.network.ui.LinkUI;

public class MidaPolyLinkUI extends LinkUI{

	public MidaPolyLinkUI(TNetwork network, Link link) {
		super(network, link);
		this.link = (MidaPolyLink)link;
	}

	@Override
	public java.awt.geom.GeneralPath getPath()
    {
        if(path == null)
            path = network.getLinkLayouter().getLinkPath(this);
        
        return adjustPath(path);
    }
    
    private java.awt.geom.GeneralPath adjustPath(java.awt.geom.GeneralPath orgPath){
    	Link linkElement=(Link)element;
    	Object offset = element.getClientProperty(ConfigUtils.PROP_LINK_ADJUST_RATIO);
    	if(offset==null)
    		return orgPath;
    	
    	try{
    		Float offsetF=Float.valueOf(offset.toString());
    		if(offsetF<=0f || offsetF>=1f)
    			return orgPath;
    		
    		Field f = Path2D.Float.class.getDeclaredField("floatCoords");
    		f.setAccessible(true);
    		Object orgProps = f.get(orgPath);
    		if(orgProps!=null){
    			if(orgProps instanceof float[]){
    				float[] orgLocations=(float[])orgProps;
    				if(orgLocations.length>=8 ){//&& ){
    					if(TWaverConst.LINK_TYPE_XSPLIT==linkElement.getLinkType()){
	    					if(orgLocations[0]>=0 && orgLocations[2]==orgLocations[4] 
	    							&& orgLocations[1]==orgLocations[3] && orgLocations[5]==orgLocations[7]){
	    						orgLocations[2]=orgLocations[0]+(orgLocations[6]-orgLocations[0])*offsetF;
	    						orgLocations[4]=orgLocations[2];
	    					}
    					}else if(TWaverConst.LINK_TYPE_LEFT==linkElement.getLinkType()){
    						if(orgLocations[0]>=0 && orgLocations[2]==orgLocations[4] 
	    							&& orgLocations[1]==orgLocations[3] && orgLocations[5]==orgLocations[7]){
	    						orgLocations[2]=orgLocations[0]-12-50*offsetF;
	    						orgLocations[4]=orgLocations[2];
	    					}
    					}else if(TWaverConst.LINK_TYPE_RIGHT==linkElement.getLinkType()){
    						if(orgLocations[0]>=0 && orgLocations[2]==orgLocations[4] 
	    							&& orgLocations[1]==orgLocations[3] && orgLocations[5]==orgLocations[7]){
	    						orgLocations[2]=orgLocations[0]+12+50*offsetF;
	    						orgLocations[4]=orgLocations[2];
	    					}
    					}
    				}
    			}
    		}
    	}catch(Exception ex){
    		System.out.println("WARNING: "+ex.getClass().getSimpleName()+":"+ex.getMessage());
    		return orgPath;
    	}
    	return orgPath;
    }

	MidaPolyLink link;

	public void paintBody(Graphics2D g2d) {
		super.paintBody(g2d);
		if(this.isFlowing()){
			if(link.getFlowingGapUp()!= -1){
				g2d.setColor(link.getFlowingColor());
				float[] dashPatten = new float[] {0, link.getFlowingGapUp()};
				Stroke dashStroke = new BasicStroke(3, //代表圆点直径
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND,
						link.getFlowingGapUp(),
						dashPatten,
						this.getDashPhase());
				g2d.setStroke(dashStroke);
				double offset = link.getFlowingOffset();
				double tx = Math.sin(this.linkAngle) * offset;
				double ty = -Math.cos(this.linkAngle) * offset;
				//System.out.println(tx + " , " + ty);
				g2d.translate(tx, ty);
				g2d.draw(this.getPath());
				g2d.translate(-tx, -ty);
			}

			if(link.getFlowingGapDn()!= -1){
				g2d.setColor(link.getFlowingColor());
				float[] dashPatten = new float[] {0, link.getFlowingGapDn()};
				Stroke dashStroke = new BasicStroke(3, //代表圆点直径
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND,
						link.getFlowingGapDn(),
						dashPatten,
						1000-this.getDashPhase());
				//System.out.println(this.getDashPhase());
				g2d.setStroke(dashStroke);
				double offset = -link.getFlowingOffset();
				double tx = Math.sin(this.linkAngle) * offset;
				double ty = -Math.cos(this.linkAngle) * offset;
				//System.out.println(tx + " , " + ty);
				g2d.translate(tx, ty);
				g2d.draw(this.getPath());
				g2d.translate(-tx, -ty);
			}
		}
	}

}
