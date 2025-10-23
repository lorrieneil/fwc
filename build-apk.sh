#!/bin/bash

# APK构建脚本
# 用于构建可安装的Android APK文件

set -e

echo "========================================="
echo "  俯卧撑计数器 APK 构建脚本"
echo "========================================="
echo ""

# 检查keystore文件是否存在
KEYSTORE_FILE="app/keystore.jks"
if [ ! -f "$KEYSTORE_FILE" ]; then
    echo "警告: 未找到签名密钥文件 ($KEYSTORE_FILE)"
    echo "正在生成签名密钥..."
    echo ""
    ./generate-keystore.sh
    if [ $? -ne 0 ]; then
        echo "错误: 密钥生成失败"
        exit 1
    fi
    echo ""
fi

# 询问构建类型
echo "请选择构建类型:"
echo "  1) Debug APK (默认，快速构建，包含调试信息)"
echo "  2) Release APK (优化版本，体积更小，已混淆)"
echo ""
read -p "请输入选择 [1-2] (默认: 1): " BUILD_TYPE

case $BUILD_TYPE in
    2)
        BUILD_VARIANT="Release"
        GRADLE_TASK="assembleRelease"
        ;;
    *)
        BUILD_VARIANT="Debug"
        GRADLE_TASK="assembleDebug"
        ;;
esac

echo ""
echo "开始构建 $BUILD_VARIANT APK..."
echo ""

# 确保gradlew可执行
chmod +x ./gradlew

# 清理之前的构建
echo "清理之前的构建..."
./gradlew clean

# 构建APK
echo ""
echo "构建APK (这可能需要几分钟)..."
./gradlew $GRADLE_TASK

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================="
    echo "  ✓ APK构建成功！"
    echo "========================================="
    echo ""
    
    # 查找生成的APK文件
    if [ "$BUILD_VARIANT" = "Release" ]; then
        APK_PATH="app/build/outputs/apk/release/app-release.apk"
    else
        APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
    fi
    
    if [ -f "$APK_PATH" ]; then
        APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
        echo "APK位置: $APK_PATH"
        echo "文件大小: $APK_SIZE"
        echo ""
        echo "安装方法:"
        echo "  1. 通过ADB安装:"
        echo "     adb install -r $APK_PATH"
        echo ""
        echo "  2. 直接复制到手机:"
        echo "     将APK文件复制到手机，点击安装"
        echo ""
        
        # 复制到项目根目录方便访问
        OUTPUT_APK="PushUpCounter-${BUILD_VARIANT}.apk"
        cp "$APK_PATH" "$OUTPUT_APK"
        echo "APK已复制到: $OUTPUT_APK"
    else
        echo "警告: 未找到生成的APK文件"
        echo "请检查 app/build/outputs/apk/ 目录"
    fi
else
    echo ""
    echo "========================================="
    echo "  ✗ APK构建失败"
    echo "========================================="
    echo ""
    echo "请检查上面的错误信息"
    exit 1
fi

echo ""
echo "完成！"
