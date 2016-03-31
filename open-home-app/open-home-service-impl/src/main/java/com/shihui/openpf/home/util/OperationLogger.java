package com.shihui.openpf.home.util;


import me.weimi.api.commons.context.RequestContext;
import me.weimi.api.commons.logger.CentralLogger;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author sofn
 * @version 1.0 Created at: 2016-03-25 15:54
 */
public class OperationLogger {
    private static Logger log = LoggerFactory.getLogger(OperationLogger.class);
    private static CentralLogger centralLogger = CentralLogger.getLogger();



    public static void log(String action, RequestContext rc, Map<String, String> expand) {
        if (rc == null) {
            log.warn("RequestContext not got");
            return;
        }
        OperationLog olog = new OperationLog();
        olog.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        olog.setAction(action);
        olog.setChannel(rc.getClientVersion().channel);
        olog.setClientVersion(rc.getClientVersion().clientVersion + "");
        olog.setCityId(MapUtils.getString(expand, "cityId", "0"));
        olog.setGid(MapUtils.getString(expand, "gid", "0"));
        olog.setServiceId(MapUtils.getString(expand, "serviceId", "0"));
        olog.setUid(rc.getCurrentUid() + "");
        olog.setIp(rc.getIp());

        centralLogger.log("central.operation", olog.toJSONObject());
    }

}
