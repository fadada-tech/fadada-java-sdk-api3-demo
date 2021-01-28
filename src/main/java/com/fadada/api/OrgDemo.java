package com.fadada.api;

import com.fadada.api.bean.req.organization.*;
import com.fadada.api.bean.rsp.BaseRsp;
import com.fadada.api.bean.rsp.organization.*;
import com.fadada.api.client.OrgClient;
import com.fadada.api.exception.ApiException;

import java.util.List;


public class OrgDemo extends BaseDemo {


    private OrgClient orgClient;

    public OrgDemo(FadadaApiClient fadadaApiClient) {
        this.orgClient = new OrgClient(fadadaApiClient);
    }

    public static void main(String[] args) {

        BaseDemo baseDemo = new BaseDemo();
        DefaultFadadaApiClient client = baseDemo.getClient();
        OrgDemo orgDemo = new OrgDemo(client);
        try {
            // 获取token
            baseDemo.getToken(client);

            // 删除员工
            orgDemo.delSubEmployee();
            // 获取添加员工url
            orgDemo.getAddEmployeeUrl();
            // 获取添加子公司url
            orgDemo.getAddSubCompanyUrl();
            // 获取变更管理员url
            orgDemo.getChangeCompanyMajorUrl();
            // 获取子公司列表
            orgDemo.getChildCompanyList();
            // 获取员工列表
            orgDemo.getEmployee();
            // 移除子公司
            orgDemo.removeSubCompany();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除员工
     *
     * @throws ApiException
     */
    public void delSubEmployee() throws ApiException {
        DelSubEmployeeReq req = new DelSubEmployeeReq();
        req.setToken(token);
        req.setCompany(unionId);
        DelSubEmployeeReq.EmployeeInfo employeeInfo = new DelSubEmployeeReq.EmployeeInfo();
        employeeInfo.setUnionId(unionId);
        req.setEmployeeInfo(employeeInfo);
        BaseRsp rsp = orgClient.delSubEmployee(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取添加员工url
     *
     * @throws ApiException
     */
    public void getAddEmployeeUrl() throws ApiException {
        GetAddEmployeeUrlReq req = new GetAddEmployeeUrlReq();
        req.setToken(token);
        req.setCompany(unionId);
        GetAddEmployeeUrlReq.EmployeeInfo employeeInfo = new GetAddEmployeeUrlReq.EmployeeInfo();
        employeeInfo.setUnionId(unionId);
        req.setEmployeeInfo(employeeInfo);
        BaseRsp<GetAddEmployeeUrlRsp> rsp = orgClient.getAddEmployeeUrl(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取新增子公司url
     *
     * @throws ApiException
     */
    public void getAddSubCompanyUrl() throws ApiException {
        GetAddSubCompanyUrlReq req = new GetAddSubCompanyUrlReq();
        req.setToken(token);
        req.setParentCompany("母公司unionId");
        req.setSubCompany("子公司unionId");
        BaseRsp<GetAddSubCompanyUrlRsp> rsp = orgClient.getAddSubCompanyUrl(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取变更公司管理员url
     *
     * @throws ApiException
     */
    public void getChangeCompanyMajorUrl() throws ApiException {
        GetChangeCompanyMajorUrlReq req = new GetChangeCompanyMajorUrlReq();
        req.setToken(token);
        req.setUnionId(unionId);
        BaseRsp<GetChangeCompanyMajorUrlRsp> rsp = orgClient.getChangeCompanyMajorUrl(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取子公司列表
     *
     * @throws ApiException
     */
    public void getChildCompanyList() throws ApiException {
        GetChildCompanyListReq req = new GetChildCompanyListReq();
        req.setToken(token);
        req.setCompany("指定企业unionId");
        BaseRsp<List<GetChildCompanyListRsp>> rsp = orgClient.getChildCompanyList(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取员工列表
     *
     * @throws ApiException
     */
    public void getEmployee() throws ApiException {
        GetEmployeeReq req = new GetEmployeeReq();
        req.setToken(token);
        req.setCompany("指定企业unionId");
        BaseRsp<List<GetEmployeeRsp>> rsp = orgClient.getEmployee(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 移除子公司
     *
     * @throws ApiException
     */
    public void removeSubCompany() throws ApiException {
        RemoveSubCompanyReq req = new RemoveSubCompanyReq();
        req.setToken(token);
        req.setParentCompany("母公司unionId");
        req.setSubCompany("子公司unionId");
        BaseRsp rsp = orgClient.removeSubCompany(req);
        CommonUtil.checkResult(rsp);
    }


}
