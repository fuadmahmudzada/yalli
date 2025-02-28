//package org.yalli.wah.config;
//
//import org.springframework.security.core.session.SessionInformation;
//import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
//import org.springframework.security.web.authentication.session.SessionAuthenticationException;
//
//import java.util.Comparator;
//import java.util.List;
//
//public class SessionExceededManager extends ConcurrentSessionControlAuthenticationStrategy {
//    /**
//     * @param sessionRegistry the session registry which should be updated when the
//     *                        authenticated session is changed.
//     */
//    public SessionExceededManager(SessionRegistry sessionRegistry) {
//        super(sessionRegistry);
//    }
//    protected void allowableSessionsExceeded(List<SessionInformation> sessions, int allowableSessions,
//                                             SessionRegistry registry) throws SessionAuthenticationException {
//        if (this.exceptionIfMaximumExceeded || (sessions == null)) {
//            throw new SessionAuthenticationException(
//                    this.messages.getMessage("ConcurrentSessionControlAuthenticationStrategy.exceededAllowed",
//                            new Object[] { allowableSessions }, "Maximum sessions of {0} for this principal exceeded"));
//        }
//        // Determine least recently used sessions, and mark them for invalidation
//        sessions.sort(Comparator.comparing(SessionInformation::getLastRequest));
//        int maximumSessionsExceededBy = sessions.size() - allowableSessions + 1;
//        List<SessionInformation> sessionsToBeExpired = sessions.subList(0, maximumSessionsExceededBy);
//        for (SessionInformation session : sessionsToBeExpired) {
//            session.expireNow();
//        }
//    }
//}
