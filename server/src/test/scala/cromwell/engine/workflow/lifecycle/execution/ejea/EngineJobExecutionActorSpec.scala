package cromwell.engine.workflow.lifecycle.execution.ejea

import akka.actor.Actor
import akka.testkit.{DefaultTimeout, ImplicitSender, TestFSMRef}
import com.typesafe.config.{Config, ConfigFactory}
import cromwell.backend.BackendJobExecutionActor
import cromwell.backend.BackendJobExecutionActor.BackendJobExecutionActorCommand
import cromwell.core.TestKitSuite
import cromwell.core.callcaching._
import cromwell.engine.workflow.lifecycle.execution.job.EngineJobExecutionActor._
import org.scalatest._
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration._
import scala.language.postfixOps

trait EngineJobExecutionActorSpec extends AbstractEngineJobExecutionActorSpec
  with Matchers with BeforeAndAfterAll with BeforeAndAfter {

  // If we WANT something to happen, make sure it happens within this window:
  val awaitTimeout: FiniteDuration = 10 seconds
  // If we want something to NOT happen, make sure it doesn't happen within this window. This is almost nothing on
  // the basis that the thing we're awaiting would probably have happened by the time the call was made. Otherwise choose
  // another time!
  val awaitAlmostNothing: FiniteDuration = 100 milliseconds

  val allowMultipleCacheCycles = false

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = scaled(awaitTimeout), interval = scaled(awaitAlmostNothing))

  // The default values for these are "null". The helper is created in "before", the ejea is up to the test cases
  private[ejea] var helper: PerTestHelper = _
  private[ejea] var ejea: TestFSMRef[EngineJobExecutionActorState, EJEAData, MockEjea] = _
  implicit def stateUnderTest: EngineJobExecutionActorState

  before {
    helper = new PerTestHelper
  }
  after {
    List(
      ("FetchCachedResultsActor", helper.fetchCachedResultsActorCreations),
      ("JobHashingActor", helper.jobHashingInitializations),
      ("CallCacheInvalidateActor", helper.invalidateCacheActorCreations)) foreach {
      case (name, GotTooMany(list)) if !allowMultipleCacheCycles => fail(s"Too many $name creations (${list.size})")
      case _ => // Fine.
    }

    if (ejea != null) system.stop(ejea)
  }

  // Some helper lists
  val CallCachingModes = List(CallCachingOff, CallCachingActivity(ReadCache), CallCachingActivity(WriteCache), CallCachingActivity(ReadAndWriteCache))
  case class RestartOrExecuteCommandTuple(operationName: String, restarting: Boolean, expectedMessageToBjea: BackendJobExecutionActorCommand)
  val RestartOrExecuteCommandTuples = List(
    RestartOrExecuteCommandTuple("execute", restarting = false, BackendJobExecutionActor.ExecuteJobCommand),
    RestartOrExecuteCommandTuple("restart", restarting = true, BackendJobExecutionActor.RecoverJobCommand))
}

object EngineJobExecutionActorSpec {
  implicit class EnhancedTestEJEA[S,D,T <: Actor](me: TestFSMRef[S, D, T]) {
    // Like setState, but mirrors back the EJEA (for easier inlining)
    def setStateInline(state: S = me.stateName, data: D = me.stateData): TestFSMRef[S, D, T] = {
      me.setState(state, data)
      me
    }
  }
}

object AbstractEngineJobExecutionActorSpec {
  val ConfigText: String =
    """
      |akka {
      |  loggers = ["akka.testkit.TestEventListener"]
      |  loglevel = "INFO"
      |  actor {
      |    debug {
      |       receive = on
      |    }
      |  }
      |  dispatchers {
      |    # A dispatcher for actors performing blocking io operations
      |    # Prevents the whole system from being slowed down when waiting for responses from external resources for instance
      |    io-dispatcher {
      |      type = Dispatcher
      |      executor = "fork-join-executor"
      |      # Using the forkjoin defaults, this can be tuned if we wish
      |    }
      |
      |    # A dispatcher for actors handling API operations
      |    # Keeps the API responsive regardless of the load of workflows being run
      |    api-dispatcher {
      |      type = Dispatcher
      |      executor = "fork-join-executor"
      |    }
      |
      |    # A dispatcher for engine actors
      |    # Because backends behavior is unpredictable (potentially blocking, slow) the engine runs
      |    # on its own dispatcher to prevent backends from affecting its performance.
      |    engine-dispatcher {
      |      type = Dispatcher
      |      executor = "fork-join-executor"
      |    }
      |
      |    # A dispatcher used by supported backend actors
      |    backend-dispatcher {
      |      type = Dispatcher
      |      executor = "fork-join-executor"
      |    }
      |
      |    # Note that without further configuration, backend actors run on the default dispatcher
      |  }
      |  test {
      |    # Some of our tests fire off a message, then expect a particular event message within 3s (the default).
      |    # Especially on CI, the metadata test does not seem to be returning in time. So, overriding the timeouts
      |    # with slightly higher values. Alternatively, could also adjust the akka.test.timefactor only in CI.
      |    filter-leeway = 10s
      |    single-expect-default = 5s
      |    default-timeout = 10s
      |  }
      |}
      |
      |services {}
    """.stripMargin

  private lazy val testConfig = ConfigFactory.parseString(ConfigText)
}

abstract class AbstractEngineJobExecutionActorSpec extends TestKitSuite
  with DefaultTimeout with ImplicitSender with Matchers with ScalaFutures with Eventually with Suite
  with OneInstancePerTest with BeforeAndAfterAll with AnyWordSpecLike {
  override protected lazy val actorSystemConfig: Config = AbstractEngineJobExecutionActorSpec.testConfig
}
