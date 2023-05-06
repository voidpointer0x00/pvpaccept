package voidpointer.mc.pvpaccept.exception;

import voidpointer.mc.pvpaccept.locale.PvpErrorMessage;

public final class RequestSenderOfflineException extends PvpException {
    public RequestSenderOfflineException() {
        super(PvpErrorMessage.REQUEST_SENDER_OFFLINE);
    }
}
