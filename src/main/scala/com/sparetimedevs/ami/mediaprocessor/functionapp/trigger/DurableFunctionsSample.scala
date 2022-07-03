/*
 * Copyright (c) 2021 sparetimedevs and respective authors and developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparetimedevs.ami.mediaprocessor.functionapp.trigger

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import com.microsoft.azure.functions.annotation.{AuthorizationLevel, BlobInput, BlobOutput, FunctionName, HttpTrigger, StorageAccount}
import com.microsoft.azure.functions.{ExecutionContext, HttpMethod, HttpRequestMessage, HttpResponseMessage, HttpStatus, OutputBinding}
import com.microsoft.durabletask.azurefunctions.{DurableActivityTrigger, DurableClientContext, DurableClientInput, DurableOrchestrationTrigger}
import com.microsoft.durabletask.{DurableTaskClient, OrchestrationRunner, TaskOrchestrationContext}
import com.sparetimedevs.ami.MediaProcessor
import com.sparetimedevs.ami.mediaprocessor.file.Format
import com.sparetimedevs.ami.mediaprocessor.functionapp.DependencyModuleImpl
import com.sparetimedevs.ami.mediaprocessor.{Errors, ValidationError, XmlParseError}

import java.nio.charset.StandardCharsets
import java.util.Optional

class DurableFunctionsSample(private val mediaProcessor: MediaProcessor) {

  def this() = this(DependencyModuleImpl.mediaProcessor)

  /** This HTTP-triggered function starts the orchestration.
    */
  @FunctionName("StartHelloCities")
  def startHelloCities(
      @HttpTrigger(name = "req", methods = Array(HttpMethod.POST)) req: HttpRequestMessage[Optional[String]],
      @DurableClientInput(name = "durableContext") durableContext: DurableClientContext,
      context: ExecutionContext
  ): HttpResponseMessage = {
    val client = durableContext.getClient
    val instanceId = client.scheduleNewOrchestrationInstance("HelloCities")
    context.getLogger.info("Created new Java orchestration with instance ID = " + instanceId)
    durableContext.createCheckStatusResponse(req, instanceId)
  }

  /** This is the orchestrator function, which can schedule activity functions, create durable timers, or wait for external events in a way that's completely fault-tolerant. The
    * OrchestrationRunner.loadAndRun() static method is used to take the function input and execute the orchestrator logic.
    */
  @FunctionName("HelloCities")
  def helloCitiesOrchestrator(@DurableOrchestrationTrigger(name = "runtimeState") runtimeState: String): String =
    OrchestrationRunner.loadAndRun(
      runtimeState,
      (ctx: TaskOrchestrationContext) => {
        def foo(ctx: TaskOrchestrationContext) = {
          var result = ""
          result += ctx.callActivity("SayHello", "Tokyo", classOf[String]).await + ", "
          result += ctx.callActivity("SayHello", "London", classOf[String]).await + ", "
          result += ctx.callActivity("SayHello", "Seattle", classOf[String]).await
          result
        }

        foo(ctx)
      }
    )

  /** This is the activity function that gets invoked by the orchestrator function.
    */
  @FunctionName("SayHello")
  def sayHello(@DurableActivityTrigger(name = "name") name: String): String =
    String.format("Hello %s!", name)

}
