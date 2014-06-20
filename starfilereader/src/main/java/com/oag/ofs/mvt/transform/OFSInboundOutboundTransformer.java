package com.oag.ofs.mvt.transform;

import java.util.List;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

import com.oag.ofs.inbound.xml.FIMSStatusInbound;
import com.oag.ofs.inbound.xml.Flight;
import com.oag.ubma.disruption.manager.support.ofs.AcftReg;
import com.oag.ubma.disruption.manager.support.ofs.Airborne;
import com.oag.ubma.disruption.manager.support.ofs.Apt;
import com.oag.ubma.disruption.manager.support.ofs.Arrive;
import com.oag.ubma.disruption.manager.support.ofs.Carrier;
import com.oag.ubma.disruption.manager.support.ofs.CarrierInfo;
import com.oag.ubma.disruption.manager.support.ofs.Category;
import com.oag.ubma.disruption.manager.support.ofs.Delay;
import com.oag.ubma.disruption.manager.support.ofs.Depart;
import com.oag.ubma.disruption.manager.support.ofs.Detail;
import com.oag.ubma.disruption.manager.support.ofs.EquipInfo;
import com.oag.ubma.disruption.manager.support.ofs.EquipType;
import com.oag.ubma.disruption.manager.support.ofs.FIMSStatusResponse;
import com.oag.ubma.disruption.manager.support.ofs.FlightInfo;
import com.oag.ubma.disruption.manager.support.ofs.FlightStatus;
import com.oag.ubma.disruption.manager.support.ofs.Leg;
import com.oag.ubma.disruption.manager.support.ofs.OffBlock;
import com.oag.ubma.disruption.manager.support.ofs.OnBlock;
import com.oag.ubma.disruption.manager.support.ofs.Status;
import com.oag.ubma.disruption.manager.support.ofs.Trm;

public class OFSInboundOutboundTransformer extends AbstractTransformer {

	@Override
	protected Object doTransform(Object src, String enc)
			throws TransformerException {
		
		
		if (src instanceof FIMSStatusInbound) {
			FIMSStatusInbound inbound = (FIMSStatusInbound) src;
			// transform
			FIMSStatusResponse response = new FIMSStatusResponse();
			response.setUTCSendDateTime(inbound.getSent());
			response.setFeedSource("Star Alliance MuleSoft ESB flow");
			response.setFeedStatus("OK");
			response.setSendDateTime(inbound.getSent());
			
			FlightStatus status = new FlightStatus();
			response.setFlightStatus(status);
			status.setLastUpdDateTime(inbound.getLastUpdTime().toGregorianCalendar().toString());
			status.setLastUpdTran(inbound.getLastUpdTran());
			List<FlightInfo> outboundFlights = status.getFlightInfo();
			for(Flight inboundFlight: inbound.getFlight()) {
				FlightInfo fi = new FlightInfo();
				CarrierInfo ci = new CarrierInfo();
				Carrier c = new Carrier();
				c.setCarrierCd(inboundFlight.getCarrier());
				c.setValue(inboundFlight.getCarrier());
				ci.setCarrier(c);
				fi.setCarrierInfo(ci);
				fi.setFlightDesig(inboundFlight.getFltNo());
				fi.setTransId(Integer.toString(inboundFlight.getTransId()));
				List<Leg> outboundLegs = fi.getLeg();
				for (com.oag.ofs.inbound.xml.Leg inboundLeg: inboundFlight.getLeg()) {
					outboundLegs.add(transform(inboundLeg));
				}
				outboundFlights.add(fi);
			}
			return response;
		}
		return null;
	}

	private Leg transform(com.oag.ofs.inbound.xml.Leg inboundLeg) {
		Leg outboundLeg = new Leg();
		outboundLeg.setArrive(transform(inboundLeg.getArr()));
		outboundLeg.setDepart(transform(inboundLeg.getDep()));
		outboundLeg.setEquipInfo(transform(inboundLeg.getEquip()));
		return outboundLeg;
	}
	
	private Arrive transform(com.oag.ofs.inbound.xml.Arr inboundArr) {
		Arrive arr = new Arrive();
		Apt apt = new Apt();
		apt.setAptCd(inboundArr.getApt());
		apt.setValue(inboundArr.getApt());
		arr.setApt(apt);
		arr.setDelay(transform(inboundArr.getDelay()));
		arr.setOnBlock(transform(inboundArr.getOnBlock()));
		arr.setTrm(transform(inboundArr.getTrm()));
		return arr;
	}
	private Depart transform(com.oag.ofs.inbound.xml.Dep inboundDep) {
		Depart dep = new Depart();
		Apt apt = new Apt();
		apt.setAptCd(inboundDep.getApt());
		apt.setValue(inboundDep.getApt());
		dep.setApt(apt);
		dep.setDelay(transform(inboundDep.getDelay()));
		dep.setAirborne(transform(inboundDep.getAirborne()));
		dep.setOffBlock(transform(inboundDep.getOffBlock()));
		return dep;
	}
	
	private Airborne transform(com.oag.ofs.inbound.xml.Airborne inboundAirbourne) {
		if (inboundAirbourne==null) {
			return null;
		}
		Airborne a = new Airborne();
		a.setAct(inboundAirbourne.getAct());
		a.setEst(inboundAirbourne.getEst());
		return a;
	}
	private Delay transform(com.oag.ofs.inbound.xml.Delay inboundDelay) {
		if (inboundDelay==null){
				return null;
		}
		Delay delay = new Delay();
		Category cat = new Category();
		cat.setCatId(inboundDelay.getRsnCode());
		cat.setValue(inboundDelay.getRsnCode());
		Detail detail = new Detail();
		detail.setDetailCd(inboundDelay.getDet());
		detail.setValue(inboundDelay.getDet());
		Status status = new Status();
		status.setStatusCd(inboundDelay.getStat());
		delay.setCategory(cat);
		delay.setStatus(status);
		delay.setDetail(detail);
		return delay;
	}

	private OffBlock transform(com.oag.ofs.inbound.xml.OffBlock inboundOffBlock) {
		if (inboundOffBlock==null) {
			return null;
		}
		OffBlock offblock= new OffBlock();
		offblock.setAct(inboundOffBlock.getAct());
		return offblock;
	}
	private OnBlock transform(com.oag.ofs.inbound.xml.OnBlock inboundOnBlock) {
		if (inboundOnBlock==null) {
			return null;
		}
		OnBlock onblock= new OnBlock();
		onblock.setAct(inboundOnBlock.getAct());
		onblock.setEst(inboundOnBlock.getEst());
		return onblock;
	}
	
	private Trm transform(com.oag.ofs.inbound.xml.Trm inboundTerminal) {
		if (inboundTerminal==null) {
			return null;
		}
		Trm terminal = new Trm();
		terminal.setAct(inboundTerminal.getAct());
		return terminal;
	}
	
	public EquipInfo transform(com.oag.ofs.inbound.xml.Equip inboundEquip) {
		if (inboundEquip==null) {
			return null;
		}
		EquipInfo eq= new EquipInfo();
		EquipType type = new EquipType();
		type.setAct(inboundEquip.getAct());
		AcftReg reg = new AcftReg();
		reg.setAct(inboundEquip.getReg().getAct());
		eq.setAcftReg(reg);
		eq.setEquipType(type);
		return eq;
	}
}
