package voidpointer.mc.pvpaccept.data;

import java.util.Date;
import java.util.UUID;

public record PvpDuelSession(UUID requested, UUID requestSender, Date expiresAt) {}
