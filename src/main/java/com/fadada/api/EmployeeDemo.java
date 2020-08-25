package com.fadada.api;

import com.fadada.api.bean.req.organization.AddEmployeeReq;
import com.fadada.api.bean.req.organization.DelEmployeeReq;
import com.fadada.api.bean.rsp.BaseRsp;
import com.fadada.api.client.EmployeeClient;
import com.fadada.api.exception.ApiException;

/**
 * @author yh
 * @version 1.0.0
 * @ClassName EmployeeDemo.java
 * @Description 员工接口使用demo
 * @Param
 * @createTime 2020年07月06日 15:36:00
 */
public class EmployeeDemo extends BaseDemo {


    private EmployeeClient employeeClient;

    public EmployeeDemo(FadadaApiClient fadadaApiClient) {
        this.employeeClient = new EmployeeClient(fadadaApiClient);
    }

    public static void main(String[] args) {
        try {
            BaseDemo baseDemo = new BaseDemo();
            DefaultFadadaApiClient client = baseDemo.getClient();
            EmployeeDemo employeeDemo = new EmployeeDemo(client);
            // 获取token
            baseDemo.getToken(client);
            // 新增员工
            employeeDemo.addEmployee();
            // 删除员工
            employeeDemo.delEmployee();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增员工
     */
    public void addEmployee() throws ApiException {
        AddEmployeeReq addEmployeeReq = new AddEmployeeReq();
        addEmployeeReq.setEmployeeInfo(unionId);
        BaseRsp rsp = employeeClient.addEmployee(token, addEmployeeReq);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 删除员工
     */
    public void delEmployee() throws ApiException {
        DelEmployeeReq delEmployeeReq = new DelEmployeeReq();
        delEmployeeReq.setEmployeeInfo(unionId);
        BaseRsp rsp = employeeClient.delEmployee(token, delEmployeeReq);
        CommonUtil.checkResult(rsp);
    }
}
