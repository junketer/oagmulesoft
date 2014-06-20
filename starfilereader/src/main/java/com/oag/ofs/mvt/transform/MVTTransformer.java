package com.oag.ofs.mvt.transform;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

import com.oag.ofs.inbound.xml.Arr;
import com.oag.ofs.inbound.xml.DateTime;
import com.oag.ofs.inbound.xml.Dep;
import com.oag.ofs.inbound.xml.Equip;
import com.oag.ofs.inbound.xml.FIMSStatusInbound;
import com.oag.ofs.inbound.xml.Flight;
import com.oag.ofs.inbound.xml.Leg;
import com.oag.ofs.inbound.xml.ObjectFactory;
import com.oag.ofs.inbound.xml.OffBlock;
import com.oag.ofs.inbound.xml.OnBlock;

/**
 * Transforms from an MVT message to a Java Object which can be 
 * marshalled in to an XML string
 * 
 */
public class MVTTransformer extends AbstractTransformer {

	@Override
	protected Object doTransform(Object src, String enc)
			throws TransformerException {
		Object returnObj = src;
		if (src instanceof String) {
			BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(((String) src).getBytes())));
			String line = null;
			boolean isFlightNumberLine=false;
			boolean isStatusLine = false;
			try {
				ObjectFactory f = new ObjectFactory();
				FIMSStatusInbound inboundXML=f.createFIMSStatusInbound();
				GregorianCalendar cal = new GregorianCalendar();
				XMLGregorianCalendar xmlCal=null;
				try {
					xmlCal=DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
					inboundXML.setLastUpdTime(xmlCal);
				} catch (DatatypeConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inboundXML.setLastUpdTran((int)System.currentTimeMillis());
				inboundXML.setOrgId(-1);
				inboundXML.setProfileId(-1);
				inboundXML.setUTCLOCInd("LOC");
				Flight flight = f.createFlight();
				inboundXML.getFlight().add(flight);
				MVTMessage msg = new MVTMessage();
				Leg leg = f.createLeg();
				Equip equip = f.createEquip();
				leg.setEquip(equip);
				Dep dep = f.createDep();
				Arr arr = null;
				while ((line = br.readLine()) !=null) {
					if ("MVT".equals(line)) {
						isFlightNumberLine=true;
						
						continue;
					}
					if (isFlightNumberLine) {
						String[] parts = line.split("/");
						int flightNumLen = parts[0].length();
						flight.setCarrier(parts[0].substring(0, 2));
						flight.setFltNo(parts[0].substring(2,flightNumLen));
						String config = parts[1];
						int i = config.indexOf(".");
						msg.setDayOfMonth(config.substring(0,i));
						GregorianCalendar schedCal = new GregorianCalendar();
						schedCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(config.substring(0,i)));
						flight.setSchdDate(createCalendar(schedCal));
						
						int j = config.indexOf(".", i+1);
						equip.setAct(config.substring(i+1,j));
						leg.setDep(dep);
						dep.setApt(config.substring(j+1));
						isFlightNumberLine=false;
						isStatusLine=true;
						continue;
					}
					if (isStatusLine) {
						String[] parts = line.split(" ");
						for (String p: parts) {
							if(p.startsWith("AD")) {
								if (p.contains("/")) {
									String[] times =p.substring(2).split("/");
									OffBlock block = f.createOffBlock();;
									block.setAct(createCalendarFromTime(times[0]));
									dep.setOffBlock(block);
									DateTime dt = f.createDateTime();
									dt.setAct(createCalendarFromTime(times[1]));
									dep.setDateTime(dt);
								} else {
									DateTime dt = f.createDateTime();
									dt.setAct(createCalendarFromTime(p.substring(2)));
									dep.setDateTime(dt);
								}
							} else if (p.startsWith("AA")) {
								arr=f.createArr();
								if (p.contains("/")) {
									String[] times =p.substring(2).split("/");
									OnBlock block = f.createOnBlock();;
									block.setAct(createCalendarFromTime(times[0]));
									arr.setOnBlock(block);
									DateTime dt = f.createDateTime();
									dt.setAct(createCalendarFromTime(times[1]));
									arr.setDateTime(dt);
								} else {
									DateTime dt = f.createDateTime();
									dt.setAct(createCalendarFromTime(p.substring(2)));
									arr.setDateTime(dt);
								}
								
							} else if (p.startsWith("EA")) {
								if (arr==null) {
									arr=f.createArr();
									arr.setDateTime(f.createDateTime());
								}
								arr.getDateTime().setEst(createCalendarFromTime(p.substring(2)));
							} else {
								if (arr!=null) {
									arr.setApt(p);
								}
							}
						}
						if (arr!=null) {
							leg.setArr(arr);
						}
						isStatusLine=false;
					}
					if (line.startsWith("PX")) {
						msg.setPaxCount(line.substring(2));
					}
					
				}
				returnObj=inboundXML; 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return returnObj;
	}

	static XMLGregorianCalendar createCalendar(GregorianCalendar cal) {
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	static XMLGregorianCalendar createCalendarFromTime(String time) {
		GregorianCalendar cal = new GregorianCalendar();
		time=time.trim();
		int hours = Integer.parseInt(time.substring(0, 2));
		int mins= Integer.parseInt(time.substring(2));
		cal.set(Calendar.HOUR_OF_DAY, hours);
		cal.set(Calendar.MINUTE, mins);
		return createCalendar(cal);
	}
}
