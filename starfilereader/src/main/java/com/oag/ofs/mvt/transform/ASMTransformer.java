package com.oag.ofs.mvt.transform;

import java.util.GregorianCalendar;

import javax.xml.datatype.XMLGregorianCalendar;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

import com.oag.ofs.inbound.xml.FIMSStatusInbound;

public class ASMTransformer extends AbstractTransformer {

	@Override
	protected Object doTransform(Object src, String enc)
			throws TransformerException {
		FIMSStatusInbound inbound = new FIMSStatusInbound();
		inbound.setLastUpdTime(MVTTransformer.createCalendar((GregorianCalendar) GregorianCalendar.getInstance()));
		inbound.setSent(MVTTransformer.createCalendar((GregorianCalendar) GregorianCalendar.getInstance()));
		inbound.setLastUpdTran(-1);
		return inbound;
	}

}
