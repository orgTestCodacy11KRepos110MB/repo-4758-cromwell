package cwl.preprocessor

import better.files.File
import cats.data.NonEmptyList
import common.assertion.CromwellTimeoutSpec
import common.mock.MockSugar
import cwl.preprocessor.CwlPreProcessor.SaladFunction
import io.circe.Printer
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CwlPreProcessorSpec extends AnyFlatSpec with CromwellTimeoutSpec with Matchers with MockSugar {
  behavior of "CwlPreProcessor"

  private val resourcesRoot = File(getClass.getResource(".").getPath)
  private val echoFileTool = CwlFileReference(resourcesRoot / "echo_tool.cwl", None)

  it should "flatten a simple file" in {
    validate(makeTestRoot("simple_workflow"), None) { mockSaladingFunction =>
      verify(mockSaladingFunction).apply(echoFileTool)
    }
  }

  it should "flatten file with a self reference" in {
    validate(makeTestRoot("self_reference"), Option("echo-workflow-2")) { _ => }
  }

  /*
    * A "valid" cyclic dependency means for example
    * file_1
    *   - workflow_1 -> depends on workflow_3
    *   - workflow_2
    * file_2
    *   - workflow_3 -> depends on workflow_2
    * workflow_1 (in file_1) references a workflow in file_2 that references a workflow in file_1,
    * but that's ok since the cycle is only over the file, not the workflows themselves.
   */
  it should "flatten file with sub workflow, self reference and valid cyclic dependency" in {
    val testRoot = makeTestRoot("complex_workflow")
    val subWorkflow =  CwlFileReference(testRoot / "sub" / "sub_workflow.cwl", Option("sub-echo-workflow-1"))

    validate(testRoot, Option("echo-workflow-2")) { mockSaladingFunction =>
      verify(mockSaladingFunction).apply(echoFileTool)
      verify(mockSaladingFunction).apply(subWorkflow)
    }
  }

  it should "detect invalid cyclic dependencies in the same file and fail" in {
    val testRoot = makeTestRoot("same_file_cyclic_dependency")

    validate(testRoot, Option("echo-workflow-2"),
      expectedFailure = Option(
        NonEmptyList.one(s"Found a circular dependency on file://$testRoot/root_workflow.cwl#echo-workflow-2")
      )
    ) { _ => }
  }

  it should "detect invalid transitive cyclic dependencies (A => B => C => A) and fail" in {
    val testRoot = makeTestRoot("transitive_cyclic_dependency")

    val subWorkflow1 = CwlFileReference(testRoot / "sub_workflow_1.cwl", None)
    val subWorkflow2 =  CwlFileReference(testRoot / "sub_workflow_2.cwl", None)

    validate(testRoot, None,
      expectedFailure = Option(
        NonEmptyList.one(s"Found a circular dependency on file://$testRoot/root_workflow.cwl")
      )
    ) { mockSaladingFunction =>
      verify(mockSaladingFunction).apply(subWorkflow1)
      verify(mockSaladingFunction).apply(subWorkflow2)
    }
  }

  it should "pre-process inlined workflows" in {
    val testRoot = makeTestRoot("deep_nesting")

    val subWorkflow1 = CwlFileReference(testRoot / "wc-tool.cwl", None)
    val subWorkflow2 =  CwlFileReference(testRoot / "parseInt-tool.cwl", None)

    validate(testRoot, None, uuidExtractor = Option("step0/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})/")) { mockSaladingFunction =>
      verify(mockSaladingFunction).apply(subWorkflow1)
      verify(mockSaladingFunction).apply(subWorkflow2)
    }
  }

  private def makeTestRoot(testDirectoryName: String) = resourcesRoot / testDirectoryName

  def validate[T](testRoot: File,
                  root: Option[String],
                  expectedFailure: Option[NonEmptyList[String]] = None,
                  uuidExtractor: Option[String] = None
                 )(additionalValidation: SaladFunction => T): Unit = {
    val rootWorkflowReference = CwlFileReference(testRoot / "root_workflow.cwl", root)

    // Mocking the salad function allows us to validate how many times it is called exactly and with which parameters
    val mockSaladingFunction = mock[SaladFunction]
    when(mockSaladingFunction.apply(any[CwlReference])) thenAnswer {
      invocationOnMock =>
        CwlPreProcessor.saladCwlFile(invocationOnMock.getArgument[CwlReference](0))
    }
    val preProcessor = new CwlPreProcessor(mockSaladingFunction)

    val process = preProcessor.preProcessCwl(rootWorkflowReference).value.unsafeRunSync()
    // Asserts that dependencies are only saladed once and exactly once
    verify(mockSaladingFunction).apply(rootWorkflowReference)
    additionalValidation(mockSaladingFunction)

    (process, expectedFailure) match {
      case (Left(errors), Some(failures)) => errors shouldBe failures
      case (Left(errors), None) => fail("Unexpected failure to pre-process workflow: " + errors.toList.mkString(", "))
      case (Right(result), None) =>
        val content = (testRoot / "expected_result.json").contentAsString
        val uuid = uuidExtractor.flatMap(_.r.findFirstMatchIn(result.printWith(Printer.noSpaces)).map(_.group(1)))
        val expectationContent = content
          .replaceAll("<<RESOURCES_ROOT>>", resourcesRoot.pathAsString)
          .replaceAll("<<RANDOM_UUID>>", uuid.getOrElse(""))

        result shouldBe io.circe.parser.parse(expectationContent).getOrElse(fail("Failed to parse expectation. Your test is broken !"))
      case (Right(_), Some(failures)) => fail("Unexpected success to pre-process workflow, was expecting failures: " + failures.toList.mkString(", "))
    }
    ()
  }
}
