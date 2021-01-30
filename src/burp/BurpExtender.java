package burp;

import java.awt.Component;
import filewriter.BurpFileWriter;

public class BurpExtender implements IBurpExtender {

    private IExtensionHelpers helpers;

    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        BurpFileWriter.callbacks = callbacks;
        // set our extension name
        callbacks.setExtensionName(BurpFileWriter.extensionName);
        this.helpers = callbacks.getHelpers();
        callbacks.registerHttpListener(new BurpFileWriter(callbacks));
    }
}
