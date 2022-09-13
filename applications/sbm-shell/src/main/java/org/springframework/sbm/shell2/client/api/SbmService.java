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

package org.springframework.sbm.shell2.client.api;

import org.springframework.sbm.engine.recipe.Answer;
import org.springframework.sbm.shell2.client.events.UserInputRequestedEvent;
import org.springframework.sbm.shell2.client.events.RecipeExecutionCompletedEvent;
import org.springframework.sbm.shell2.client.events.RecipeExecutionProgressUpdateEvent;
import org.springframework.sbm.shell2.client.events.ScanCompletedEvent;
import org.springframework.sbm.shell2.client.events.ScanProgressUpdatedEvent;

import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Fabian Krüger
 */
public interface SbmService {
    void scan(Path projectRoot,
              Consumer<ScanProgressUpdatedEvent> scanProgressUpdatedEventConsumer,
              Consumer<ScanCompletedEvent> scanCompletedEventConsumer);

    void apply(String selectedRecipe,
               Consumer<RecipeExecutionProgressUpdateEvent> recipeExecutionProgressUpdateEventConsumer,
               Consumer<RecipeExecutionCompletedEvent> recipeExecutionCompletedEventConsumer,
               Function<UserInputRequestedEvent, Answer> userInputRequestedEventConsumer);
}
