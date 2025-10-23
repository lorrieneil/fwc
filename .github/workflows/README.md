# GitHub Actions 工作流说明

本项目使用 GitHub Actions 自动构建 APK 文件。

## 工作流文件

### 1. build-apk.yml - 自动构建

**触发条件：**
- 推送到 main/master/develop 分支
- 创建针对 main/master 分支的 Pull Request
- 手动触发（在 GitHub Actions 页面）

**功能：**
- 构建 Debug 和 Release 版本的 APK
- 将 APK 作为 Artifacts 上传（保留 7 天）
- 可在 Actions 页面下载构建产物

**使用方法：**
直接推送代码到对应分支即可自动触发构建。

### 2. build-and-release-apk.yml - 构建并发布

**触发条件：**
- 推送 Git 标签（tag），格式为 `v*`（例如：v1.0.0, v2.1.3）
- 手动触发（可指定版本号）

**功能：**
- 构建 Debug 和 Release 版本的 APK
- 自动创建 GitHub Release
- 上传 APK 文件到 Release
- 生成详细的 Release 说明

**使用方法：**

#### 方法一：通过 Git 标签发布（推荐）

```bash
# 创建标签
git tag -a v1.0.0 -m "Release version 1.0.0"

# 推送标签到 GitHub
git push origin v1.0.0
```

#### 方法二：手动触发

1. 访问 GitHub 仓库的 Actions 页面
2. 选择 "Build and Release APK" 工作流
3. 点击 "Run workflow"
4. 输入版本号（如 v1.0.0）
5. 点击 "Run workflow" 按钮

## 下载 APK

### 从 Artifacts 下载（开发构建）

1. 访问仓库的 Actions 页面
2. 选择一个成功的工作流运行
3. 在页面底部的 "Artifacts" 区域下载 APK

### 从 Releases 下载（正式发布）

1. 访问仓库的 Releases 页面
2. 选择对应的版本
3. 在 "Assets" 区域下载 APK 文件

推荐下载 **PushUpCounter-Release.apk**，这是优化后的版本。

## APK 签名说明

工作流使用自动生成的开发签名密钥：
- 密钥别名: pushup-key
- 密钥密码: android123

⚠️ **注意：** 这些是开发/测试用密钥，不适合用于 Google Play 等正式发布。

如需正式发布到应用商店，请：
1. 生成生产密钥
2. 将密钥信息保存到 GitHub Secrets
3. 修改工作流以使用 Secrets 中的密钥

## 版本号管理

建议的版本号格式：
- 主版本.次版本.修订号（例如：v1.0.0）
- 主版本：重大更新或不兼容的修改
- 次版本：新功能添加
- 修订号：Bug 修复

示例：
- v1.0.0 - 首次正式发布
- v1.1.0 - 添加新功能
- v1.1.1 - 修复 Bug

## 故障排除

### 构建失败

1. 检查 Actions 日志中的错误信息
2. 确保 `app/build.gradle.kts` 配置正确
3. 确保所有依赖都可访问

### 无法创建 Release

1. 确保标签名称以 `v` 开头
2. 检查仓库是否有创建 Release 的权限
3. 确保 GITHUB_TOKEN 有足够的权限

### APK 无法安装

1. 确认 Android 版本 >= 7.0 (API 24)
2. 检查设备是否允许安装未知来源的应用
3. 如果已安装旧版本，请先卸载

## 相关文档

- [BUILD_APK.md](../../BUILD_APK.md) - 本地构建 APK 指南
- [DEPLOYMENT.md](../../DEPLOYMENT.md) - 部署到生产环境的指南
- [DEVELOPMENT.md](../../DEVELOPMENT.md) - 开发指南
