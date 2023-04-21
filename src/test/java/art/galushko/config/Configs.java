package art.galushko.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Configs {
    public static Config config = ConfigFactory.load();

    public static class UNIVERSITY {
        private static final Config universityConfig = config.getConfig("university.api");
        public static String HOST = universityConfig.getString("host");
        public static String INFO_ENDPOINT = universityConfig.getString("endpoints.info");
        public static String SEARCH_ENDPOINT = universityConfig.getString("endpoints.search");
        public static String UPDATE_ENDPOINT = universityConfig.getString("endpoints.update");
    }
}
