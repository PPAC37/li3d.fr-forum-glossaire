/*
 */
package com.ppac37.EText2022;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * reprise de
 * https://github.com/MarginallyClever/Makelangelo-software/blob/master/src/main/java/com/marginallyclever/util/PropertiesFileHelper.java
 *
 * @author q6
 */
public final class UtilPropertiesFile {

    private static final Logger logger = LoggerFactory.getLogger(UtilPropertiesFile.class);
    private static final String APP_PROPERTIES_FILENAME = "app.properties";
    private static final String GIT_PROPERTIES_FILENAME = "git.properties";

    /**
     * @return version number in the form of vX.Y.Z where X is MAJOR, Y is MINOR
     * version, and Z is PATCH See <a href="http://semver.org/">Semantic
     * Versioning 2.0.0</a>
     */
    public static String getAppVersion() {
        final Properties prop = loadProperties(APP_PROPERTIES_FILENAME, false);
        String version = prop.getProperty("app.version");
        logger.trace("version: {}", version);
        return version;
    }

    /**
     * returns version git read from the file git.properties produced by the
     * maven plugin git-commit-id-maven-plugin
     *
     * @return version git
     */
    public static String getGitVersion() {
        final Properties prop = loadProperties(GIT_PROPERTIES_FILENAME, true);
        String fullGitRevision = "dirty";
        if (prop.getProperty("git.branch") != null) {
            fullGitRevision = prop.getProperty("git.branch") + "-" + prop.getProperty("git.commit.id.abbrev");
            if ("true".equals(prop.getProperty("git.dirty"))) {
                fullGitRevision += "-dirty";
            }
        }

        logger.trace("fullGitRevision: {}", fullGitRevision);
        return fullGitRevision;
    }

    private static Properties loadProperties(String filename, boolean optionnal) throws IllegalStateException {
        final Properties prop = new Properties();
        try (final InputStream input = MainDemo.class.getClassLoader().getResourceAsStream(filename)) {
            if (!optionnal && input == null) {
                throw new IllegalStateException("unable to find " + filename);
            }
            if (input != null) {
                //load a properties file from class path
                prop.load(input);
            }

        } catch (IllegalStateException | IOException ex) {
            logger.error("Failed to load {}", filename, ex);
        }
        return prop;
    }
}
