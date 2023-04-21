package art.galushko.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;

@Tag("API")
@Slf4j
public class ApiTests {
    @BeforeEach
    public void logTestNameAtStart(TestInfo testInfo) {
        log.info(testInfo.getDisplayName() + " started!");
    }

    @AfterEach
    public void logTestNameAtFinish(TestInfo testInfo) {
        log.info(testInfo.getDisplayName() + " finished!");
    }
}
