/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.datamaio.fly.web;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public final class Datastore {

	private static final List<String> IDS = new ArrayList<String>();
	private static final Logger LOGGER = Logger.getLogger(Datastore.class);
	static {
		load();
	}

	private Datastore() {
		throw new UnsupportedOperationException();
	}

	public static void register(String regId) {
		LOGGER.info("Registering " + regId);
		synchronized (IDS) {
			IDS.add(regId);
			save();
		}
	}

	public static void unregister(String regId) {
		LOGGER.info("Unregistering " + regId);
		synchronized (IDS) {
			IDS.remove(regId);
			save();
		}
	}

	public static void updateRegistration(String oldId, String newId) {
		LOGGER.info("Updating " + oldId + " to " + newId);
		synchronized (IDS) {
			IDS.remove(oldId);
			IDS.add(newId);
			save();
		}
	}

	public static List<String> getDevices() {
		synchronized (IDS) {
			return new ArrayList<String>(IDS);
		}
	}

	public static void save() {
		synchronized (IDS) {
			Path path = Paths.get("ids.data");
			try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {			
				oos.writeObject(IDS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void load() {
		synchronized (IDS) {
			Path path = Paths.get("ids.data");
			if(Files.exists(path)) {
				try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
					@SuppressWarnings("unchecked")
					List<String> ids = (List<String>) ois.readObject();
					IDS.clear();
					IDS.addAll(ids);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
