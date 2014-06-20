package com.oag.ofs.filter;

import org.mule.api.MuleMessage;
import org.mule.api.routing.filter.Filter;

/**
 * MVT / ASM payload filter
 * Evaluates true if this is an MVT or not
 * @author DTillin
 *
 */
public class MVTPayloadFilter implements Filter {

	@Override
	public boolean accept(MuleMessage message) {
	
		try {
			return message.getPayloadAsString().contains("MVT");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
