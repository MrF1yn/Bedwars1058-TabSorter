package dev.mrflyn.bw1058tabsorter;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

public class ConfigManager {

    private Logger logger;
    private String path;
    private YamlConfiguration config;

    public ConfigManager(){
        this.logger = BW1058TabSorter.plugin.getLogger();
        this.path = "plugins/BedWars1058/Addons/Bedwars1058-TabSorter/";
    }

    public void init(){
        saveResource("config.yml", false);
        config = YamlConfiguration.loadConfiguration(new File(this.path+"config.yml"));
    }

    public YamlConfiguration getConfig(){
        return config;
    }

    public InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        } else {
            try {
                URL url = this.getClass().getClassLoader().getResource(filename);
                if (url == null) {
                    return null;
                } else {
                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);
                    return connection.getInputStream();
                }
            } catch (IOException var4) {
                return null;
            }
        }
    }

    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = this.getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in ");
            } else {
                File outFile = new File(path, resourcePath);
                int lastIndex = resourcePath.lastIndexOf(47);
                File outDir = new File(path, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }

                try {
                    if (outFile.exists() && !replace) {
                        this.logger.info("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                    } else {
                        OutputStream out = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];

                        int len;
                        while((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    }
                } catch (IOException var10) {
                    this.logger.info("Could not save " + outFile.getName() + " to " + outFile);
                    var10.printStackTrace();
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }

}
