package de.hdm_stuttgart.mi.dad.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class ConfigHandler {
    final String DIRECTORY_OF_PROFILES = ".dbgrep/profiles";

    public Path getDefaultProfile() throws IOException {
        try (Stream<Path> stream = Files.list(getDirectory())) {
            List<Path> profiles = stream
                    //TODO Filterung mit verschiedenen Dateitypen wie .cnf
                    .filter(file -> !Files.isDirectory(file))
                    .toList();

            long fileCount = profiles.size();

            if (fileCount == 0) {
                //TODO Errornachrichten System erstellen? Enums?
                throw new IllegalArgumentException("Im Ordner " + DIRECTORY_OF_PROFILES +
                        " existiert keine File. Bitte erstellen Sie ein Profil in diesem Ordner.");
            }
            if (fileCount > 1) {
                //TODO Auf jeden Fall Formatierung xD
                throw new IllegalArgumentException("Es sind mehrere Profile im Ordner " + DIRECTORY_OF_PROFILES +
                        " vorhanden. Bitte wählen Sie mit dem Befehl '--profile' ein Profil aus den hier" +
                        " aufgelisteten Profilen heraus: \n" + getStringOfProfileList(profiles));
            }
            return profiles.get(0);
        }
    }

    public Path getSelectedProfile(String fileName) {
        Path pathOfProfile = getDirectory().resolve(fileName);
        if (Files.exists(pathOfProfile) && !Files.isDirectory(pathOfProfile)) {
            return pathOfProfile;
        }
        //TODO eigene Exception schreiben?
        throw new IllegalArgumentException("Die angegebene File" + fileName + "existiert nicht im Ordner " +
                DIRECTORY_OF_PROFILES + ".");
    }

    private Path getDirectory() {
        return Paths.get(DIRECTORY_OF_PROFILES);
    }

    private String getStringOfProfileList(List<Path> profiles){
        StringBuilder profileList = new StringBuilder();
        for (Path profile : profiles){
            profileList.append(profile.getFileName().toString());
            profileList.append("\n");
        }
        return profileList.toString();
    }

}
