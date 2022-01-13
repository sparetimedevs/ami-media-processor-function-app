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

package com.sparetimedevs.ami.mediaprocessor.functionapp.test

import cats.effect.IO
import com.sparetimedevs.ami.MediaProcessor
import com.sparetimedevs.ami.mediaprocessor.Errors
import com.sparetimedevs.ami.mediaprocessor.file.Format

class MediaProcessorTestImpl extends MediaProcessor {

  override def createImages(musicXmlData: Array[Byte], outputFileFormat: Format): IO[Either[Errors, Map[String, Array[Byte]]]] =
    IO.pure(Right(Map(("part-p1", testImageData))))

}
