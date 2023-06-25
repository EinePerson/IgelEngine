package de.igelstudios.igelengine.common.io;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import de.igelstudios.ClientMain;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class EngineSettings {
    private String main;

    private String name;

    private String build;

    public static EngineSettingsParser parser(String fileName) {
        return new EngineSettingsParser(fileName);
    }

    public String getBuild() {
        return this.build;
    }

    public String getName() {
        return this.name;
    }

    public String getMain() {
        return this.main;
    }

    public static class EngineSettingsParser {
        private String fileName;

        public EngineSettingsParser(String fileName) {
            this.fileName = fileName;
        }

        public EngineSettings read() {
            EngineSettings settings;
            try {
                settings = new EngineSettings();
                JsonReader reader = (new Gson()).newJsonReader(new InputStreamReader(Objects.<InputStream>requireNonNull(ClientMain.class.getClassLoader().getResourceAsStream(this.fileName))));
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    EngineSettings.class.getDeclaredField(name).set(settings, reader.nextString());
                }
                reader.endObject();
            } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return settings;
        }
    }
}
