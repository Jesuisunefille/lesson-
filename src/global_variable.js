const ip = '192.168.242.77';

// 存放静态资源的Nginx服务器Host地址
const resNginxHost = `http://${ip}:8023`;

// OSS服务器Host地址
// 在访问端口栏查看地址
const ossHost = 'https://cindy-lesson-bucket.oss-cn-hangzhou.aliyuncs.com/';

// 用户后台服务地址
export const lessonUserBackgroundHost = `http://${ip}:5277`;

// 弹幕服务地址
export const lessonDanmuServer = `ws://${ip}:5555/api/v1/danmu-server/`;

// OSS服务器中用户头像的存储地址
export const ossUserAvatar = `${ossHost}/user-avatar/`;

// Nginx轮播图代理地址
export const nginxBanner = `${resNginxHost}/banner/`;

// Nginx视频封面图代理地址
export const nginxVideoCover = `${resNginxHost}/video-cover/`;

// Nginx视频摘要图代理地址
export const nginxVideoSummary = `${resNginxHost}/video-summary/`;

// Nginx视频文件代理地址
export const nginxVideo = `${resNginxHost}/video/`;

