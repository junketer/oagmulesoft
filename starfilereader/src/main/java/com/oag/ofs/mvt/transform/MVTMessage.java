package com.oag.ofs.mvt.transform;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mvt")
public class MVTMessage {

	private String carrier;
	private String serviceNum;
	private String dayOfMonth;
	private String equipment;
	private String port;
	private String action;
	private String offBlock;
	private String onBlock;
	
	private String actualDep;
	private String actualArr;
	private String estDep;
	private String estArr;
	
	private String paxCount;
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public String getServiceNum() {
		return serviceNum;
	}
	public void setServiceNum(String serviceNum) {
		this.serviceNum = serviceNum;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public String getPaxCount() {
		return paxCount;
	}
	public void setPaxCount(String paxCount) {
		this.paxCount = paxCount;
	}
	public String getActualDep() {
		return actualDep;
	}
	public void setActualDep(String actualDep) {
		this.actualDep = actualDep;
	}
	public String getActualArr() {
		return actualArr;
	}
	public void setActualArr(String actualArr) {
		this.actualArr = actualArr;
	}
	public String getEstDep() {
		return estDep;
	}
	public void setEstDep(String estDep) {
		this.estDep = estDep;
	}
	public String getEstArr() {
		return estArr;
	}
	public void setEstArr(String estArr) {
		this.estArr = estArr;
	}
	public String getOnBlock() {
		return onBlock;
	}
	public void setOnBlock(String onBlock) {
		this.onBlock = onBlock;
	}
	public String getOffBlock() {
		return offBlock;
	}
	public void setOffBlock(String offBlock) {
		this.offBlock = offBlock;
	}

}
