package br.datamaio.gcm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;

public class GcmSender {
	private static final int MULTICAST_SIZE = 1000;
	private static final Executor threadPool = Executors.newFixedThreadPool(5);

	
	public static String send(String title, String body) throws IOException {
		com.google.android.gcm.server.Sender sender = new com.google.android.gcm.server.Sender(new GcmApiKey().getKey());
				
		List<String> devices = GcmDatastore.getDevices();
		String status;
		if (devices.isEmpty()) {
			status = "Message ignored as there is no device registered!";
		} else {
			// NOTE: check below is for demonstration purposes; a real
			// application
			// could always send a multicast, even for just one recipient
			if (devices.size() == 1) {
				// send a single message using plain post
				String registrationId = devices.get(0);
				Message message = new Message.Builder()
						.addData("title", title)
						.addData("body", body).build();
				Result result = sender.send(message, registrationId, 5);
				status = "Sent message to one device: " + result;
			} else {
				// send a multicast message using JSON
				// must split in chunks of 1000 devices (GCM limit)
				int total = devices.size();
				List<String> partialDevices = new ArrayList<String>(total);
				int counter = 0;
				int tasks = 0;
				for (String device : devices) {
					counter++;
					partialDevices.add(device);
					int partialSize = partialDevices.size();
					if (partialSize == MULTICAST_SIZE || counter == total) {
						asyncSend(sender, partialDevices, title, body);
						partialDevices.clear();
						tasks++;
					}
				}
				status = "Asynchronously sending " + tasks + " multicast messages to " + total + " devices";
			}
		}
		return status;
	}

	private static void asyncSend(com.google.android.gcm.server.Sender sender, List<String> partialDevices, String title, String body) {
		// make a copy
		final List<String> devices = new ArrayList<String>(partialDevices);
		threadPool.execute(new Runnable() {

			public void run() {
				Message message = new Message.Builder()
						.addData("title", title)
						.addData("body", body)
						.build();
				MulticastResult multicastResult;
				try {
					multicastResult = sender.send(message, devices, 5);
				} catch (IOException e) {
					// logger.log(Level.SEVERE, "Error posting messages", e);
					return;
				}
				List<Result> results = multicastResult.getResults();
				// analyze the results
				for (int i = 0; i < devices.size(); i++) {
					String regId = devices.get(i);
					Result result = results.get(i);
					String messageId = result.getMessageId();
					if (messageId != null) {
						// logger.fine("Succesfully sent message to device: " +
						// regId + "; messageId = " + messageId);
						String canonicalRegId = result.getCanonicalRegistrationId();
						if (canonicalRegId != null) {
							// same device has more than on registration id:
							// update it
							// logger.info("canonicalRegId " + canonicalRegId);
							GcmDatastore.updateRegistration(regId, canonicalRegId);
						}
					} else {
						String error = result.getErrorCodeName();
						if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
							// application has been removed from device -
							// unregister it
							// logger.info("Unregistered device: " + regId);
							GcmDatastore.unregister(regId);
						} else {
							// logger.severe("Error sending message to " + regId
							// + ": " + error);
						}
					}
				}
			}
		});
	}

}
