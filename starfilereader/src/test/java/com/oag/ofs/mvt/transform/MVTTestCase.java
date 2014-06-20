package com.oag.ofs.mvt.transform;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.junit.Test;
import org.mule.api.transformer.TransformerException;

public class MVTTestCase extends TestCase {

	
	@Test
	public void testMVTTransform() throws IOException {
		StringBuilder sb = load("MVT.txt");
		try {
			assertNotNull(new MVTTransformer().doTransform(sb.toString(), "UTF-8"));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testMVTTransform2() throws IOException {
		StringBuilder sb = load("MVT2.txt");
		try {
			assertNotNull(new MVTTransformer().doTransform(sb.toString(), "UTF-8"));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testMVTTransform3() throws IOException {
		StringBuilder sb = load("MVT3.txt");
		try {
			assertNotNull(new MVTTransformer().doTransform(sb.toString(), "UTF-8"));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	private StringBuilder load(String file) throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream(file);
		byte[] b= new byte[128];
		int read=0;
		StringBuilder sb = new StringBuilder();
		while ((read=is.read(b))>0) {
			sb.append(new String(b,0,b.length));
		}
		return sb;
	}
}
