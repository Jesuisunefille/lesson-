import axios from 'axios'
import {lessonUserBackgroundHost} from '@/global_variable'

// 创建Axios实例：配置Axios请求前缀和超时时间
const baseAxios = axios.create({
    baseURL: `${lessonUserBackgroundHost}/api/v1`,
    timeout: 5000
})

// 设置Axios请求拦截器：在发送请求前执行，正常时执行req函数，异常时执行err函数
baseAxios.interceptors.request.use(req => {

    // 从sessionStorage中获取Token令牌
    let token = sessionStorage.getItem("token");

    // 若Token令牌存在，则将Token令牌存入请求头
    if (token) req.headers['token'] = token;

    // 无无论Token是否获取成功，都放行请求
    return req;

}, err => Promise.reject(err))

// 设置Axios响应拦截器：在接收到响应之后执行，正常时执行req函数，异常时执行err函数
baseAxios.interceptors.response.use(resp => {

    // 统一打印响应数据，便于测试
    console.log('baseAxios响应拦截器：', resp);

    // 判断是否需要对Token进行自动续期
    if (resp.data['code'] === 99) {

        console.log("执行Token自动续期");

        // 从响应中获取新的Token令牌
        let newToken = resp.data['data'];

        // 更新sessionStorage中的Token令牌
        sessionStorage.setItem("token", newToken);

        // 更新vuex中的Token令牌
        // this.$store.dispatch('setToken', newToken)

        // 将新的Token令牌设置到请求头
        baseAxios.defaults.headers['token'] = newToken;

        console.log("Token自动续期完毕，重新发送请求");

        // 重新发送请求
        return baseAxios.request(resp.config);
    }

    // 若不需要Token续期，则直接放行请求
    return resp;

}, err => Promise.reject(err))

/*轮播图：批查轮播图记录*/
export const BANNER_LIST_API = () => baseAxios.get(
    "/banner/list");

export const VIDEO_PAGE_API = (page, size) => baseAxios.get(
    "/video/page",
    {params: {page, size}});

/*视频：按视频主键单查视频记录（包括章节记录）*/
export const VIDEO_SELECT_DETAIL_BY_VIDEO_ID = (videoId) => baseAxios.get(
    "/video/select-detail-by-id",
    {params: {'video-id': videoId}});

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
export const USER_SELECT_POINTS_BY_USER_ID = (userId) => baseAxios.get(
    "/user/select-points-by-user-id",
    {params: {'user-id': userId}});

/*用户：按主键单删用户记录*/
export const USER_DELETE_BY_USER_ID_API = (params) => baseAxios.post(
    "/user/delete-by-user-id", params);

/*用户：按主键单改用户记录*/
export const USER_UPDATE_BY_USER_ID_API = (params) => baseAxios.post(
    "/user/update-by-user-id", params);

// 用户GET: 按 `用户ID` 查询一条 `用户` 记录
export const USER_SELECT_BY_USER_ID_API = (userId) => baseAxios.get(
    "/user/select-by-id",
    {params: {'user-id': userId}});

/*用户：按账号密码进行登录*/
export const USER_LOGIN_API = (params) => baseAxios.post(
    "/user/login", params);

/*用户：按主键单改用户头像*/
export const USER_UPDATE_AVATAR_BY_USER_ID_API = (params) => baseAxios.post(
    "/user/update-avatar-by-id", params);

/*用户：按主键单改用户密码*/
export const USER_UPDATE_PASSWORD_BY_USER_ID_API = (params) => baseAxios.post(
    "/user/update-password-by-id", params);

/*用户：按手机号获取验证码*/
export const USER_GET_VERIFICATION_CODE = (phone) => baseAxios.get(
    "/user/get-verification-code",
    {params: {phone}});

/*订单：单增订单记录*/
export const ORDER_INSERT_API = (params) => baseAxios.post(
    "/order/insert", params);

/*订单：按用户主键批查订单记录*/
export const ORDER_PAGE_DETAIL_BY_USER_ID_API = (userId, page, size) => baseAxios.get(
    "/order/page-detail-by-user-id",
    {params: {'user-id': userId, page, size}});

/*购物车：单增购物车记录*/
export const CART_INSERT_OR_UPDATE_API = (params) => baseAxios.post(
    "/cart/insert-or-update", params);

/*购物车：按用户主键清空购物车记录*/
export const CART_DELETE_BY_USER_ID = (params) => baseAxios.post(
    "/cart/delete-by-id", params);

/*购物车：按用户主键和商品主键数组批删购物车记录*/
export const CART_DELETE_BY_USER_ID_AND_VIDEO_IDS = (params) => baseAxios.post(
    "/cart/delete-by-user-id-and-video-ids", params);

/*购物车：按用户主键批查购物车记录*/
export const CART_SELECT_BY_USER_ID_API = (userId) => baseAxios.get(
    "/cart/select-by-user-id",
    {params: {"user-id": userId}});

// 订单POST: 按 `订单ID` 删除一条 `订单` 记录
export const ORDER_DELETE_BY_ORDER_API = (params) => baseAxios.post(
    "/order/delete-by-order-id", params);

// 购物车POST：按 `用户ID/商品ID数组` 删除多条 `购物车` 记录
export const CART_DELETE_BY_USER_ID_AND_VIDEO_IDS_API = (params) => baseAxios.post(
    "/cart/delete-by-user-id-and-video-ids", params);

// 视频订单中间表POST：按 `视频订单中间表ID` 删除一条 `视频订单中间表` 记录
export const VIDEO_ORDER_DELETE_BY_VIDEO_ORDER_API = (params) => baseAxios.post(
    "/video-order/delete-by-video-order-id", params);

// 购物车POST：按 `用户ID` 清空 `购物车` 记录
export const CART_DELETE_BY_USER_ID_API = (params) => baseAxios.post(
    "/cart/delete-by-user-id", params);









