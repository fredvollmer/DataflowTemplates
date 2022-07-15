/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.syndeo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.beam.sdk.schemas.SchemaTranslation;
import org.apache.beam.sdk.schemas.transforms.SchemaTransformProvider;
import org.apache.beam.syndeo.common.ProtoTranslation;
import org.apache.beam.syndeo.common.ProviderUtil;
import org.apache.beam.syndeo.v1.SyndeoV1.SchemaTransformConfigurations;
import org.apache.beam.syndeo.v1.SyndeoV1.SchemaTransformDescription;

public class ConfigGen {

  public static void main(String[] args) {

    String fileName = "/Users/laraschmidt/Documents/beam2/config_gen/beam/transforms_metadata.txt";

    SchemaTransformConfigurations.Builder config = SchemaTransformConfigurations.newBuilder();

    for (SchemaTransformProvider p : ProviderUtil.getProviders()) {
      SchemaTransformDescription.Builder builder = SchemaTransformDescription.newBuilder();
      builder.setConfigurationOptions(
          ProtoTranslation.toSyndeoProtos(
              SchemaTranslation.schemaToProto(p.configurationSchema(), true)));
      builder.setTransformUrn(p.identifier());
      builder.addAllInputs(p.inputCollectionNames());
      builder.addAllOutputs(p.outputCollectionNames());
      config.addTransforms(builder.build());
    }

    try {
      File output = new File(fileName);
      FileOutputStream file = new FileOutputStream(output);
      config.build().writeTo(file);
      file.close();
      System.out.println("Wrote to " + output.getName());
    } catch (IOException e) {
      System.out.println("An error occurred.");
    }
  }
}
