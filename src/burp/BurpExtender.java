package burp;

import java.awt.Component;
import hackoverviewer.DemoExtender;
import hackoverviewer.DemoExtenderGui;

public class BurpExtender implements IBurpExtender, ITab {

    private IExtensionHelpers helpers;

    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        DemoExtender.callbacks = callbacks;
        // set our extension name
        callbacks.setExtensionName(DemoExtender.extensionName);
        this.helpers = callbacks.getHelpers();
        DemoExtender.demoExtenderGui = new DemoExtenderGui();
        callbacks.addSuiteTab(this);
        callbacks.registerHttpListener(new DemoExtender(callbacks));
    }

    @Override
    public String getTabCaption() {
        return DemoExtender.extensionName;
    }

    @Override
    public Component getUiComponent() {
        return DemoExtender.demoExtenderGui;
    }
}
