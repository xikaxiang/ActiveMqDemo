package examples.utils;

public class EnvHelper {

	public static String env(String key, String defaultValue) {
		String rc = System.getenv(key);
		if (rc == null)
			return defaultValue;
		return rc;
	}

	/**
	 * 根据index 到 args 获取参数值
	 * @param args
	 * @param index
	 * @param defaultValue
	 * @return
	 */
	public static String arg(String[] args, int index, String defaultValue) {
		if (index < args.length)
			return args[index];
		else
			return defaultValue;
	}
}
