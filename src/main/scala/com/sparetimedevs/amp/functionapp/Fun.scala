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

package com.sparetimedevs.amp.functionapp

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.{AuthorizationLevel, FunctionName, HttpTrigger}

import java.util.Optional

class ScalaFunction {

  /** This function listens at endpoint "/api/scala-function"
    */
  @FunctionName("scala-function")
  def run(
      @HttpTrigger(
        name = "ScalaFunction",
        methods = Array(HttpMethod.GET, HttpMethod.POST),
        authLevel = AuthorizationLevel.ANONYMOUS
      )
      request: HttpRequestMessage[Optional[String]],
      context: ExecutionContext
  ): HttpResponseMessage = {
    context.getLogger.info("Scala HTTP trigger processed a request.")
    request.createResponseBuilder(HttpStatus.OK).body("This is written in Scala. Hello!").build
  }
}
