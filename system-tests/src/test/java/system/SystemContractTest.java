package system;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import com.trivago.cluecumber.core.CluecumberCore;
import com.trivago.cluecumber.engine.exceptions.CluecumberException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SystemContractTest {

    @Test
    void testSystem() throws CluecumberException {
        Results results = Runner
            .path("classpath:system")
            .outputCucumberJson(true)
            .outputHtmlReport(true)
            .outputJunitXml(true)
            .parallel(1);
        new CluecumberCore.Builder().build()
            .generateReports(
            "target/karate-reports",
            "target/cluecumber-reports");
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }

}
