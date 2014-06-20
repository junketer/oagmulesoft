package com.oag.ofs.star.log.transformer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Splits consolidated input files back out in to separate messages
 * 2 possible inputs
 * 0: raw directory if different from default (k:\\OFS\\Data\\raw\\)
 * 1: file name in that directory to split (rather than all files in the dir). If used, param 0 is mandatory
 * @author dtillin
 *
 */
public class LogFileSplitter {
	
	static final String raw="k:\\OFS\\Data\\raw\\";
	static final String in="k:\\OFS\\Data\\split\\";
	
	public static void main(String[] args) throws IOException {
		String input = raw;
		File raw = new File(input);
		if (args.length>0) {
			input = args[0];
		} 
		if (args.length>1) {
			raw = new File(input+args[1]);
		}
		if (raw.isDirectory()) {
			for(File f : raw.listFiles())  {
				split(f);
			}
		} else {
			split(raw);
		}
	}

	private static void split(File f) throws FileNotFoundException, IOException {
		InputStream is = new FileInputStream(f);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		BufferedWriter bw=null;
		int index = 0;
		String line = null;
		boolean writeData=false;
		while ((line = br.readLine()) !=null) {
			if (line.contains("LQ_OAG")) {
				continue;
			}
			if ("QU".equals(line.trim())) {
				String newFileName = in +f.getName().replace(".", "-" + Integer.toString(index)+".");
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(newFileName))));
				bw.write(line);
			} else if (line.trim().length()>0) {
				bw.newLine();
				bw.write(line);
			} else if (bw !=null && line.trim().length()==0) {
				writeData=true;
			}
			
			if (writeData) {
				bw.flush();
				bw.close();
				bw=null;
				index++;
				writeData=false;
			}
			
		}
		br.close();
	}
}
