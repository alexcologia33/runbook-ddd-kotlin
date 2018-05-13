package com.paucls.runbookDDD.domain.model.runbook

import com.paucls.runbookDDD.domain.model.AggregateRoot
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

/**
 * The root entity of the Runbook aggregate - Aggregate Root.
 */
@RedisHash("Runbook")
class Runbook(
        @Id
        val runbookId: String,
        val name: String,
        val ownerId: String
) : AggregateRoot() {

    companion object {
        val OPEN = "OPEN"
        val COMPLETED = "COMPLETED"
    }

    private var status = OPEN

    val tasks: MutableMap<String, Task> = HashMap()

    fun isCompleted() = status == COMPLETED

    fun addTask(taskId: String, name: String, description: String, assigneeId: String?) {
        tasks[taskId] = Task(taskId, name, description, assigneeId)

        registerEvent(TaskAdded(runbookId, taskId))

        if (assigneeId != null) {
            registerEvent(TaskAssigned(runbookId, taskId, assigneeId, name))
        }
    }

    fun assignTask(taskId: String, assigneeId: String, userId: String) {
        getTask(taskId).assign(assigneeId)

        registerEvent(TaskAssigned(runbookId, taskId, assigneeId, getTask(taskId).name))
    }

    fun startTask(taskId: String, userId: String) {
        getTask(taskId).startTask(userId)
    }

    fun completeTask(taskId: String, userId: String) {
        getTask(taskId).completeTask()
    }

    fun rejectTask(taskId: String, userId: String) {
        getTask(taskId).rejectTask()
    }

    fun completeRunbook(userId: String) {
        if (userId != ownerId) {
            throw RunbookOwnedByDifferentUserException()
        }

        val hasPendingTask = tasks.values.any { !it.isCompleted() }
        if (hasPendingTask) {
            throw RunBookWithPendingTasksException()
        }

        status = COMPLETED
    }

    private fun getTask(taskId: String) = tasks[taskId] ?: throw NonExistentTaskException()
}
