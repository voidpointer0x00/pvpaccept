package voidpointer.mc.pvpaccept.exception;

import voidpointer.mc.pvpaccept.locale.PvpErrorMessage;

public class DuelNotFoundException extends PvpException {
    public DuelNotFoundException() {
        super(PvpErrorMessage.DUEL_NOT_FOUND);
    }
}
