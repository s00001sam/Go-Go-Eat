package com.sam.gogoeat.api.usecase

//class AccountLogin(private var repository: FlowRepository, private var coreInfoRepository: CoreInfoRepository) : ResourceUseCase<Resource<TutorUserData>, AccountLoginReq, TutorUserData, BaseResp<TutorUserData>>() {
//    override suspend fun processAfterGetResource(params: AccountLoginReq, resource: Resource<TutorUserData>): Resource<TutorUserData> {
//        if (resource.isSuccess()) {
//            updateUserData(resource.data)
//            saveAccount(params)
//        }
//        return super.processAfterGetResource(params, resource)
//    }
//
//    private suspend fun saveAccount(params: AccountLoginReq) {
//        val finalSaveAccount = if (params.saveAccount) params.account else null
//        repository.clearLoginAccountData()
//        repository.saveLoginAccountData(LoginAccountData(params.saveAccount, finalSaveAccount))
//    }
//
//    private suspend fun updateUserData(tutorUserData: TutorUserData?) {
//        VLog.i("updateUserData, $tutorUserData")
//        val coreInfo = coreInfoRepository.getBaseCoreInfo()
//        coreInfo.clientSn = tutorUserData?.clientSn ?: ""
//        coreInfo.token = tutorUserData?.token ?: ""
//        coreInfo.greenDayClientSn = tutorUserData?.clientSn ?: ""
//        CoreInfoInternalHelper.saveCoreInfo(coreInfo)
//        tutorUserData?.let {
//            repository.saveTutorUserData(it)
//        }
//    }
//
//    override suspend fun getResponse(params: AccountLoginReq): Response<BaseResp<TutorUserData>> {
//        return repository.login(LoginRequest(params.account, params.password, params.prod))
//    }
//}