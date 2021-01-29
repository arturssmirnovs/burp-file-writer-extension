package hackoverviewer;

import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IHttpListener;
import burp.IHttpRequestResponse;
import burp.IParameter;
import burp.IRequestInfo;
import burp.IResponseInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.simple.JSONObject;

public final class DemoExtender implements IHttpListener {

    IBurpExtenderCallbacks extenderCallbacks;
    IExtensionHelpers extenderHelpers;
    IRequestInfo requestInfo;
    IResponseInfo responseInfo;
    
    String folder;
    String domainName;
    String url;
    
    public static IBurpExtenderCallbacks callbacks;
    public static String extensionName = "File Writer";
    
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
        
     if(toolFlag == IBurpExtenderCallbacks.TOOL_PROXY && messageIsRequest == true)
        requestInfo = extenderHelpers.analyzeRequest(messageInfo);
     
        responseInfo = extenderHelpers.analyzeResponse(messageInfo.getResponse());
        
        domainName = requestInfo.getUrl().getHost(); 
        url = requestInfo.getUrl().toString(); 
        
        
        JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("domain", domainName);
        requestJsonObject.put("url", url);
        requestJsonObject.put("method", requestInfo.getMethod());
        requestJsonObject.put("headers", requestInfo.getHeaders().toString());

        JSONObject responseJsonObject = new JSONObject();
        responseJsonObject.put("status", String.valueOf(responseInfo.getStatusCode()));
        responseJsonObject.put("cookie", String.valueOf(responseInfo.getCookies()));
        responseJsonObject.put("headers", String.valueOf(responseInfo.getHeaders()));
        responseJsonObject.put("status", String.valueOf(responseInfo.getStatusCode()));

        JSONObject json = new JSONObject();
        json.put("request", requestJsonObject);
        json.put("response", responseJsonObject);


        byte[] byte_Request = messageInfo.getResponse();
        byte[] byte_body = Arrays.copyOfRange(byte_Request, responseInfo.getBodyOffset(), byte_Request.length);//not length-1
        String body = new String(byte_body);
            
        String path = folder;
        
        // create folder if don't exists
        createFolder(path+"/"+domainName);
        
        callbacks.printOutput(domainName);

        
        
        String filePath = url.replace(requestInfo.getUrl().getProtocol()+"://", "").replace(":"+requestInfo.getUrl().getPort(), "");
        
        callbacks.printOutput(filePath);

        // write request
        writeToFile(path+"/"+filePath, body);
        
        // write data of request
        writeToFile(path+"/"+filePath+".data", json.toJSONString());
    }
    
    public DemoExtender(IBurpExtenderCallbacks callbacks) {   
        extenderCallbacks = callbacks;
        extenderHelpers = callbacks.getHelpers();
        
        folder = System.getProperty("user.dir")+"/filewriter";
        
        createFolder(folder);
    }  
    
    public void createFolder(String path) {
        File theDir = new File(path);
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }
    
    public void writeToFile(String path, String text) {
       
        callbacks.printOutput(path);
        
        try {
            File theDir = new File(path);
            if (!theDir.exists()){
                theDir.getParentFile().mkdirs();
                theDir.createNewFile();
            }
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(text);
            myWriter.close();
            callbacks.printOutput("Successfully wrote to the file.");
        } catch (IOException e) {
            callbacks.printOutput(e.getMessage());
        }
    }
}
