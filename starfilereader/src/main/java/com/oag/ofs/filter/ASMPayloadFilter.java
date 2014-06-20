package com.oag.ofs.filter;

import org.mule.api.MuleMessage;
import org.mule.api.routing.filter.Filter;

public class ASMPayloadFilter implements Filter {

	@Override
	public boolean accept(MuleMessage message) {
		try {
			return message.getPayloadAsString().contains("ASM");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
