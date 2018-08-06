package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {

	public static Properties props = new Properties();
	public static Properties Repository = new Properties();

	public static void ConfigRead(String Config) {

		File configFile = new File(Config);
		try {
			FileReader reader = new FileReader(configFile);
			props.load(reader);
			reader.close();
		} catch (FileNotFoundException ex) {
			CustomReporter.initiateTest("Load Config Value!");
			CustomReporter.MessageLogger("File does not exist.", CustomReporter.status.Error);

		} catch (IOException ex) {
			CustomReporter.initiateTest("Load Config Value!");
			CustomReporter.MessageLogger("I/O error.", CustomReporter.status.Error);
		}

	}

	public static void ReadRepository(String ORFile) {
		File configFile = new File(ORFile);
		try {
			FileReader reader = new FileReader(configFile);

			Repository.load(reader);
			reader.close();
		} catch (FileNotFoundException ex) {
			CustomReporter.MessageLogger("File does not exist.", CustomReporter.status.Error);

		} catch (IOException ex) {
			CustomReporter.MessageLogger("I/O error.", CustomReporter.status.Error);
		}

	}

}
