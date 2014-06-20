package com.oag.ofs.mvt.transform;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

public class MVTOrASMTransformer extends AbstractTransformer {

	@Override
	protected Object doTransform(Object src, String enc)
			throws TransformerException {
		if (src instanceof String) {
			String payload = (String) src;
			if (payload.contains("MVT")) {
				return new MVTTransformer().doTransform(src, enc);
			} else {
				return new ASMTransformer().doTransform(src, enc);
			}
		}
		return src;
	}

}
