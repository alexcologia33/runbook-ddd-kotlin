package com.paucls.runbookDDD.application.runbook

import com.paucls.runbookDDD.domain.model.runbook.RunbookAggregate
import com.paucls.runbookDDD.persistence.RunbookRepository
import com.paucls.runbookDDD.persistence.nextIdentity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RunbookApplicationService @Autowired constructor(
        var runbookRepository: RunbookRepository
) {

    fun createRunbook(c: CreateRunbookCommand): String {
        val runbookId = runbookRepository.nextIdentity()

        val runbook = RunbookAggregate(runbookId, c.name, c.ownerId)
        runbookRepository.save(runbook)

        return runbookId
    }
}