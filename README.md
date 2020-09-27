### 介绍

本项目工程主要是法大大 Api3.0 SDK的案例代码，其中使用的参数请到法大大开发者中心申请，使用到的SDK依赖请到法大大开发者中心下载，接口说明等请下载SDK文档查看。

### 用法
（1）首先初始化配置
默认使用构造函数DefaultFadadaApiClient(String appId, String appKey),如果需要对接不同的环境请使用构造函数
DefaultFadadaApiClient(String appId, String appKey, String serverUrl)，serverUrl为不同环境请求地址。
FadadaApiConfig该类里面字段变量主要用来设置签名的类型和超时时间的设置。
FadadaApiService该接口提供了json相关接口和http请求相关接口，如果不想使用默认的方法可以实现该接口。
(2)根据不同需求调用对应的client
例如：要获取token

```
DefaultFadadaApiClient fadadaApiClient = new DefaultFadadaApiClient(APPID, APPKEY);
Oauth2Client oauth2Client = new Oauth2Client(fadadaApiClient);
BaseRsp<AccessTokenRsp> rsp = oauth2Client.getToken();
```

### 方法列表
请求接口对应方法如下，请求以及响应参数信息请查看api文档。

| 请求接口                                 | client类       | 方法名                       | 备注                     |
| ---------------------------------------- | -------------- | ---------------------------- | ------------------------ |
| 获取token                                | Oauth2Client   | getToken                     |                          |
| 获取个人unionId地址                      | AccountClient  | getPersonUnionIdUrl          |                          |
| 获取企业unionId地址                      | AccountClient  | getCompanyUnionIdUrl         |                          |
| 获取用户授权地址                         | AccountClient  | getAuthorizeUrl              |                          |
| 获取个人实名信息                         | AccountClient  | getPersonInfo                |                          |
| 获取企业实名信息                         | AccountClient  | getCompanyInfo               |                          |
| 账号信息校验                             | AccountClient  | checkAccountInfo             |                          |
| 接入方信息查询                           | AccountClient  | getAccessObjectInfo          |                          |
| 原始文件上传                             | DocumentClient | uploadFile                   | hash值已计算，不用传该值 |
| 草稿文件下载                             | DocumentClient | getByDraftId                 |                          |
| 签署文件下载                             | DocumentClient | getBySignFileId              |                          |
| 技术报告下载                             | DocumentClient | contractReportDownload       |                          |
| 在线文件验签                             | DocumentClient | verifySignature              | has值已计算，不用传该值  |
| 关键字查询坐标                           | DocumentClient | lookUpCoordinates            |                          |
| 公证处保全报告下载                       | DocumentClient | downloadEvidenceReport       |                          |
| 依据原始文件创建签署任务                 | SignTaskClient | createSignTaskByFileId       |                          |
| 获取签署任务发起链接                     | SignTaskClient | getSentUrl                   |                          |
| 依据模板创建签署任务                     | SignTaskClient | createSignTaskByDraftId      |                          |
| 获取签署链接                             | SignTaskClient | getSignUrl                   |                          |
| 查询签署任务详情                         | SignTaskClient | getTaskDetailByTaskId        |                          |
| 撤销签署任务                             | SignTaskClient | cancelSignTask               |                          |
| 签署任务催签                             | SignTaskClient | urgeSign                     |                          |
| 签署文件在线预览链接                     | SignTaskClient | getSignPreviewUrl            |                          |
| 依据草稿id批量创建签署任务               | SignTaskClient | batchCreateByDraftId         |                          |
| 根据批次号添加签署任务（依据草稿id创建） | SignTaskClient | batchAddByDraftId            |                          |
| 根据批次号批量发起签署任务               | SignTaskClient | batchSent                    |                          |
| 根据批次号获取签署链接                   | SignTaskClient | batchGetSignUrl              |                          |
| 根据批次号查询签署任务                   | SignTaskClient | batchGetSigntasksByBatchNo   |                          |
| 新增员工                                 | EmployeeClient | addEmployee                  |                          |
| 删除员工                                 | EmployeeClient | delEmployee                  |                          |
| 上传企业印章                             | SealClient     | addCompanySeal               | hash值已计算，不用传该值 |
| 删除企业印章                             | SealClient     | delCompanySeal               |                          |
| 印章授权                                 | SealClient     | sealAuth                     |                          |
| 取消授权                                 | SealClient     | cancelSealAuth               |                          |
| 查询企业印章列表                         | SealClient     | companySealList              |                          |
| 查询企业印章详情                         | SealClient     | companySealDetail            |                          |
| 模板文件上传（模板创建第一步）           | TemplateClient | uploadCompanyTemplateFile    | hash值已计算，不用传该值 |
| 新增或更新模板信息（模板创建第二步）     | TemplateClient | updateCompanyTemplate        |                          |
| 获取模板编辑链接（模板创建第三步）       | TemplateClient | getEditCompanyTemplateUrl    |                          |
| 模板文件删除                             | TemplateClient | delCompanyTemplateFile       |                          |
| 模板列表查询                             | TemplateClient | queryCompanyTemplateList     |                          |
| 模板文件下载                             | TemplateClient | downloadCompanyTemplateFile  |                          |
| 模板在线预览链接                         | TemplateClient | getCompanyTemplatePreviewUrl |                          |
| 获取模板详情                             | TemplateClient | getTemplateDetailById        |                          |
| 模板填充                                 | TemplateClient | createByTemplate             |                          |


### 注意事项

(1) 由于本项目只是一个demo，所以对token的时效性没有处理，真正引入SDK使用请对token时效做好控制，避免token失效带来的问题。
(2) 本项目中引入的pdf,图片资源只是为了完成本项目的使用，切不可把这些文件拿到现网运行。





