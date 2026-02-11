package org.example.model.service

class AccountSettingService(private val settingService: SettingService) {
    fun deleteLocalAccount(){
        settingService.removeSetting()
    }
}