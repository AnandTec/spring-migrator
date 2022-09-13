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
package org.springframework.sbm.shell2.server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.sbm.engine.events.ActionStartedEvent;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.shell2.ScanProgressUpdate;
import org.springframework.sbm.shell2.ScanResult;
import org.springframework.sbm.shell2.server.events.*;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * @author Fabian Krüger
 */
@Service
@RequiredArgsConstructor
public class SbmServiceImpl implements SbmService {

    @Override
    public void scan(Path projectRoot, Consumer<ScanProgressUpdatedEvent> scanProgressUpdatedEventConsumer, Consumer<ScanCompletedEvent> scanCompletedEventConsumer) {
        // TODO: remove simulation of scan
        new Thread(() -> {
            IntStream.range(0, 10).forEach(i-> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                scanProgressUpdatedEventConsumer.accept(new ScanProgressUpdatedEvent(new ScanProgressUpdate()));
            });
            scanCompletedEventConsumer.accept(new ScanCompletedEvent(new ScanResult(
                    List.of(
                            Recipe.builder().name("recipe-1").build(),
                            Recipe.builder().name("recipe-2").build())
                    )
                )
            );
        }).run();
    }

    @Override
    public void apply(String selectedRecipe, Consumer<RecipeExecutionProgressUpdateEvent> recipeExecutionProgressUpdateEventConsumer, Consumer<RecipeExecutionCompletedEvent> recipeExecutionCompletedEventConsumer) {
        // TODO: remove simulation of apply
        new Thread(() -> {
            IntStream.range(0, 10).forEach(i-> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                recipeExecutionProgressUpdateEventConsumer.accept(new RecipeExecutionProgressUpdateEvent());
            });
            recipeExecutionCompletedEventConsumer.accept(new RecipeExecutionCompletedEvent(new RecipeExecutionResult(selectedRecipe)));
        }).run();
    }

}
