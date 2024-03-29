package cromwell.services.instrumentation.impl.statsd

import akka.testkit.{TestActorRef, TestProbe}
import cats.data.NonEmptyList
import com.typesafe.config.ConfigFactory
import cromwell.core.TestKitSuite
import cromwell.services.instrumentation.InstrumentationService.InstrumentationServiceMessage
import cromwell.services.instrumentation._
import cromwell.services.instrumentation.impl.statsd.StatsDInstrumentationServiceActor._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.Eventually
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

import scala.jdk.CollectionConverters._
import scala.concurrent.duration._

class StatsDInstrumentationServiceActorBenchmarkSpec extends TestKitSuite with AnyFlatSpecLike with BeforeAndAfterAll with Matchers with Eventually {
  behavior of "StatsDInstrumentationServiceActor"

  val config = ConfigFactory.parseString(
    """
      |hostname = "localhost"
      |port = 8125
      |prefix = "prefix_value" # can be used to prefix all metrics with an api key for example
      |flush-rate = 100 ms # rate at which aggregated metrics will be sent to statsd
    """.stripMargin
  )

  val registryProbe = TestProbe().ref
  override implicit val patienceConfig: PatienceConfig = PatienceConfig(scaled(3.seconds))
  val testBucket = CromwellBucket(List("test_prefix"), NonEmptyList.of("test", "metric", "benchmark", "bucket"))


  it should "have good throughput for gauges" in {
    val instrumentationActor = TestActorRef(new StatsDInstrumentationServiceActor(config, ConfigFactory.load(), registryProbe))
    val gaugeName = instrumentationActor.underlyingActor.metricBaseName.append(testBucket.toStatsDString()).name
    LazyList.range(0, 1 * 1000 * 1000, 1).foreach({ i =>
      instrumentationActor ! InstrumentationServiceMessage(CromwellGauge(testBucket, i.toLong))
    })
    eventually {
      instrumentationActor.underlyingActor.metricRegistry.getGauges.asScala.get(gaugeName).map(_.getValue) shouldBe Some(999999)
    }
  }
}
