package voidpointer.mc.pvpaccept.exception;

import voidpointer.mc.pvpaccept.locale.PvpErrorMessage;

public final class PlayerRequiredException extends PvpException {
    public PlayerRequiredException() {
        super(PvpErrorMessage.PLAYER_REQUIRED);
    }
}
