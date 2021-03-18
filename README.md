# Blog [Demo](https://www.ruok.moe)
使用java开发的博客，主要技术如下：
场景 | 技术
---|---
后端 | Springboot
前端模板 | 由[Hexo icarus主题](https://github.com/ppoffice/hexo-theme-icarus)改为freemarker
权限 | Spring Security
登陆 | 账号密码登陆或Github登陆
后台管理模板 | [Layui](https://www.layui.com/)
数据库访问 | Spring Data Jpa
Markdown支持 | [editor.md](https://pandao.github.io/editor.md/)

主要功能：
### 1、博客浏览
![image](https://user-images.githubusercontent.com/22655828/111648133-0baa6b80-883e-11eb-9bab-c6ee481d1433.png)

### 2、后台管理
![image](https://user-images.githubusercontent.com/22655828/111648285-2aa8fd80-883e-11eb-854d-ce510e0cb4f7.png)

### 3、图库
![image](https://user-images.githubusercontent.com/22655828/111649048-f7b33980-883e-11eb-8905-68392f91b630.png)

用于文件管理及当作图库
- 需要管理权限
- 目前只支持拖拽上传和复制粘贴上传
- 下载：选择一个是直接下载，选择多个或文件夹是打包下载
- 原图：此页展示的是压缩图片，点击原图复制原图链接到剪贴板

### 4、登陆
![image](https://user-images.githubusercontent.com/22655828/111649010-ed913b00-883e-11eb-83da-94d5ea9aa595.png)

支持账号密码登陆及Github登陆，账号密码只支持后台生成。

### 5、评论
![image](https://user-images.githubusercontent.com/22655828/111650267-16fe9680-8840-11eb-9ab8-5212401d0bac.png)
