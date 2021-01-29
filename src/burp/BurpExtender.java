package burp;

import java.awt.Component;
import hackoverviewer.DemoExtender;

public class BurpExtender implements IBurpExtender {

    private IExtensionHelpers helpers;

    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        DemoExtender.callbacks = callbacks;
        // set our extension name
        callbacks.setExtensionName(DemoExtender.extensionName);
        this.helpers = callbacks.getHelpers();
        callbacks.registerHttpListener(new DemoExtender(callbacks));
    }
}
