package me.waicool20.cpu;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateChecker {
    private URL projectFilePage;
    private String latestVersion;
    private String dlLink;
    private static UpdateChecker updateChecker = null;

    private UpdateChecker() {
        try {
            this.projectFilePage = new URL("http://dev.bukkit.org/bukkit-plugins/cpu/files.rss");
        } catch (MalformedURLException e) {
            CPUPlugin.logger.severe("[CPU] Invalid URL! Could not connect to update server!");
        }
    }

    public static UpdateChecker getInstance(){
        if(updateChecker == null){
            updateChecker = new UpdateChecker();
            return updateChecker;
        }
        return updateChecker;
    }

    public boolean NewUpdateAvailable(){
        try {
            InputStream inputStream = projectFilePage.openConnection().getInputStream();
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);

            Node latestFile = document.getElementsByTagName("item").item(0);
            NodeList versionInfo = latestFile.getChildNodes();

            this.latestVersion = versionInfo.item(1).getTextContent();
            int latestVersionNumber = Integer.parseInt(latestVersion.replaceAll("[a-zA-Z. ]", ""));
            int currentVersion = Integer.parseInt(CPUPlugin.pdfFile.getVersion().replaceAll("[a-zA-Z. ]", ""));
            this.dlLink = versionInfo.item(3).getTextContent();

            if(latestVersionNumber > currentVersion){
                return true;
            }

        } catch (IOException e) {
            CPUPlugin.logger.severe("[CPU] Could not connect to update server!");
        } catch (SAXException e) {
            CPUPlugin.logger.severe("[CPU] Could not get updates!");
        } catch (ParserConfigurationException e) {
            CPUPlugin.logger.severe("[CPU] Could not get updates!");
        }
        return false;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getDlLink() {
        return dlLink;
    }
}
