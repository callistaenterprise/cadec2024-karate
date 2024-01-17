package system

import com.intuit.karate.gatling.PreDef._
import io.gatling.core.Predef._
import scala.concurrent.duration._

class SystemSimulation extends Simulation {

  val protocol = karateProtocol()
  protocol.nameResolver = (req, ctx) => req.getHeader("karate-name")
  protocol.runner.karateEnv("performance")

  val productScenario = scenario("Karate Product Scenario")
    .exec(karateFeature(
      "classpath:system/Product.feature@Performance")
    )

  setUp(
    productScenario.inject(rampUsers(100).during(5))
      .protocols(protocol)
  )
}
