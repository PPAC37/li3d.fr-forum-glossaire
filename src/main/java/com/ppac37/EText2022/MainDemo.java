package com.ppac37.EText2022;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/*
 */
/**
 * Définie comme point d'entré du projet et du jar ( voir "MainDemo" dans le
 * pom.xml ).
 *
 * Truc java qui dit Coucou.<br>
 * A implémenter : gestion des arguments ... .
 *
 * @author q6
 */
public class MainDemo {

    /**
     * Defined in uses Maven's resource filtering to update the VERSION based
     * upon pom.xml. In this way we only define the VERSION once and prevent
     * violating DRY.
     */
    public static final String GIT_VERSION = UtilPropertiesFile.getGitVersion();
    /**
     * Defined in src/resources/app.properties.
     */
    public static final String VERSION = UtilPropertiesFile.getAppVersion();

    //
    static boolean someBooleanFlag = true;

    // 
    public static void main(String[] args) {
        System.out.println("Coucou !");

// TODO commonCli gestion des arguments
        // definir les arguments a utiliser
        // repertoire des fichier html cache local 
        // url du sujet
        //
        //Creating the Options
        //
        // create Options object
        Options options = new Options();

        // add v option
        options.addOption("v", "version", false, "version");

        // add nolf option
//        options.addOption("nolf", "nolookandfeel", false, "disable java LookAndFeel usage.");
        // add t option
//        options.addOption("t", false, "display current time");
        //
        //International Time option
        //
        // add c option
//        options.addOption("c", true, "country code");
        //
        // add src option
//        options.addOption("f", "file", true, "source file");
        //options.addRequiredOption("f","file", true, "source file");
//
        // add src option
//        options.addOption("o", "out", true, "dest dir");
        //options.addRequiredOption("f","file", true, "source file");
        //
        // add src option
//        options.addOption("u", "url", true, "source url");
        //options.addRequiredOption("f","file", true, "source file");
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(MainDemo.class.getSimpleName(), options);

        //
        //Parsing the command line arguments
        //
        // create the parser
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            //
            //Retrieving the argument value
            //
            // 
            if (line.hasOption("v") || true) {
                // output the version
                System.out.println(MainDemo.class.getSimpleName() + " version " + GIT_VERSION);
                System.out.println(MainDemo.class.getPackage().getName() + " version " + GIT_VERSION);
                System.out.printf("%s version %s\n", new java.io.File(MainDemo.class.getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .getPath())
                        .getName(), GIT_VERSION);

                String sVersion = GIT_VERSION;
                Class toUseAsRef = MainDemo.class;
                String sAppName = toUseAsRef.getSimpleName();

                File fSrcCodeSource = new java.io.File(toUseAsRef.getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .getPath());
                System.out.printf("%s version %s (%s)\n", toUseAsRef.getSimpleName(), sVersion, fSrcCodeSource.toString());
                System.out.printf("%s version %s (%s)\n", toUseAsRef.getSimpleName(), sVersion, fSrcCodeSource.getName());

                System.out.printf("Usage : java -jar %3$s\n", toUseAsRef.getSimpleName(), sVersion, fSrcCodeSource.toString());

                //
                //
                //
                try {
                    String sysPropKeySunJavaCommand = "sun.java.command";
                    String commandeLine = System.getProperty(sysPropKeySunJavaCommand);
                    System.out.printf("%s : %s\n", sysPropKeySunJavaCommand, commandeLine);

                } catch (Exception e) {
                    // ? Security Exception ?
                }
            }

            // get c option value
            String countryCode = line.getOptionValue("c");

            if (countryCode == null) {
                // print default date
            } else {
                // print date for country specified by countryCode
            }

            // has the buildfile argument been passed?
            if (line.hasOption("buildfile")) {
                // initialise the member variable
                //this.buildfile = line.getOptionValue("buildfile");
            }

        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }

        //
        //Retrieving the argument value
        //
        //
        //
        //
        //
        // Savoir si l'on travail avec des fichier en cache ou avec la version en ligne du sujet du glossaire
        // BF
        // avoir un fichier de configuration genre .ini qui permte de proposer des valeur pr defauts et un fichier de configuration .cfg pour les veaulr de l'utilisateur
        // Obtenir le contenus en ligne d'aprés d'une URL et le parser avec JSoup
        // html to md
        // md to html
        // diff
        if (true) {
            ForumLI3DFR.main(args);
        }

    }

    /**
     * https://stackoverflow.com/questions/11158235/get-name-of-executable-jar-from-within-main-method
     * TODO : * <code>new java.io.File(SomeClassInYourJar.class.getProtectionDomain()
     * .getCodeSource()
     * .getLocation()
     * .getPath())
     * .getName()</code>
     *
     * @param cls
     * @return
     */
    private String getPath(Class cls) {
        String cn = cls.getName();
        String rn = cn.replace('.', '/') + ".class";
        String path
                = getClass().getClassLoader().getResource(rn).getPath();
        int ix = path.indexOf("!");
        if (ix >= 0) {
            return path.substring(0, ix);
        } else {
            return path;
        }
    }

    //
    public static void mainDisplaySystemProperties(String[] args) {

        Properties properties = System.getProperties();
        // Java 8
//        properties.forEach((k, v) -> System.out.println(k + ":" + v));

        // Classic way to loop a map
        //for (Map.Entry<Object, Object> entry : properties.entrySet()) {
        //    System.out.println(entry.getKey() + " : " + entry.getValue());
        //}
        // No good, output is truncated, long lines end with ...
        //properties.list(System.out);
        // Thanks Java 8
        LinkedHashMap<String, String> collect = properties.entrySet().stream()
                .collect(Collectors.toMap(k -> (String) k.getKey(), e -> (String) e.getValue()))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        collect.forEach((k, v) -> System.out.println(k + ":" + v));
    }
}
