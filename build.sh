#!/bin/bash

# 围棋做题App构建脚本
# 使用方法: ./build.sh [clean|build|run]

echo "开始构建围棋做题App..."

case "$1" in
    "clean")
        echo "清理项目..."
        ./gradlew clean
        ;;
    "build")
        echo "构建项目..."
        ./gradlew build
        ;;
    "run")
        echo "运行应用..."
        ./gradlew installDebug
        ;;
    *)
        echo "使用方法: ./build.sh [clean|build|run]"
        echo "clean - 清理项目"
        echo "build - 构建项目"
        echo "run - 安装并运行应用"
        ;;
esac

echo "构建完成！"