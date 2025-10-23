# 俯卧撑计数器 (PushUp Counter)

一款基于AI姿态识别的Android俯卧撑计数应用，可以上传视频进行实时分析和计数。

## 功能特点

- 📹 **视频上传**: 从相册选择俯卧撑视频进行分析
- 🎥 **实时播放**: 在视频播放的同时进行姿态识别
- 🤖 **AI计数**: 使用Google ML Kit的姿态检测技术自动识别和计数俯卧撑
- 📊 **实时反馈**: 显示当前动作状态、角度和计数
- ✅ **姿态检测**: 检测俯卧撑动作的规范性

## 技术栈

- **Kotlin**: 主要开发语言
- **Android SDK**: 目标版本 34 (Android 14)
- **Google ML Kit**: 姿态检测 (Pose Detection)
- **ExoPlayer**: 视频播放
- **CameraX**: 相机功能（未来支持）
- **Material Design**: UI设计

## 工作原理

应用使用Google ML Kit的姿态检测API来分析视频中的人体姿态：

1. 用户上传俯卧撑视频
2. 应用按固定时间间隔提取视频帧
3. 对每一帧进行姿态检测，识别关键身体部位（肩膀、肘部、手腕等）
4. 计算肘部角度来判断俯卧撑的上下动作
5. 检测身体对齐情况，判断动作是否规范
6. 实时更新计数和状态

## 使用方法

1. 打开应用
2. 授予相机和存储权限
3. 点击"选择视频"按钮
4. 从相册中选择一个俯卧撑视频
5. 等待应用分析视频
6. 观看视频播放，同时查看实时计数

## 构建和运行

### 前置要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 8 或更高版本
- Android SDK 34
- Gradle 8.2

### 构建步骤

1. 克隆项目:
```bash
git clone <repository-url>
cd PushUpCounter
```

2. 运行设置脚本（可选）:
```bash
./setup.sh
```

3. 使用Android Studio打开项目

4. 同步Gradle并下载依赖

5. 连接Android设备或启动模拟器

6. 点击运行按钮或执行:
```bash
./gradlew installDebug
```

> **注意**: 首次运行时，Gradle会自动下载所需的依赖和工具。这可能需要几分钟时间。

## 项目结构

```
app/src/main/
├── java/com/pushup/counter/
│   ├── MainActivity.kt              # 主界面
│   ├── VideoAnalysisActivity.kt     # 视频分析界面
│   └── PushUpDetector.kt           # 俯卧撑检测逻辑
├── res/
│   ├── layout/                     # 布局文件
│   ├── values/                     # 资源值
│   ├── drawable/                   # 图标资源
│   └── xml/                        # XML配置
└── AndroidManifest.xml             # 应用清单
```

## 权限说明

应用需要以下权限：

- **CAMERA**: 用于未来的实时摄像功能
- **READ_MEDIA_VIDEO** (Android 13+): 读取视频文件
- **READ_EXTERNAL_STORAGE** (Android 12及以下): 读取外部存储

## 未来计划

- [ ] 实时摄像头录制和分析
- [ ] 历史记录和统计
- [ ] 多种锻炼类型支持（仰卧起坐、深蹲等）
- [ ] 社交分享功能
- [ ] 个性化训练计划

## 许可证

本项目使用 MIT 许可证。详见 [LICENSE](LICENSE) 文件。

## 文档

- [使用指南](USAGE.md) - 详细的使用说明和技巧
- [功能特性](FEATURES.md) - 完整的功能列表和技术细节
- [开发指南](DEVELOPMENT.md) - 开发环境设置和代码规范
- [贡献指南](CONTRIBUTING.md) - 如何为项目做贡献
- [更新日志](CHANGELOG.md) - 版本历史和更新记录
- [项目总结](PROJECT_SUMMARY.md) - 项目概览和架构说明

## 贡献

欢迎提交问题和拉取请求！详见 [CONTRIBUTING.md](CONTRIBUTING.md)
