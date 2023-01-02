/*
 */
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

/**
 * Définie comme point d'entré du projet et du jar ( voir "MainDemo" dans le
 * pom.xml ).
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
    static boolean debugSystemProperties = false;
    //
    
    public static final String CLI_OPT_O = "o";
    

    /**
     * Gestion des arguments de la ligne de commande avec commonCli.
     *
     * <p>
     * definir les arguments a utiliser : repertoire des fichier html cache
     * local , url du sujet ou idDuSujet? ou idComment? ou idUser?<br>
     * </p>
     *
     * <p>
     * </p>
     *
     * -o -out dirPath<br>
     * -url -url httpurl<br>
     *
     * -d domaine ex : www.li3d.fr<br>
     * -t topicId ex : 54<br>
     * -u userId ex 45000<br>
     *
     * -s sectionId ex <br>
     *
     *
     *
     * @param args
     */
    public static void main(String[] args) {

        Options options = new Options();

        try {
            boolean addCliOptions = true;
            if (addCliOptions) {

                options.addOption("v", "version", false, "version");

                //        options.addOption("t", false, "display current time");
                //        options.addOption("c", true, "country code");
                
                //        options.addOption("f", "file", true, "source file");
                //options.addRequiredOption("f","file", true, "source file");
                
                // Pour un eventuel fichier ou repertoire de destination des fichiers de sorties de notre application ...
                options.addOption(CLI_OPT_O, "out", true, "repertoire de destination");
                
                // Pour spécifier un id de topic sur le forum 
                options.addOption("t", "idTopic", true, "NOT IMPLEMENTED Id de Topic du forum");
                
                options.addOption("u", "url", true, "NOT IMPLEMENTED source url");
                
                options.addOption("cc", "clean-cache", false, "NOT IMPLEMENTED Pour recréer le repertoire de cache");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (true) {
            if (args.length == 0) {

                Class toUseAsRef = MainDemo.class;
                String sAppName = toUseAsRef.getSimpleName();
                if (true) {
                    System.out.printf("// %s (git version %s)\n", sAppName, GIT_VERSION);
                }

                HelpFormatter formatter = new HelpFormatter();
                if (true) {

                    File fSrcCodeSource = new java.io.File(toUseAsRef.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .getPath());
                    // output usage
                    formatter.printHelp(String.format("java -jar %3$s\n", sAppName, GIT_VERSION, fSrcCodeSource.toString()), options);
                    System.err.println("");
                }

            } else {
                if (true) {
                    for (String s : args) {
                        System.out.println("arg -> " + s);
                    }
                }
            }
            // create the parser for the command line arguments
            CommandLineParser parser = new DefaultParser();
            try {
                // parse the command line arguments
                CommandLine line = parser.parse(options, args);

                //
                //Retrieving the argument value
                //
                if (line.hasOption("v")) {
                    // output the version
                    System.out.println("// " + MainDemo.class.getSimpleName() + " version " + GIT_VERSION);
                    System.out.println("// " + MainDemo.class.getPackage().getName() + " version " + GIT_VERSION);
                    System.out.printf("// %s version %s\n",
                            new java.io.File(MainDemo.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName(),
                            GIT_VERSION
                    );

                    //
                    try {
                        String sysPropKeySunJavaCommand = "sun.java.command";
                        String commandeLine = System.getProperty(sysPropKeySunJavaCommand);
                        System.out.printf("%s : %s\n", sysPropKeySunJavaCommand, commandeLine);
                    } catch (Exception e) {
                        // ? Security Exception ?
                    }
                }

                if (line.hasOption(CLI_OPT_O)) {
                    String optionValue = line.getOptionValue(CLI_OPT_O);
                    System.out.println("output dir from cli: "+optionValue);
                    
                    // TODO revoir pour ne pas faire en static mais en passage d'argument ou setter.
                    ForumLI3DFR.baseDirOutput = optionValue;
                    
                }
                
                String countryCode = line.getOptionValue("c");
                if (countryCode == null) {
                    // print default date
                } else {
                    // print date for country specified by countryCode
                }

                if (line.hasOption("buildfile")) {
                    //this.buildfile = line.getOptionValue("buildfile");
                }

            } catch (ParseException exp) {
                // oops, something went wrong
                //System.err.println("Parsing failed.  Reason: " + exp.getMessage());
                exp.printStackTrace();
            } catch (Exception exp) {
                // oops, something went wrong
                //System.err.println("Parsing failed.  Reason: " + exp.getMessage());
                exp.printStackTrace();
            }

        }
        //
        if (debugSystemProperties) {
            outSystemProperties();
        }

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
     * TODO : <code>new java.io.File(SomeClassInYourJar.class.getProtectionDomain()
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

    /**
     * For debug : Output System.getProperties.
     */
    private static void outSystemProperties() {

        Properties properties = System.getProperties();
        if (false) {
            // Java 8
            properties.forEach((k, v) -> System.out.println(k + ":" + v));
        }

        if (false) {
            // Classic way to loop a map
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }

        if (false) {
            // No good, output is truncated, long lines end with ...
            properties.list(System.out);
        }

        if (true) {
            // Java 8
            LinkedHashMap<String, String> collect = properties.entrySet().stream()
                    .collect(Collectors.toMap(k -> (String) k.getKey(), e -> (String) e.getValue()))
                    .entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            collect.forEach((k, v) -> System.out.println(k + ":" + v));
        }
    }
}
