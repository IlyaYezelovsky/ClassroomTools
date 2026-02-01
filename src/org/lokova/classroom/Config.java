package org.lokova.classroom;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public enum Config {

	INSTANCE;

	public class ConfigData {

		private String dataPath;

		private ConfigData() {
			reset();
		}

		public String getDataPath() {
			return dataPath;
		}

		public void reset() {
			dataPath = "data.ser";
		}

		public void setDataPath(String dataPath) {
			this.dataPath = dataPath;
		}

	}

	static {
		initialize();
	}

	public static ConfigData data() {
		return INSTANCE.cfgData;
	}

	public static void initialize() {
		INSTANCE.cfgData = INSTANCE.new ConfigData();
		File file = new File("config.json");
		Gson gson = new Gson();
		if (!file.exists()) {
			try (var writer = new FileWriter("config.json")) {
				file.createNewFile();
				gson.toJson(INSTANCE.cfgData, writer);
			} catch (IOException e) {
				throw new UncheckedIOException("Failed to initialize config", e);
			}
		} else {
			INSTANCE.load();
		}
	}

	private ConfigData cfgData;

	public ConfigData getData() {
		return cfgData;
	}

	public void load() {
		File file = new File("config.json");
		Gson gson = new Gson();
		try (var reader = new FileReader(file)) {
			ConfigData loaded = gson.fromJson(reader, ConfigData.class);
			cfgData = loaded;
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to load config", e);
		}
	}

	public void save() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (var writer = new FileWriter("config.json")) {
			gson.toJson(cfgData, writer);
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to save config", e);
		}
	}

}