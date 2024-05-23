package Tocode.in.italent.digital;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BuildToc {

    public static void main(String[] args) {
        // Specify the path to your HTML file
        String filePath = "C:\\workspacesolving\\tochtml\\toc.html";
        System.out.println("filepath:"+filePath);

        try {
            // Read HTML content from file
            String htmlContent = readHtmlFromFile(filePath);
             System.out.println("html content:"+htmlContent);
            // Convert HTML content to StringBuilder
            StringBuilder content = new StringBuilder(htmlContent);

            // Call the buildMainToc method with the netproFlag and content parameters
            StringBuilder toc = buildMainToc("true", content);

            // Print or use the generated TOC
            System.out.println(toc.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readHtmlFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static StringBuilder buildMainToc(String netproFlag, StringBuilder content) {
        StringBuilder toc = new StringBuilder("<div id=\"tg-body\"><div id=\"support-toc\"><h2>Contents</h2>");
        try {
            org.jsoup.nodes.Document htmlDocument = Jsoup.parse(content.toString(), "", Parser.xmlParser());
            htmlDocument.outputSettings().prettyPrint(false).indentAmount(0);
            Elements tocList = htmlDocument.select("ul").isEmpty() ? new Elements() : htmlDocument.select("ul").first().children();
            if (tocList.size() > 0 && !tocList.first().select("a").isEmpty() && tocList.first().select("a").first().attr("href").contains("toc-hId")) {
                for (Element element : tocList) {
                    String hrefValue = element.select("a").first().attr("href").split("#")[1];
                    String tagName = htmlDocument.select("#" + hrefValue).first().tagName();
                    if (tagName != null) {
                        switch (tagName.toLowerCase()) {
                            case "h2":
                                toc.append("<div class=\"toc-h2\"><a href=\"#").append(hrefValue).append("\">")
                                        .append(element.select("a").first().text()).append("</a></div>");
                                break;
                            case "h3":
                                toc.append("<div class=\"toc-h3\"><a href=\"#").append(hrefValue).append("\">")
                                        .append(element.select("a").first().text()).append("</a></div>");
                                break;
                            case "h4":
                                toc.append("<div class=\"toc-h4\"><a href=\"#").append(hrefValue).append("\">")
                                        .append(element.select("a").first().text()).append("</a></div>");
                                break;
                            default:
                                break;
                        }
                    }
                }
                toc.append("</div>");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while generating the TOC: " + e.toString());
            e.printStackTrace();
        }

        if (netproFlag != null && netproFlag.equalsIgnoreCase("true")) {
            toc.append("<div class=\"toc-h2\"><a href=\"#comm-anc\">Related Cisco Support Community Discussions</a></div>");
        }

        toc.append("</div>");

        return toc;
    }
}




















//package Tocode.in.italent.digital;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Element;
//import org.jsoup.parser.Parser;
//import org.jsoup.select.Elements;
//
//public class BuildToc {
//
//    public static void main(String[] args) {
//        String htmlContent = "html><body><div class=\"lia-message-template-summary-zone\"><div class=\"lia-message-template-content-zone\"><p><ul =""=""><li style=\"list-style-type:disc; margin-left:15px; margin-bottom:1px;\"><a href=\"https://techzone.cisco.com/t5/AMP-for-Endpoints/Troubleshoot-Secure-Endpoint-Connector-Uninstall-Methods/ta-p/11093975#toc-hId--1148888468\">Introduction</a></li><li style=\"list-style-type:disc; margin-left:15px; margin-bottom:1px;\"><a href=\"https://techzone.cisco.com/t5/AMP-for-Endpoints/Troubleshoot-Secure-Endpoint-Connector-Uninstall-Methods/ta-p/11093975#toc-hId--1541915478\">Prerequisites</a></li><li style=\"list-style-type:disc; margin-left:30px; margin-bottom:1px;\"><a href=\"https://techzone.cisco.com/t5/AMP-for-Endpoints/Troubleshoot-Secure-Endpoint-Connector-Uninstall-Methods/ta-p/11093975#toc-hId--1609346264\">Requirements</a></li><li style=\"list-style-type:disc; margin-left:30px; margin-bottom:1px;\"><a href=\"https://techzone.cisco.com/t5/AMP-for-Endpoints/Troubleshoot-Secure-Endpoint-Connector-Uninstall-Methods/ta-p/11093975#toc-hId--2002373274\">Components Used</a></li><li style=\"list-style-type:disc; margin-left:15px; margin-bottom:1px;\"><a href=\"https://techzone.cisco.com/t5/AMP-for-Endpoints/Troubleshoot-Secure-Endpoint-Connector-Uninstall-Methods/ta-p/11093975#toc-hId-1770484293\">Background Information</a></li><li style=\"list-style-type:disc; margin-left:15px; margin-bottom:1px;\"><a href=\"https://techzone.cisco.com/t5/AMP-for-Endpoints/Troubleshoot-Secure-Endpoint-Connector-Uninstall-Methods/ta-p/11093975#toc-hId-1724711640\">Uninstall Methods</a></li><li style=\"list-style-type:disc; margin-left:30px; margin-bottom:1px;\"><a href=\"https://techzone.cisco.com/t5/AMP-for-Endpoints/Troubleshoot-Secure-Endpoint-Connector-Uninstall-Methods/ta-p/11093975#toc-hId-1038281623\">Manually Uninstall</a></li><li style=\"list-style-type:disc; margin-left:30px; margin-bottom:1px;\"><a href=\"https://techzone.cisco.com/t5/AMP-for-Endpoints/Troubleshoot-Secure-Endpoint-Connector-Uninstall-Methods/ta-p/11093975#toc-hId-841768118\">Uninstall Connector from Secure Endpoint console.</a></li><li style=\"list-style-type:disc; margin-left:30px; margin-bottom:1px;\"><a href=\"https://techzone.cisco.com/t5/AMP-for-Endpoints/Troubleshoot-Secure-Endpoint-Connector-Uninstall-Methods/ta-p/11093975#toc-hId-448741108\">Uninstall Connector Using APIs</a></li><li style=\"list-style-type:disc; margin-left:30px; margin-bottom:1px;\"><a href=\"https://techzone.cisco.com/t5/AMP-for-Endpoints/Troubleshoot-Secure-Endpoint-Connector-Uninstall-Methods/ta-p/11093975#toc-hId-55714098\">Uninstall Connector Using Command Line Switches</a></li><li style=\"list-style-type:disc; margin-left:15px; margin-bottom:1px;\"><a href=\"https://techzone.cisco.com/t5/AMP-for-Endpoints/Troubleshoot-Secure-Endpoint-Connector-Uninstall-Methods/ta-p/11093975#toc-hId-152603600\">Related Information</a></li></ul></p><h2 id=\"toc-hId--952374963\">&nbsp;</h2><h2 id=\"toc-hId--1148888468\">Introduction</h2><p>This document describes the process to uninstall a Cisco Secure Endpoint (CSE) connector installed on Windows devices with different methods.</p><h2 id=\"toc-hId--1345401973\">&nbsp;</h2><h2 id=\"toc-hId--1541915478\">Prerequisites</h2><p>&nbsp;</p><h3 id=\"toc-hId--1609346264\">Requirements</h3><p>&nbsp;</p><p>Cisco recommends that you have knowledge of these topics:</p><p>&nbsp;</p><ul>\r\n" + 
//        		"<li>Secure Endpoint Connector</li>\r\n" + 
//        		"<li>Secure Endpoint Console</li>\r\n" + 
//        		"<li>Secure Endpoint APIs</li>\r\n" + 
//        		"</ul><h3 id=\"toc-hId--1805859769\">&nbsp;</h3><h3 id=\"toc-hId--2002373274\">Components Used</h3><p>&nbsp;</p><p>The information in this document is based on these software and hardware versions:</p><p>&nbsp;</p><ul>\r\n" + 
//        		"<li>Secure Endpoint console version v5.4.2024042415</li>\r\n" + 
//        		"<li>Secure Endpoint Windows connector version v8.2.3.30119</li>\r\n" + 
//        		"<li>Secure Endpoint API v3</li>\r\n" + 
//        		"</ul><p>&nbsp;</p><p>The information in this document was created from the devices in a specific lab environment. All of the devices used in this document started with a cleared (default) configuration. If your network is live, ensure that you understand the potential impact of any command.</p><h2 id=\"toc-hId-1966997798\">&nbsp;</h2><h2 id=\"toc-hId-1770484293\">Background Information</h2><p>&nbsp;</p><p>The procedure described in this document is helpful in situations where you are looking to uninstall the Secure Endpoint connector.</p><p>&nbsp;</p><p>Uninstalling the connector is an option to get rid of the connector altogether, either for fresh installations or simply not having the connector on a Windows device anymore.</p><h2 id=\"toc-hId-1573970788\">&nbsp;</h2><h2 id=\"toc-hId-1724711640\">Uninstall Methods</h2><p>&nbsp;</p><p>Once you want to uninstall the Secure Endpoint connector on a Windows computer, follow the mehtod that better suits your needs.</p><h3 id=\"toc-hId-1234795128\">&nbsp;</h3><h3 id=\"toc-hId-1038281623\">Manually Uninstall</h3><p>&nbsp;</p><p>In order to uninstall a connector locally.</p><p><br />Step 1. In the device, navigate to <strong>Program Files &gt; Cisco &gt; AMP &gt; x </strong>(Where x is the version of the CSE connector).</p><p>&nbsp;</p><p>Step 2. Locate the&nbsp;<strong>uninstall.exe</strong> file. As shown in the image.</p><p>&nbsp;</p><p><span class=\"lia-inline-image-display-wrapper lia-image-align-inline\" image-alt=\"uninstall.exe.png\" style=\"width: 526px;\"><img src=\"/c/dam/en/us/support/docs/security/secure-endpoint/221959-troubleshoot-secure-endpoint-connector-u-00.png\" alt=\"uninstall.exe\" aria-label=\"uninstall.exe\" /></span></p><p><br />Step 3. Execute the file and follow the wizard until getting the Uninstallation Complete screen.&nbsp;As shown in the image.</p><p>&nbsp;</p><p><span class=\"lia-inline-image-display-wrapper lia-image-align-inline\" image-alt=\"uninstall complete.png\" style=\"width: 469px;\"><img src=\"/c/dam/en/us/support/docs/security/secure-endpoint/221959-troubleshoot-secure-endpoint-connector-u-01.png\" alt=\"uninstall complete\" aria-label=\"uninstall complete\" /></span></p></div></div></body></html>";
//        // Convert HTML content to StringBuilder
//        StringBuilder content = new StringBuilder(htmlContent);
//
//        // Call the buildMainToc method with the netproFlag and content parameters
//        StringBuilder toc = buildMainToc("true", content);
//
//        // Print or use the generated TOC
//        System.out.println(toc.toString());
//    }
//
//    public static StringBuilder buildMainToc(String netproFlag, StringBuilder content) {
//        StringBuilder toc = new StringBuilder("<div id=\"tg-body\"><div id=\"support-toc\"><h2>Contents</h2>");
//        try {
//            org.jsoup.nodes.Document htmlDocument = Jsoup.parse(content.toString(), "", Parser.xmlParser());
//            htmlDocument.outputSettings().prettyPrint(false).indentAmount(0);
//            Elements tocList = htmlDocument.select("ul").isEmpty() ? new Elements(): htmlDocument.select("ul").first().children();
//            if (tocList.size() > 0 && !tocList.first().select("a").isEmpty()
//                    && tocList.first().select("a").first().attr("href").contains("toc-hId")) {
//                for (Element element : tocList) {
//                    String hrefValue = element.select("a").first().attr("href").split("#")[1];
//                    String tagName = htmlDocument.select("#" + hrefValue).first().tagName();
//                    if (tagName != null) {
//                        switch (tagName.toLowerCase()) {
//                            case "h2":
//                                toc.append("<div class=\"toc-h2\"><a href=\"#").append(hrefValue).append("\">")
//                                        .append(element.select("a").first().text()).append("</a></div>");
//                                break;
//                            case "h3":
//                                toc.append("<div class=\"toc-h3\"><a href=\"#").append(hrefValue).append("\">")
//                                        .append(element.select("a").first().text()).append("</a></div>");
//                                break;
//                            case "h4":
//                                toc.append("<div class=\"toc-h4\"><a href=\"#").append(hrefValue).append("\">")
//                                        .append(element.select("a").first().text()).append("</a></div>");
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                }
//                toc.append("</div>");
//            }
//        } catch (Exception e) {
//            System.out.println("Error occurred while generating the TOC: " + e.toString());
//            e.printStackTrace();
//        }
//
//        if (netproFlag != null && netproFlag.equalsIgnoreCase("true")) {
//            toc.append("<div class=\"toc-h2\"><a href=\"#comm-anc\">Related Cisco Support Community Discussions</a></div>");
//        }
//
//        toc.append("</div>");
//
//        return toc;
//    }
//}
