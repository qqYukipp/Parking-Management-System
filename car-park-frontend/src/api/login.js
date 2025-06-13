import request from "@/utils/request.js";


export const loginApi = (data) => {
    return request.post('/login', data)
}

export const getRouterApi = () => {
    return request.get('/getRouters')
}

export const registerApi = (data) => {
    return request.post('/register', data)
}

export const thirdLoginApi = (provider) => {
    return request.get(`/oauth2/authorization/${provider}`)
}