package br.com.datamaio.env.util;

import java.io.*;

class ThreadedStreamHandler extends Thread {
	private InputStream in;
	private PrintWriter pw;
	private boolean printoutput = false;
	private StringBuilder output = new StringBuilder();

	ThreadedStreamHandler(InputStream in) {
		this(in, null);
	}

	ThreadedStreamHandler(InputStream in, OutputStream out) {
        this.in = in;
        if(out!=null) {
            this.pw = new PrintWriter(out);
        }
	}
	
	public void run() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = br.readLine()) != null) {
				if(printoutput){
					System.out.println(line);
				}
				output.append(line + "\n");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// ignore this one
			}
		}
	}

	public String getOutput() {
		String st = output.toString();
		if(st.contains("[sudo] password for")) {
			return "";
		}
		return st;
	}

	public void setPrintoutput(boolean printoutput) {
		this.printoutput = printoutput;
	}
	
	private void doSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// ignore
		}
	}
}
