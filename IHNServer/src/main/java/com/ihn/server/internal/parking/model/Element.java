package com.ihn.server.internal.parking.model;

import java.util.Map;

public class Element {

	/** LJZ_P1_B1_DR_1 */
	protected String id;
	
	/** Door 1 */
	protected String name;
	
	/** Door 1 */
	protected String enname;
	
	protected ElementType type;
	
	protected int posX;
	protected int posY;
	protected int posZ;
	
	protected String icon;
	
	protected Map<String,String> styles;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	public ElementType getType() {
		return type;
	}

	public void setType(ElementType type) {
		this.type = type;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getPosZ() {
		return posZ;
	}

	public void setPosZ(int posZ) {
		this.posZ = posZ;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Map<String, String> getStyles() {
		return styles;
	}

	public void setStyles(Map<String, String> styles) {
		this.styles = styles;
	}
	
	
	
}
