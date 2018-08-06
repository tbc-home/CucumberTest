package common;

import java.util.HashMap;

public class HashTableRepository {

	public static HashMap<String, String> Hash = new HashMap<String, String>();
	public static HashMap<String, String> HashCampaign = new HashMap<String, String>();

	public static void setHash(String sKey, String sValue) {
		if (HashCampaign.containsKey(CommonLib.CampaignName)) {
			if (!(("," + HashCampaign.get(CommonLib.CampaignName)).contains("," + sKey))) {
				HashCampaign.put(CommonLib.CampaignName, HashCampaign.get(CommonLib.CampaignName) + "," + sKey);
			}
		} else {
			HashCampaign.put(CommonLib.CampaignName, sKey);
		}
		Hash.put(sKey, sValue);
	}

	public static String getHash(String sKey) {
		return Hash.get(sKey);
	}

	public static String removeHash(String sKey) {
		return Hash.remove(sKey);
	}

	public static boolean findHash(String sKey) {
		return Hash.containsKey(sKey);
	}

	public static String HashString() {
		return Hash.toString();
	}

	public static int Hashsize() {
		return Hash.size();
	}

}
