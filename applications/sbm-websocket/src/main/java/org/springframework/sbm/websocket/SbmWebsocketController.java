/*
 * Copyright 2021 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.sbm.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;

import java.nio.file.Path;

@Controller
@RequiredArgsConstructor
public class SbmWebsocketController {

  private final SbmService sbmService;

  @MessageMapping({"/scan"})
  @SendTo("/queue/recipes/applicable")
  public ScanResult scan(String message) throws Exception {
    Path rootPath = Path.of(message);
    ScanResult scanResult = sbmService.scan(rootPath);
    return scanResult;
  }

  @MessageMapping("/apply")
  @SendTo("/queue/migration/result")
  public RecipeExecutionResult apply(String recipeName) {
    return sbmService.apply(recipeName);
  }

  @SubscribeMapping("/topic/hello")
  public String sendOneTimeMessage() {
    return "server one-time message via the application";
  }

}