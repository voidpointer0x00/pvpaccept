package voidpointer.mc.pvpaccept.exception;

import voidpointer.mc.pvpaccept.locale.InternalMessage;

public class PluginCommandNotFoundException extends PvpException {
    public PluginCommandNotFoundException() {
        super(InternalMessage.PLUGIN_COMMAND_NOT_FOUND);
    }
}
