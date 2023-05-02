package voidpointer.mc.pvpaccept.exception;

import voidpointer.mc.pvpaccept.locale.PvpErrorMessage;

public class PvpRequestNotFoundException extends PvpException {
    public PvpRequestNotFoundException() {
        super(PvpErrorMessage.REQUEST_NOT_FOUND);
    }
}
