package hackoverviewer;

import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IHttpListener;
import burp.IHttpRequestResponse;
import burp.IRequestInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DemoExtender implements IHttpListener {

    IBurpExtenderCallbacks extenderCallbacks;
    IExtensionHelpers extenderHelpers;
    IRequestInfo requestInfo;
    String domainName;
    String url;
    String hash;
    
    public static IBurpExtenderCallbacks callbacks;
    public static DemoExtenderGui demoExtenderGui;
    public static String extensionName = "HO Callback";
    
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
     if(toolFlag == IBurpExtenderCallbacks.TOOL_PROXY && messageIsRequest == true)
        requestInfo = extenderHelpers.analyzeRequest(messageInfo);
        domainName = requestInfo.getUrl().getHost(); 
        url = requestInfo.getUrl().toString(); 
        hash = demoExtenderGui.getHash();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", "curl -d '{\"domain\":\""+domainName+"\", \"url\":\""+url+"\", \"hash\":\""+hash+"\"}' -H \"Content-Type: application/json\" -X POST "+demoExtenderGui.getURL());
            Process process = processBuilder.start();
        } catch (IOException ex) {

        }
    }
    
    public DemoExtender(IBurpExtenderCallbacks callbacks) {   
        extenderCallbacks = callbacks;
        extenderHelpers = callbacks.getHelpers();
    }  
}
