#!/bin/bash

# 俯卧撑计数器 - 项目设置脚本

echo "================================"
echo "俯卧撑计数器项目设置"
echo "================================"
echo ""

# 检查Java是否安装
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到Java"
    echo "请安装JDK 8或更高版本"
    exit 1
fi

echo "✅ Java版本:"
java -version

# 检查Android SDK
if [ -z "$ANDROID_HOME" ]; then
    echo "⚠️  警告: ANDROID_HOME未设置"
    echo "请设置ANDROID_HOME环境变量指向Android SDK"
else
    echo "✅ Android SDK: $ANDROID_HOME"
fi

# 赋予gradlew执行权限
chmod +x gradlew
echo "✅ Gradle wrapper已准备就绪"

# 如果gradle wrapper jar不存在，生成它
if [ ! -f gradle/wrapper/gradle-wrapper.jar ]; then
    echo "⏳ 正在生成Gradle wrapper..."
    gradle wrapper --gradle-version 8.2
    echo "✅ Gradle wrapper已生成"
fi

echo ""
echo "================================"
echo "设置完成！"
echo "================================"
echo ""
echo "下一步："
echo "1. 使用Android Studio打开项目"
echo "   或"
echo "2. 运行 ./gradlew assembleDebug 构建项目"
echo ""
