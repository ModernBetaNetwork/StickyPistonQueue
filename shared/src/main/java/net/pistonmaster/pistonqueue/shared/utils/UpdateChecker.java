/*
 * #%L
 * PistonQueue
 * %%
 * Copyright (C) 2021 AlexProgrammerDE
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package net.pistonmaster.pistonqueue.shared.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public final class UpdateChecker {
    private final Consumer<String> logger;
    private final int resourceId;

    public UpdateChecker(Consumer<String> logger, int resourceId) {
        this.logger = logger;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        try {
            URL url = URI.create("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "/").toURL();
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(false);
            huc.setConnectTimeout(5 * 1000);
            huc.setReadTimeout(5 * 1000);
            huc.setRequestMethod("GET");
            huc.connect();

            InputStream input = huc.getInputStream();
            Scanner scanner = new Scanner(input);
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
            }
        } catch (IOException exception) {
            logger.accept("Cannot look for updates: " + exception.getMessage());
        }
    }
}
