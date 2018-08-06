package common;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.sun.management.OperatingSystemMXBean;

public class ResourceMonitor {

	public static HashMap<String, String> HashResource = new HashMap<String, String>();

	public static void printUsage(Runtime runtime) {
		OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
		double total;

		total = runtime.totalMemory() - runtime.freeMemory();

		String LogTime = new SimpleDateFormat("yyyy, MM, dd, HH, mm, ss").format(new Date());

		if (HashResource.containsKey("UserMemory")) {
			HashResource.put("UserMemory", HashResource.get("UserMemory") + ";" + "{ x: new Date(" + LogTime + "), y: " + String.format("%.2f", ((double) total / (double) runtime.totalMemory()) * 100) + " }");
		} else {
			HashResource.put("UserMemory", "{ x: new Date(" + LogTime + "), y: " + String.format("%.2f", ((double) total / (double) osBean.getTotalPhysicalMemorySize()) * 100) + " }");
		}

		if (HashResource.containsKey("CPUPercentage")) {
			HashResource.put("CPUPercentage", HashResource.get("CPUPercentage") + ";" + "{ x: new Date(" + LogTime + "), y: " + String.format("%.2f", osBean.getProcessCpuLoad()) + " }");
		} else {
			HashResource.put("CPUPercentage", "{ x: new Date(" + LogTime + "), y: " + String.format("%.2f", osBean.getProcessCpuLoad()) + " }");
		}

	}

}
