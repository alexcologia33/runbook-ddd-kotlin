package com.paucls.runbookDDD.domain.model.runbook

import com.paucls.runbookDDD.domain.model.DomainException

class CanOnlyCompleteInProgressTaskException : DomainException()

class RunbookOwnedByDifferentUserException : DomainException()

class RunBookWithPendingTasksException : DomainException()

class TaskAssignedToDifferentUserException : DomainException()
