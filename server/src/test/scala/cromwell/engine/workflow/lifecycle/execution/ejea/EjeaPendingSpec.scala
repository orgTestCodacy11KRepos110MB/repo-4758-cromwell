package cromwell.engine.workflow.lifecycle.execution.ejea

import com.typesafe.config.ConfigFactory
import cromwell.backend.BackendConfigurationDescriptor
import cromwell.backend.TestConfig.globalConfig
import cromwell.engine.workflow.lifecycle.execution.job.EngineJobExecutionActor._
import cromwell.engine.workflow.tokens.JobTokenDispenserActor.JobTokenRequest
import org.scalatest.concurrent.Eventually

class EjeaPendingSpec extends EngineJobExecutionActorSpec with CanValidateJobStoreKey with Eventually {

  override implicit val stateUnderTest: EngineJobExecutionActorState = Pending

  "An EJEA in the Pending state" should {

    List(false, true) foreach { restarting =>
      s"wait for the Execute signal then request an execution token (with restarting=$restarting)" in {
        ejea = helper.buildEJEA(restarting = restarting)
        ejea ! Execute

        if (restarting) {
          helper.jobRestartCheckTokenDispenserProbe.expectMsgClass(max = awaitTimeout, classOf[JobTokenRequest])
        } else {
          helper.jobExecutionTokenDispenserProbe.expectMsgClass(max = awaitTimeout, classOf[JobTokenRequest])

          helper.jobPreparationProbe.msgAvailable should be(false)
          helper.jobStoreProbe.msgAvailable should be(false)
          ejea.stateName should be(RequestingExecutionToken)
        }
      }

      s"should use system-level hog-factor setting in token request, if hog-factor is not overridden in backend configuration (with restarting=$restarting)" in {
        ejea = helper.buildEJEA(restarting = restarting)
        ejea ! Execute

        if (restarting) {
          helper.jobRestartCheckTokenDispenserProbe.expectMsgClass(max = awaitTimeout, classOf[JobTokenRequest])
        } else {
          val tokenRequest = helper.jobExecutionTokenDispenserProbe.expectMsgClass(max = awaitTimeout, classOf[JobTokenRequest])
          // 1 is the default hog-factor value defined in reference.conf
          tokenRequest.jobTokenType.hogFactor should be(1)
        }
      }

      s"should use hog-factor defined in backend configuration in token request (with restarting=$restarting)" in {
        val expectedHogFactorValue = 123
        val overriddenHogFactorAttributeString = s"hog-factor: $expectedHogFactorValue"
        val backendWithOverriddenHogFactorConfigDescriptor = BackendConfigurationDescriptor(backendConfig = ConfigFactory.parseString(overriddenHogFactorAttributeString), globalConfig)
        ejea = helper.buildEJEA(restarting = restarting, backendConfigurationDescriptor = backendWithOverriddenHogFactorConfigDescriptor)
        ejea ! Execute

        if (restarting) {
          helper.jobRestartCheckTokenDispenserProbe.expectMsgClass(max = awaitTimeout, classOf[JobTokenRequest])
        } else {
          val tokenRequest = helper.jobExecutionTokenDispenserProbe.expectMsgClass(max = awaitTimeout, classOf[JobTokenRequest])
          tokenRequest.jobTokenType.hogFactor should be(expectedHogFactorValue)
        }
      }
    }
  }
}
