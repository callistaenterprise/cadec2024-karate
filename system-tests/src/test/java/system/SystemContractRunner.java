package system;

import com.intuit.karate.junit5.Karate;

class SystemContractRunner {

    @Karate.Test
    Karate testProduct() {
        return Karate
            .run("Product")
            .relativeTo(getClass());
    }

    @Karate.Test
    Karate testUserJourney() {
        return Karate
            .run("UserJourney")
            .relativeTo(getClass());
    }
}
