package com.shihui.openpf.home.util;


import me.weimi.api.commons.context.RequestContext;
import me.weimi.api.commons.logger.CentralLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
        olog.setAction(action + "?businessId=" + expand.get("businessId") + "&businessName=" + expand.get("businessName"));
        olog.setChannel(rc.getClientVersion().channel);
        olog.setClientVersion(rc.getClientVersion().clientVersion + "");
        olog.setDeviceId(expand.get("ndeviceid"));
        olog.setCityId(expand.get("cityId"));
        olog.setGid(expand.get("gid"));
        olog.setServiceId(expand.get("serviceId"));
        olog.setUid(rc.getCurrentUid() + "");
        olog.setIp(rc.getIp());
        if(expand.get("product_id")!=null) {
            Map<String, String> map = new HashMap<>();
            map.put("product_id",expand.get("product_id"));
            olog.setExpand(map);
        }
        log.info("TFS LOG:" + olog.toJSONObject());
        centralLogger.log(action, olog.toJSONObject());
    }

}
