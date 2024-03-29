package cromwell.backend.google.pipelines.common

import akka.actor.ActorSystem
import com.google.api.client.http.HttpRequestInitializer
import com.typesafe.config.{Config, ConfigFactory}
import cromwell.backend.BackendConfigurationDescriptor
import cromwell.backend.google.pipelines.common.api.{PipelinesApiFactoryInterface, PipelinesApiRequestFactory}
import cromwell.backend.standard.StandardAsyncJob
import cromwell.cloudsupport.gcp.GoogleConfiguration
import cromwell.core.WorkflowOptions
import cromwell.core.filesystem.CromwellFileSystems
import cromwell.core.logging.JobLogger
import cromwell.core.path.PathBuilder

import scala.concurrent.Await
import scala.concurrent.duration._

object PipelinesApiTestConfig {

  private val PapiBackendConfigString =
    """
      |project = "my-cromwell-workflows"
      |root = "gs://my-cromwell-workflows-bucket"
      |
      |genomics {
      |  auth = "application-default"
      |  endpoint-url = "https://lifesciences.googleapis.com/"
      |  location = "us-central1"
      |}
      |
      |filesystems.gcs.auth = "application-default"
      |
      |request-workers = 1
      |
      |default-runtime-attributes {
      |    cpu: 1
      |    failOnStderr: false
      |    continueOnReturnCode: 0
      |    docker: "ubuntu:latest"
      |    memory: "2048 MB"
      |    bootDiskSizeGb: 10
      |    disks: "local-disk 10 SSD"
      |    noAddress: false
      |    preemptible: 0
      |    zones:["us-central1-b", "us-central1-a"]
      |}
      |
      |""".stripMargin

  private val NoDefaultsConfigString =
    """
      |project = "my-cromwell-workflows"
      |root = "gs://my-cromwell-workflows-bucket"
      |
      |genomics {
      |  auth = "application-default"
      |  endpoint-url = "https://genomics.googleapis.com/"
      |}
      |
      |filesystems {
      |  gcs {
      |    auth = "application-default"
      |  }
      |}
      |""".stripMargin

  private val PapiGlobalConfigString =
    s"""
       |google {
       |  application-name = "cromwell"
       |  auths = [
       |    {
       |      name = mock
       |      scheme = mock
       |    }
       |    {
       |      # legacy `application-default` auth that actually just mocks
       |      name = "application-default"
       |      scheme = "mock"
       |    }
       |  ]
       |}
       |
       |filesystems {
       |  gcs {
       |    class = "cromwell.filesystems.gcs.GcsPathBuilderFactory"
       |  }
       |}
       |
       |backend {
       |  default = "JES"
       |  providers {
       |    JES {
       |      actor-factory = "cromwell.backend.google.pipelines.common.PipelinesApiBackendLifecycleActorFactory"
       |      config {
       |      $PapiBackendConfigString
       |      }
       |    }
       |  }
       |}
       |
       |""".stripMargin

  val PapiBackendConfig: Config = ConfigFactory.parseString(PapiBackendConfigString)
  val PapiGlobalConfig: Config = ConfigFactory.parseString(PapiGlobalConfigString)
  val PapiBackendNoDefaultConfig: Config = ConfigFactory.parseString(NoDefaultsConfigString)
  val PapiBackendConfigurationDescriptor: BackendConfigurationDescriptor = {
    new BackendConfigurationDescriptor(PapiBackendConfig, PapiGlobalConfig) {
      override private[backend] lazy val cromwellFileSystems = new CromwellFileSystems(PapiGlobalConfig)
    }
  }
  val NoDefaultsConfigurationDescriptor: BackendConfigurationDescriptor =
    BackendConfigurationDescriptor(PapiBackendNoDefaultConfig, PapiGlobalConfig)
  val genomicsFactory: PipelinesApiFactoryInterface = new PipelinesApiFactoryInterface {
    override def build(httpRequestInitializer: HttpRequestInitializer): PipelinesApiRequestFactory = {
      new PipelinesApiRequestFactory {
        override def cancelRequest(job: StandardAsyncJob) = throw new UnsupportedOperationException
        override def getRequest(job: StandardAsyncJob) = throw new UnsupportedOperationException
        override def runRequest(createPipelineParameters:
                                PipelinesApiRequestFactory.CreatePipelineParameters,
                                jobLogger: JobLogger,
                               ) = throw new UnsupportedOperationException
      }
    }
    override def usesEncryptedDocker: Boolean = false
  }
  def pathBuilders()(implicit as: ActorSystem): List[PathBuilder] =
    Await.result(PapiBackendConfigurationDescriptor.pathBuilders(WorkflowOptions.empty), 5.seconds)
  val googleConfiguration: GoogleConfiguration = GoogleConfiguration(PapiGlobalConfig)
  val papiAttributes: PipelinesApiConfigurationAttributes =
    PipelinesApiConfigurationAttributes(googleConfiguration, PapiBackendConfig, "papi")
  val papiConfiguration = new PipelinesApiConfiguration(PapiBackendConfigurationDescriptor, genomicsFactory, googleConfiguration, papiAttributes)
}
