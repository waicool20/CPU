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
    private int latestVersion;
    private String dlLink;

    public UpdateChecker() {
        try {
            this.projectFilePage = new URL("http://dev.bukkit.org/bukkit-plugins/cpu/files.rss");
        } catch (MalformedURLException e) {
            CPU.logger.severe("[CPU] Invalid URL! Could not connect to update server!");
        }
    }

    public boolean NewUpdateAvailable(){
        try {
            InputStream inputStream = projectFilePage.openConnection().getInputStream();
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);

            Node latestFile = document.getElementsByTagName("item").item(0);
            NodeList versionInfo = latestFile.getChildNodes();

            this.latestVersion = Integer.parseInt(versionInfo.item(1).getTextContent().replaceAll("[a-zA-Z. ]", ""));
            int currentVersion = Integer.parseInt(CPU.pdfFile.getVersion().replaceAll("[a-zA-Z. ]", ""));
            this.dlLink = versionInfo.item(3).getTextContent();

            if(latestVersion > currentVersion){
                return true;
            }

        } catch (IOException e) {
            CPU.logger.severe("[CPU] Could not connect to update server!");
        } catch (SAXException e) {
            CPU.logger.severe("[CPU] Could not get updates!");
        } catch (ParserConfigurationException e) {
            CPU.logger.severe("[CPU] Could not get updates!");
        }
        return false;
    }

    public int getLatestVersion() {
        return latestVersion;
    }

    public String getDlLink() {
        return dlLink;
    }
}
