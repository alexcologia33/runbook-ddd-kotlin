package com.paucls.runbookDDD.application.runbook

import com.paucls.runbookDDD.application.EmailSender
import com.paucls.runbookDDD.domain.model.runbook.TaskAssigned
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

/**
 * Demo event handler to test how Spring Data AbstractAggregateRoot entities dispatch events.
 */
@Component
class TaskAssignedHandler(val emailSender: EmailSender) {

    private val logger = LoggerFactory.getLogger(TaskAssignedHandler::class.java)

    @TransactionalEventListener
    fun handle(event: TaskAssigned) {
        logger.info(">> Handling event: $event")

        emailSender.sendEmail(event.assigneeId, "Task Assigned", "The task '${event.name}' has been assigned to you!")
    }

}
