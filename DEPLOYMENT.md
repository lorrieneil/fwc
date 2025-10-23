# 部署指南

## 发布前检查清单

### 代码质量
- [ ] 所有功能正常工作
- [ ] 已修复已知的Bug
- [ ] 代码已经过审查
- [ ] 已移除调试代码和日志
- [ ] 已更新版本号

### 测试
- [ ] 单元测试通过
- [ ] UI测试通过
- [ ] 在多个设备上测试
- [ ] 在不同Android版本上测试
- [ ] 性能测试通过

### 文档
- [ ] README.md 已更新
- [ ] CHANGELOG.md 已更新
- [ ] API文档已更新
- [ ] 使用说明完整

### 配置
- [ ] 签名密钥已配置
- [ ] ProGuard规则已优化
- [ ] 应用权限检查正确
- [ ] 版本号已递增

## 版本号管理

### 当前版本
- versionCode: 1
- versionName: "1.0.0"

### 版本号规则
- **versionCode**: 每次发布递增（整数）
- **versionName**: 语义化版本 (MAJOR.MINOR.PATCH)

更新位置：`app/build.gradle.kts`

```kotlin
defaultConfig {
    versionCode = 1
    versionName = "1.0.0"
}
```

## 构建发布版本

### 1. 生成签名密钥（首次）

```bash
keytool -genkey -v -keystore pushup-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias pushup-key
```

保存密钥信息：
- Keystore 密码
- Key 别名
- Key 密码

### 2. 配置签名

在 `app/build.gradle.kts` 中：

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("../pushup-release-key.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

### 3. 构建Release APK

```bash
# 构建 APK
./gradlew assembleRelease

# 输出位置
# app/build/outputs/apk/release/app-release.apk
```

### 4. 构建 AAB（推荐）

```bash
# 构建 Android App Bundle
./gradlew bundleRelease

# 输出位置
# app/build/outputs/bundle/release/app-release.aab
```

## 发布到 Google Play

### 1. 准备资源

#### 应用图标
- 512x512 px 高分辨率图标

#### 应用截图
- 至少 2 张截图
- 推荐尺寸：1080x1920 px

#### 功能图片
- 1024x500 px

#### 宣传图片（可选）
- 180x120 px

### 2. 应用描述

#### 简短描述（80字符）
```
基于AI的俯卧撑计数应用，智能分析视频，实时计数
```

#### 完整描述（4000字符）
参考 README.md 中的内容编写

#### 主要功能
- 视频上传分析
- AI姿态检测
- 智能计数
- 实时反馈

### 3. 分类和标签

- **应用类型**: 应用
- **类别**: 健康与健身
- **内容分级**: 所有人
- **标签**: 健身, 运动, AI, 计数器

### 4. 隐私政策

如果应用收集用户数据，需要提供隐私政策URL。

当前应用不收集用户数据，可以声明：
- 应用不访问网络
- 所有处理都在本地进行
- 不收集个人信息

### 5. 上传步骤

1. 登录 [Google Play Console](https://play.google.com/console)
2. 创建新应用
3. 填写商店信息
4. 上传 AAB 文件
5. 填写版本说明
6. 设置定价和分发
7. 提交审核

## 版本更新流程

### 1. 更新代码
- 修复Bug或添加新功能
- 更新 CHANGELOG.md

### 2. 更新版本号
```kotlin
// app/build.gradle.kts
defaultConfig {
    versionCode = 2  // 递增
    versionName = "1.0.1"  // 更新
}
```

### 3. 测试
- 完整测试新功能
- 回归测试旧功能

### 4. 构建和发布
```bash
./gradlew bundleRelease
```

### 5. 在Google Play发布
- 创建新版本
- 上传新的AAB
- 填写更新说明
- 选择发布方式（立即/分阶段）

## 发布渠道

### 内部测试
- 用于内部团队测试
- 最快的发布流程

### 封闭测试
- 邀请指定测试人员
- 获取早期反馈

### 开放测试
- 任何人都可以加入
- 收集更广泛的反馈

### 生产
- 所有用户可见
- 建议先进行分阶段发布

## 回滚计划

如果发现严重问题：

1. **立即操作**
   - 在 Google Play Console 暂停发布
   - 评估问题严重程度

2. **短期方案**
   - 发布紧急修复版本
   - 或回滚到上一个稳定版本

3. **长期方案**
   - 分析问题根源
   - 改进测试流程
   - 更新开发流程

## 监控和分析

### Google Play Console
- 崩溃报告
- ANR（应用无响应）
- 用户评分和评论
- 安装/卸载统计

### Firebase（可选）
- 崩溃分析
- 性能监控
- 用户行为分析

## 用户支持

### 响应评论
- 及时回复用户评论
- 感谢正面反馈
- 积极解决问题

### 问题跟踪
- 使用 GitHub Issues
- 分类和优先级
- 定期更新状态

## 法律和合规

### 许可证
- 确保所有依赖的许可证兼容
- 在应用中显示第三方许可证

### 权限
- 只请求必要的权限
- 在使用时解释权限用途

### 数据隐私
- 遵守 GDPR（如适用）
- 提供数据删除选项
- 透明的数据使用政策

## 市场推广

### 发布公告
- 在社交媒体分享
- 撰写博客文章
- 提交到应用目录网站

### 用户反馈
- 鼓励用户评分
- 收集使用反馈
- 持续改进应用

## 备份和恢复

### 重要文件备份
- 签名密钥文件（安全存储）
- 密钥密码（加密存储）
- 版本发布记录
- 用户数据（如适用）

## 后续版本规划

参考 [FEATURES.md](FEATURES.md) 中的规划：

- v1.1.0: 实时摄像头模式
- v1.2.0: 历史记录和统计
- v2.0.0: 多运动类型支持

---

**祝发布顺利！** 🚀
