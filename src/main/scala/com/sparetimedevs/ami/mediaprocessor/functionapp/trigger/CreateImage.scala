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

import com.microsoft.azure.functions.HttpStatus
import com.microsoft.azure.functions.annotation.{AuthorizationLevel, FunctionName, HttpTrigger}

import java.util.Optional

class CreateImage {

  /** This function listens at endpoint "/api/create-image"
    */
  @FunctionName("create-image")
  def createImage(
      @HttpTrigger(
        name = "createImageTrigger",
        methods = Array(HttpMethod.PUT),
        authLevel = AuthorizationLevel.ANONYMOUS
      )
      request: HttpRequestMessage[Optional[String]],
      @BlobInput(
        name = "file",
        dataType = "binary",
        path = "java-functions-container/azurefxicon.png",
        connection = "AzureWebJobsStorage"
      )
      content: Array[Byte],
      context: ExecutionContext
  ): HttpResponseMessage = {
    // The function will be triggered through an HTTP PUT request and should be idempotent. Should return 202 and URL to pull for result. The query param is the file identifier.
    // Add a Blob storage input binding and a Blob storage output binding.
    // See: https://medium.com/geekculture/azure-function-a-httptriger-with-a-blobinput-in-java-ea6939199e7b
    // And: https://github.com/Gogetter/azure-blog-posts/blob/main/java-azure-function/src/main/java/dev/etimbuk/functions/FileDownloadHttpTriggerFunction.java
    //
    // Find out what the best "things" are to work with...
    // probably not File as input (because blob to file isn't a thing, right, it is not on the filesystem)
    // probably byteArray as output. Or is base64 encoded string also fine?
    //

    context.getLogger.info("FileDownloadHttpTrigger processed a request.")

    if (content != null && content.length > 0) return request.createResponseBuilder(HttpStatus.OK).body(content).build
    else return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("The searched file does not exist.").build

    context.getLogger.info("Scala HTTP trigger processed a request.")
    request
      .createResponseBuilder(HttpStatus.ACCEPTED)
      .body("{ \"status\" : \"ACCEPTED\", \"url\": \"http://sparetimedevs.com\" }")
      .build //TODO or not with body but with location header.
  }
}
