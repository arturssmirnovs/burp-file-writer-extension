package hackoverviewer;

import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IHttpListener;
import burp.IHttpRequestResponse;
import burp.IParameter;
import burp.IRequestInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DemoExtender implements IHttpListener {

    IBurpExtenderCallbacks extenderCallbacks;
    IExtensionHelpers extenderHelpers;
    IRequestInfo requestInfo;
    String domainName;
    String url;
    String hash;
    String params;
    
    public static IBurpExtenderCallbacks callbacks;
    public static DemoExtenderGui demoExtenderGui;
    public static String extensionName = "HO Callback";
    
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
     if(toolFlag == IBurpExtenderCallbacks.TOOL_PROXY && messageIsRequest == true)
        requestInfo = extenderHelpers.analyzeRequest(messageInfo);
        domainName = requestInfo.getUrl().getHost(); 
        url = requestInfo.getUrl().toString(); 
        hash = demoExtenderGui.getHash();
        
        List<IParameter> paras = requestInfo.getParameters();//当body是json格式的时候，这个方法也可以正常获取到键值对，牛掰。但是PARAM_JSON等格式不能通过updateParameter方法来更新。
        Map<String,String> paraMap = new HashMap<String,String>();
        for (IParameter para:paras){
          paraMap.put(para.getName(), para.getValue());
        }
        
        params = paraMap.toString();
        
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", "curl -d '{\"domain\":\""+domainName+"\",\"type\":\""+requestInfo.getContentType()+"\",\"params\":\""+params+"\",\"method\":\""+requestInfo.getMethod()+"\", \"url\":\""+url+"\", \"hash\":\""+hash+"\"}' -H \"Content-Type: application/json\" -X POST "+demoExtenderGui.getURL());
            Process process = processBuilder.start();
        } catch (IOException ex) {

        }
    }
    
    public DemoExtender(IBurpExtenderCallbacks callbacks) {   
        extenderCallbacks = callbacks;
        extenderHelpers = callbacks.getHelpers();
    }  
}
