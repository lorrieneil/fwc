# 构建APK指南

本文档介绍如何为俯卧撑计数器应用构建可安装的APK文件。

## 快速开始

### 方法一：使用构建脚本（推荐）

最简单的方式是使用提供的构建脚本：

```bash
./build-apk.sh
```

脚本会自动处理所有步骤，包括：
- 检查并生成签名密钥（如果不存在）
- 选择构建类型（Debug或Release）
- 构建APK
- 将APK复制到项目根目录

构建完成后，你会在项目根目录看到生成的APK文件：
- `PushUpCounter-Debug.apk` 或
- `PushUpCounter-Release.apk`

### 方法二：手动构建

#### 1. 生成签名密钥（首次构建时）

```bash
./generate-keystore.sh
```

这会在 `app/keystore.jks` 生成一个签名密钥文件。

#### 2. 构建Debug APK

```bash
./gradlew assembleDebug
```

生成的APK位置：`app/build/outputs/apk/debug/app-debug.apk`

#### 3. 构建Release APK（优化版）

```bash
./gradlew assembleRelease
```

生成的APK位置：`app/build/outputs/apk/release/app-release.apk`

## 安装APK

### 通过ADB安装

如果你的设备已通过USB连接并启用了USB调试：

```bash
# 安装Debug版本
adb install -r app/build/outputs/apk/debug/app-debug.apk

# 或安装Release版本
adb install -r app/build/outputs/apk/release/app-release.apk
```

### 直接安装

1. 将APK文件复制到你的Android设备
2. 在设备上找到APK文件
3. 点击APK文件开始安装
4. 如果提示"未知来源"，需要在设置中允许从该来源安装应用

## Debug vs Release

### Debug版本
- **优点**：
  - 构建速度快
  - 包含调试信息
  - 可以使用Android Studio调试
- **缺点**：
  - APK体积较大
  - 性能略低
- **适用于**：开发和测试

### Release版本
- **优点**：
  - 代码已混淆优化
  - APK体积更小
  - 性能更好
- **缺点**：
  - 构建时间较长
  - 无法调试
- **适用于**：正式发布和日常使用

## 签名密钥信息

默认的开发签名密钥信息（**仅用于开发/测试**）：

- **文件位置**: `app/keystore.jks`
- **密钥别名**: `pushup-key`
- **密钥密码**: `android123`
- **Keystore密码**: `android123`

⚠️ **重要提示**：此密钥仅用于开发和测试目的。如果要发布到Google Play或其他应用商店，请生成并使用专用的生产密钥，并妥善保管密钥文件和密码。

## 构建配置

项目的签名配置在 `app/build.gradle.kts` 中定义：

```kotlin
signingConfigs {
    create("release") {
        storeFile = file("keystore.jks")
        storePassword = "android123"
        keyAlias = "pushup-key"
        keyPassword = "android123"
    }
}
```

密钥信息从 `keystore.properties` 文件读取，该文件格式如下：

```properties
storePassword=android123
keyPassword=android123
keyAlias=pushup-key
storeFile=keystore.jks
```

## 生产环境发布

如果要发布到生产环境（如Google Play），请参考 [DEPLOYMENT.md](DEPLOYMENT.md) 文档，其中包含：

1. 生成生产密钥的详细步骤
2. 配置签名的最佳实践
3. 使用环境变量保护密钥
4. 构建AAB（Android App Bundle）
5. 上传到Google Play的完整流程

### 生成生产密钥

```bash
keytool -genkey -v -keystore pushup-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias pushup-release
```

然后更新 `keystore.properties` 文件指向新的密钥。

## 常见问题

### 1. 构建失败：找不到Android SDK

确保已安装Android SDK，或者让Gradle自动下载：

```bash
./gradlew --version
```

### 2. 签名密钥不存在

运行密钥生成脚本：

```bash
./generate-keystore.sh
```

### 3. "无法安装应用"错误

确保：
- 设备已启用"未知来源"安装
- 如果已安装旧版本，先卸载
- 或使用 `adb install -r` 覆盖安装

### 4. 构建时内存不足

编辑 `gradle.properties` 并增加堆内存：

```properties
org.gradle.jvmargs=-Xmx2048m
```

## 清理构建

如果遇到构建问题，可以清理并重新构建：

```bash
./gradlew clean
./gradlew assembleDebug
```

## 查看APK信息

使用以下命令查看APK的详细信息：

```bash
# 查看APK内容
unzip -l app/build/outputs/apk/debug/app-debug.apk

# 查看APK签名
jarsigner -verify -verbose -certs app/build/outputs/apk/debug/app-debug.apk
```

## 持续集成

如果要在CI/CD环境中构建，可以使用环境变量：

```bash
export KEYSTORE_PASSWORD=your_password
export KEY_PASSWORD=your_key_password
export KEY_ALIAS=your_alias

./gradlew assembleRelease
```

## 支持

如有问题，请查看：
- [README.md](README.md) - 项目概述
- [DEVELOPMENT.md](DEVELOPMENT.md) - 开发指南
- [DEPLOYMENT.md](DEPLOYMENT.md) - 部署指南
