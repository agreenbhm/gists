import burp.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;

public class BurpExtension implements IBurpExtender, IContextMenuFactory {

    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();

        callbacks.setExtensionName("Custom HTTP Response Processor");

        callbacks.registerContextMenuFactory(this);
    }

    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation invocation) {
        List<JMenuItem> menuItems = new ArrayList<>();
        JMenuItem menuItem = new JMenuItem("Process HTTP Response");

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IHttpRequestResponse[] selectedMessages = invocation.getSelectedMessages();

                for (IHttpRequestResponse message : selectedMessages) {
                    processHttpResponse(message);
                }
            }
        });

        menuItems.add(menuItem);
        return menuItems;
    }

    private void processHttpResponse(IHttpRequestResponse message) {
        byte[] response = message.getResponse();
        if (response == null) {
            callbacks.printError("No response to process.");
            return;
        }

        // You can implement your custom processing code here
        // For example, you can print the response to the extension output
        callbacks.printOutput("Processing HTTP response:");
        callbacks.printOutput(helpers.bytesToString(response));
    }
}
