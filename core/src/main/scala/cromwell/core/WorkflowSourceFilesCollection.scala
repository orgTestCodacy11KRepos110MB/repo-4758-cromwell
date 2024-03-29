package cromwell.core

import wom.core._

/**
  * Represents the collection of source files that a user submits to run a workflow
  */

sealed trait WorkflowSourceFilesCollection {
  def workflowSource: Option[WorkflowSource]
  def workflowUrl: Option[WorkflowUrl]
  def workflowRoot: Option[String]
  def inputsJson: WorkflowJson
  def workflowOptions: WorkflowOptions
  def labelsJson: WorkflowJson
  def workflowType: Option[WorkflowType]
  def workflowTypeVersion: Option[WorkflowTypeVersion]
  def workflowOnHold: Boolean
  def requestedWorkflowId: Option[WorkflowId]

  def warnings: Seq[String]

  def importsZipFileOption: Option[Array[Byte]] = this match {
    case _: WorkflowSourceFilesWithoutImports => None
    case w: WorkflowSourceFilesWithDependenciesZip => Option(w.importsZip) // i.e. Some(importsZip) if our wiring is correct
  }

  def setOptions(workflowOptions: WorkflowOptions) = {

    this match {
      case w: WorkflowSourceFilesWithoutImports => w.copy(workflowOptions = workflowOptions)
      case w: WorkflowSourceFilesWithDependenciesZip => w.copy(workflowOptions = workflowOptions)
    }
  }
}

trait HasWorkflowIdAndSources {
  def sources: WorkflowSourceFilesCollection
  def id: WorkflowId
}

object WorkflowSourceFilesCollection {
  def apply(workflowSource: Option[WorkflowSource],
            workflowUrl: Option[WorkflowUrl],
            workflowRoot: Option[String],
            workflowType: Option[WorkflowType],
            workflowTypeVersion: Option[WorkflowTypeVersion],
            inputsJson: WorkflowJson,
            workflowOptions: WorkflowOptions,
            labelsJson: WorkflowJson,
            importsFile: Option[Array[Byte]],
            workflowOnHold: Boolean,
            warnings: Seq[String],
            requestedWorkflowId: Option[WorkflowId]): WorkflowSourceFilesCollection = importsFile match {
    case Some(imports) =>
      WorkflowSourceFilesWithDependenciesZip(
        workflowSource = workflowSource,
        workflowUrl = workflowUrl,
        workflowRoot = workflowRoot,
        workflowType = workflowType,
        workflowTypeVersion = workflowTypeVersion,
        inputsJson = inputsJson,
        workflowOptions = workflowOptions,
        labelsJson = labelsJson,
        importsZip = imports,
        workflowOnHold = workflowOnHold,
        warnings = warnings,
        requestedWorkflowId = requestedWorkflowId)
    case None =>
      WorkflowSourceFilesWithoutImports(
        workflowSource = workflowSource,
        workflowUrl = workflowUrl,
        workflowRoot = workflowRoot,
        workflowType = workflowType,
        workflowTypeVersion = workflowTypeVersion,
        inputsJson = inputsJson,
        workflowOptions = workflowOptions,
        labelsJson = labelsJson,
        workflowOnHold = workflowOnHold,
        warnings = warnings,
        requestedWorkflowId = requestedWorkflowId)
  }
}

final case class WorkflowSourceFilesWithoutImports(workflowSource: Option[WorkflowSource],
                                                   workflowUrl: Option[WorkflowUrl],
                                                   workflowRoot: Option[String],
                                                   workflowType: Option[WorkflowType],
                                                   workflowTypeVersion: Option[WorkflowTypeVersion],
                                                   inputsJson: WorkflowJson,
                                                   workflowOptions: WorkflowOptions,
                                                   labelsJson: WorkflowJson,
                                                   workflowOnHold: Boolean = false,
                                                   warnings: Seq[String],
                                                   requestedWorkflowId: Option[WorkflowId]) extends WorkflowSourceFilesCollection

final case class WorkflowSourceFilesWithDependenciesZip(workflowSource: Option[WorkflowSource],
                                                        workflowUrl: Option[WorkflowUrl],
                                                        workflowRoot: Option[String],
                                                        workflowType: Option[WorkflowType],
                                                        workflowTypeVersion: Option[WorkflowTypeVersion],
                                                        inputsJson: WorkflowJson,
                                                        workflowOptions: WorkflowOptions,
                                                        labelsJson: WorkflowJson,
                                                        importsZip: Array[Byte],
                                                        workflowOnHold: Boolean = false,
                                                        warnings: Seq[String],
                                                        requestedWorkflowId: Option[WorkflowId]) extends WorkflowSourceFilesCollection {
  override def toString = {
    s"WorkflowSourceFilesWithDependenciesZip($workflowSource, $workflowUrl, $workflowType, $workflowTypeVersion," +
      s""" $inputsJson, ${workflowOptions.asPrettyJson}, $labelsJson, <<ZIP BINARY CONTENT>>, $warnings)"""
  }
}
