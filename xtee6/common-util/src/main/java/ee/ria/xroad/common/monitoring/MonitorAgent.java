package ee.ria.xroad.common.monitoring;

import java.util.Date;

import akka.actor.ActorSystem;
import lombok.extern.slf4j.Slf4j;

/**
 * This class encapsulates monitoring agent that can receive
 * monitoring information.
 */
@Slf4j
public final class MonitorAgent {

    private static MonitorAgentProvider monitorAgentImpl;

    private MonitorAgent() {
    }

    /**
     * Initialize the MonitorAgent with given ActorSystem.
     * This method must be called before any other methods in this class.
     * @param actorSystem actor system to be used by this monitoring agent
     */
    public static void init(ActorSystem actorSystem) {
        monitorAgentImpl = new DefaultMonitorAgentImpl(actorSystem);
    }

    /**
     * Initialize the MonitorAgent with given implementation.
     * This method must be called before any other methods in this class.
     * @param implementation monitor agent implementation to be used by this monitoring agent
     */
    public static void init(MonitorAgentProvider implementation) {
        MonitorAgent.monitorAgentImpl = implementation;
    }

    /**
     * Message was processed successfully by the proxy.
     * @param messageInfo Successfully processed message.
     * @param startTime Time of start of the processing.
     * @param endTime Time of end of the processing.
     */
    public static void success(MessageInfo messageInfo, Date startTime,
            Date endTime) {
        try {
            if (monitorAgentImpl != null) {
                monitorAgentImpl.success(messageInfo, startTime, endTime);
            }
        } catch (Throwable t) {
            log.error("MonitorAgent::success() failed", t);
        }
    }

    /**
     * Client proxy failed to make connection to server proxy.
     * @param messageInfo information about the message that could not be sent
     */
    public static void serverProxyFailed(MessageInfo messageInfo) {
        try {
            if (monitorAgentImpl != null) {
                monitorAgentImpl.serverProxyFailed(messageInfo);
            }
        } catch (Throwable t) {
            log.error("MonitorAgent::serverProxyFailed() failed", t);
        }
    }

    /**
     * Processing of a given message failed for various reasons.
     * Parameter messageInfo can be null if the message is not available
     * at the point of the failure.
     * @param messageInfo information about the message that could not be processed
     * @param faultCode fault code of the failure
     * @param faultMessage fault message of the failure
     */
    public static void failure(MessageInfo messageInfo, String faultCode,
            String faultMessage) {
        try {
            if (monitorAgentImpl != null) {
                monitorAgentImpl.failure(messageInfo, faultCode, faultMessage);
            }
        } catch (Throwable t) {
            log.error("MonitorAgent::failure() failed", t);
        }
    }

}
