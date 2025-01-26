package org.yalli.wah.model.exception;

public class ExcessivePasswordResetAttemptsException extends RuntimeException{
    public ExcessivePasswordResetAttemptsException(int attemptMade, int maxAttemptsAllowed){
        super("Exceeded maximum password reset attempts. Attempts made: " + attemptMade + ", Max allowed: " + maxAttemptsAllowed);
    }
}
