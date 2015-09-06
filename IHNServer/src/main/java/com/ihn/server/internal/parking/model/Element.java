package com.ihn.server.internal.parking.model;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Element {

	protected String parkid;
	
	/** LJZ_P1_B1_DR_1 */
	protected String id;
	
	protected String parent;
	
	/** Door 1 */
	protected String name;
	
	/** Door 1 */
	//protected String enname;
	
	//protected ElementType type;
	protected String type;
	
	/** Build or Device or Spot or Addition */
	protected String category;
	
	protected Double positionx;
	protected Double positiony;
	protected Double positionz;
	
	protected String picture;
	protected Double width; //x
	protected Double height; //y
	protected Double depth; //z
	
	protected Double rotationx;
	protected Double rotationy;
	protected Double rotationz;
	
	protected Boolean transparent;
	protected Double opacity;
	public String getParkid() {
		return parkid;
	}
	public void setParkid(String parkid) {
		this.parkid = parkid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public String getEnname() {
//		return enname;
//	}
//	public void setEnname(String enname) {
//		this.enname = enname;
//	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getPositionx() {
		return positionx;
	}
	public void setPositionx(Double positionx) {
		this.positionx = positionx;
	}
	public Double getPositiony() {
		return positiony;
	}
	public void setPositiony(Double positiony) {
		this.positiony = positiony;
	}
	public Double getPositionz() {
		return positionz;
	}
	public void setPositionz(Double positionz) {
		this.positionz = positionz;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Double getDepth() {
		return depth;
	}
	public void setDepth(Double depth) {
		this.depth = depth;
	}
	public Double getRotationx() {
		return rotationx;
	}
	public void setRotationx(Double rotationx) {
		this.rotationx = rotationx;
	}
	public Double getRotationy() {
		return rotationy;
	}
	public void setRotationy(Double rotationy) {
		this.rotationy = rotationy;
	}
	public Double getRotationz() {
		return rotationz;
	}
	public void setRotationz(Double rotationz) {
		this.rotationz = rotationz;
	}
	public Boolean getTransparent() {
		return transparent;
	}
	public void setTransparent(Boolean transparent) {
		this.transparent = transparent;
	}
	public Double getOpacity() {
		return opacity;
	}
	public void setOpacity(Double opacity) {
		this.opacity = opacity;
	}
	public void setCategory(String category){
		this.category=category;
	}
	public String getCategory(){
		return this.category;
	}
	
		
}
