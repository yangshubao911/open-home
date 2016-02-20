package com.shihui.openpf.home.resource;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.home.service.api.ClientService;
import me.weimi.api.auth.annotations.AuthType;
import me.weimi.api.commons.context.RequestContext;
import me.weimi.api.swarm.annotations.ApiStatus;
import me.weimi.api.swarm.annotations.BaseInfo;
import me.weimi.api.swarm.annotations.ParamDesc;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by zhoutc on 2016/2/20.
 */
@Controller
@Path("/v2/openpf/home/client")
public class ClientResource {

    @Resource
    ClientService clientService;

    @Path("/listGoods")
    @GET
    @BaseInfo(desc = "查询大类下所有商品", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({ MediaType.APPLICATION_JSON })
    public String list(@Context RequestContext rc,
                       @ParamDesc(desc = "业务Id", isRequired = true) @QueryParam("serviceId") int serviceId,
                       @ParamDesc(desc = "小区Id", isRequired = true) @QueryParam("groupId") long groupId,
                       @ParamDesc(desc = "用户Id", isRequired = true) @QueryParam("userId") long userId) {
        return clientService.listGoods(serviceId , userId, groupId);
    }



}
