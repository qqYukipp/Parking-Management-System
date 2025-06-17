import { defineStore } from 'pinia'
import { ref } from 'vue'


export const useUserStore = defineStore('user', () => {
    // state：存放当前用户信息
    const userInfo = ref(
        JSON.parse(localStorage.getItem('loginUser') || '{}')
    )
    const dynamicRoutesLoaded = ref(false)

    // action：更新用户信息
    function setUser(newUser) {
        userInfo.value = newUser
        // 同步到 localStorage
        localStorage.setItem('loginUser', JSON.stringify(newUser))
    }

    function setUserInfo(newUserInfo) {
        userInfo.value.username = newUserInfo.username
        userInfo.value.email = newUserInfo.email
        userInfo.value.phone = newUserInfo.phone
        userInfo.value.avatar = newUserInfo.avatar
        userInfo.value.nickName = newUserInfo.nickName
        userInfo.value.account = newUserInfo.account
        userInfo.value.roleList = newUserInfo.roleList
    }

    function setDynamicRoutesLoaded(value) {
        dynamicRoutesLoaded.value = value;
    }

    return {
        userInfo,
        dynamicRoutesLoaded,
        setUser,
        setUserInfo,
        setDynamicRoutesLoaded
    }
})
