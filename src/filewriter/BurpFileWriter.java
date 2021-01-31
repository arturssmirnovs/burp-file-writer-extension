package filewriter;

import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IHttpListener;
import burp.IHttpRequestResponse;
import burp.IRequestInfo;
import burp.IResponseInfo;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Arrays;
import org.json.simple.JSONObject;

public final class BurpFileWriter implements IHttpListener {

    IBurpExtenderCallbacks extenderCallbacks;
    IExtensionHelpers extenderHelpers;
    IRequestInfo requestInfo;
    IResponseInfo responseInfo;
    
    String folder;
    String domainName;
    String url;
    
    public static IBurpExtenderCallbacks callbacks;
    public static String extensionName = "File Writer";
    
    /**
     * Process request / response
     * 
     * @param toolFlag
     * @param messageIsRequest
     * @param messageInfo 
     */
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
        
     if(toolFlag == IBurpExtenderCallbacks.TOOL_PROXY && messageIsRequest == true)
        requestInfo = extenderHelpers.analyzeRequest(messageInfo);
     
        // checking if response is null
        try {
            int x = messageInfo.getResponse().length;
        } catch(Exception e) {
            return;
        }
        
        responseInfo = extenderHelpers.analyzeResponse(messageInfo.getResponse());
        
        domainName = requestInfo.getUrl().getHost(); 
        url = requestInfo.getUrl().toString(); 
        
        JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("domain", domainName);
        requestJsonObject.put("url", url);
        requestJsonObject.put("method", requestInfo.getMethod());
        requestJsonObject.put("contentType", requestInfo.getContentType());
        requestJsonObject.put("headers", requestInfo.getHeaders().toString());

        JSONObject responseJsonObject = new JSONObject();
        
        responseJsonObject.put("status", String.valueOf(responseInfo.getStatusCode()));
        responseJsonObject.put("headers", String.valueOf(responseInfo.getHeaders()));

        JSONObject json = new JSONObject();
        json.put("request", requestJsonObject);
        json.put("response", responseJsonObject);

        byte[] byte_Request = messageInfo.getResponse();
        byte[] byte_body = Arrays.copyOfRange(byte_Request, responseInfo.getBodyOffset(), byte_Request.length);//not length-1
        String body = new String(byte_body);
            
        String path = folder;
        
        // create folder if don't exists
        createFolder(path+"/"+domainName);
                
        String filePath = url.replace(requestInfo.getUrl().getProtocol()+"://", "").replace(":"+requestInfo.getUrl().getPort(), "");
        
        if (filePath.endsWith("/")) {
            filePath = filePath+"index";
        }
        
        json.put("path", filePath);

        // write request
        writeToFile(path+"/"+filePath, body);
        
        // write data of request
        writeToFile(path+"/"+filePath+".data", json.toJSONString());
    }
    
    /**
     * Initial script
     * 
     * @param callbacks 
     */
    public BurpFileWriter(IBurpExtenderCallbacks callbacks) {   
        extenderCallbacks = callbacks;
        extenderHelpers = callbacks.getHelpers();
        
        folder = System.getProperty("user.dir")+"/filewriter";
        
        createFolder(folder);
        
        callbacks.printOutput("Writing files to: "+folder);
    }  
    
    /**
     * Create initial folder
     * 
     * @param path 
     */
    public void createFolder(String path) {
        File theDir = new File(path);
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }
    
    /**
     * Write file to path
     * 
     * @param path
     * @param text 
     */
    public void writeToFile(String path, String text) {
        try {
            File theDir = new File(path);
            if (!theDir.exists()){
                theDir.getParentFile().mkdirs();
                theDir.createNewFile();
            }
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(text);
            myWriter.close();
        } catch (IOException e) {
            callbacks.printOutput(path+" : "+e.getMessage());
        }
    }
}