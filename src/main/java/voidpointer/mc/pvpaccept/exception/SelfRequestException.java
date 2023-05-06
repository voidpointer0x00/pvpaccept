package voidpointer.mc.pvpaccept.exception;

import voidpointer.mc.pvpaccept.locale.PvpErrorMessage;

public final class SelfRequestException extends PvpException {
    public SelfRequestException() {
        super(PvpErrorMessage.SELF_REQUEST);
    }
}
