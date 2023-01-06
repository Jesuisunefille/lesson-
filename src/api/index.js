import axios from 'axios'
import {ip,backgroundPort} from "@/main.js";

// 创建Axios实例：配置Axios请求前缀和超时时间
const baseAxios = axios.create({
    baseURL: ip + ':' + backgroundPort + '/api/v1',
    timeout: 5000
})

// 设置Axios请求拦截器：在发送请求前执行
// 正常执行req函数，异常执行err函数
baseAxios.interceptors.request.use(req => {
    // 从sessionStorage中尝试获取token
    let token = sessionStorage.getItem("token");
    // 如果token获取成功，则将token存放到请求头中
    if (token) req.headers['token'] = token;
    // 无论token是否获取成功，放行请求
    return req;
}, err => Promise.reject(err))


// 设置Axios响应拦截器：在接收到响应之后执行
// 正常执行req函数，异常执行err函数
baseAxios.interceptors.response.use(resp => {
    console.log('baseAxios响应拦截器：', resp);
    if (resp.data['code'] === 99) {
        console.log(resp.data['message']);
        console.log('执行token自动续期');
        let newToken = resp.data['data'];
        console.log('获取到新的token: ', newToken);
        sessionStorage.setItem("token", newToken);
        console.log('更新sessionStorage中的token');
        // todo: 更新vuex中的token
        console.log('更新vuex中的token');
        // 将新的token设置到重发的请求头
        baseAxios.defaults.headers['token'] = newToken;
        console.log('将新的token设置到重发的请求头');
        console.log('重新发送请求');
        // 重新发送请求
        return baseAxios.request(resp.config);
    }
    // 放行响应
    return resp;
}, err => Promise.reject(err))

/*轮播图：批查轮播图记录*/
export const BANNER_LIST_API = () => baseAxios.get(
    "/banner/list");

/*视频：分页查询视频记录*/
export const VIDEO_PAGE_API = (page, size) => baseAxios.get(
    "/video/page",
    {params: {page, size}});

/*视频：按视频主键单查视频记录（包括章节记录）*/
export const VIDEO_SELECT_DETAIL_BY_ID = (id) => baseAxios.get(
    "/video/select-detail-by-id",
    {params: {id}});

/*视频：按视频标题搜索视频*/
export const VIDEO_SEARCH_BY_TITLE_API = (title) => baseAxios.get(
    "/video/search-by-title",
    {params: {title}});

/*用户：单增用户记录*/

export const USER_REGISTER_API = (params) => baseAxios.post(
    "/user/register", params);

/*用户：按手机号和验证码进行登录*/
export const USER_LOGIN_BY_PHONE_API = (params) => baseAxios.post(
    "/user/login-by-phone", params);

/*用户：按主键查询用户积分*/
export const USER_SELECT_POINTS_BY_ID = (id) => baseAxios.get(
    "/user/select-points-by-id",
    {params: {id}});

/*用户：按主键单删用户记录*/
export const USER_DELETE_BY_ID_API = (params) => baseAxios.post(
    "/user/delete-by-id", params);

/*用户：按主键单改用户记录*/
export const USER_UPDATE_BY_ID_API = (params) => baseAxios.post(
    "/user/update-by-id", params);

/*用户：按主键单查用户记录*/
export const USER_SELECT_BY_ID_API = (id) => baseAxios.get(
    "/user/select-by-id",
    {params: {id}});

/*用户：按账号密码进行登录*/
export const USER_LOGIN_API = (params) => baseAxios.post(
    "/user/login", params);

/*用户：按主键单改用户头像*/
export const USER_UPDATE_AVATAR_BY_ID_API = (params) => baseAxios.post(
    "/user/update-avatar-by-id", params);

/*用户：按主键单改用户密码*/
export const USER_UPDATE_PASSWORD_BY_ID_API = (params) => baseAxios.post(
    "/user/update-password-by-id", params);

/*用户：按手机号获取验证码*/
export const USER_GET_VERIFICATION_CODE = (phone) => baseAxios.get(
    "/user/get-verification-code",
    {params: {phone}});

/*订单：单增订单记录*/
export const ORDER_INSERT_API = (params) => baseAxios.post(
    "/order/insert", params);

/*订单：按主键单删订单记录*/
export const ORDER_DELETE_BY_ID_API = (params) => baseAxios.post(
    "/order/delete-by-id", params);

/*订单：按用户主键批查订单记录*/
export const ORDER_SELECT_BY_USERID_API = (userId) => baseAxios.get(
    "/order/select-by-user-id",
    {params: {"user-id": userId}});

/*购物车：单增购物车记录*/
export const CART_INSERT_OR_UPDATE_API = (params) => baseAxios.post(
    "/cart/insert-or-update", params);

/*购物车：按用户主键清空购物车记录*/
export const CART_DELETE_BY_USER_ID = (params) => baseAxios.post(
    "/cart/delete-by-user-id", params);

/*购物车：按用户主键和商品主键数组批删购物车记录*/
export const CART_DELETE_BY_USER_ID_AND_VIDEO_IDS = (params) => baseAxios.post(
    "/cart/delete-by-user-id-and-video-ids", params);

/*购物车：按用户主键批查购物车记录*/
export const CART_SELECT_BY_USER_ID_API = (userId) => baseAxios.get(
    "/cart/select-by-user-id",
    {params: {"user-id": userId}});