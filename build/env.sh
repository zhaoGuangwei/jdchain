
#定义默认的子脚本错误退出命令；
if [ ! $RTN ]
then
    #当以 source 命令调用子脚本时，只能用 return 命令终止子脚本的执行；
    #当直接在 shell 中调用脚本或者 fork 等其它命令执行子脚本时，用 exit 命令终止脚本执行；
    RTN="exit"
fi

if [ $ENV_SHELL ]
then
    #如果已经设置过环境变量，则不再重复执行；
    echo "略过初始化环境变量。。。"
    ${RTN}
fi

echo "---------------- 初始化环境变量 ----------------"

#主代码库的根目录
BASE_DIR=$(cd `dirname $0`/..; pwd)

#标记环境变量脚本；
ENV_SHELL=$BASE_DIR/build/env.sh

#执行代码库更新的脚本；
UPDATE_SHELL=$BASE_DIR/build/repo.sh

#执行测试的脚本；
TEST_SHELL=$BASE_DIR/build/test.sh

#执行构建打包产品的脚本；
PACK_SHELL=$BASE_DIR/build/pack.sh

#框架工程的Git仓库的根目录
FRAMEWORK_DIR=$BASE_DIR/framework

#核心实现工程的Git仓库的根目录
CORE_DIR=$BASE_DIR/core

#打包工程的Git仓库的根目录
DEPLOY_DIR=$BASE_DIR/deploy

#测试工程的Git仓库的根目录
TEST_DIR=$BASE_DIR/test

#初始化参数：是否略过测试步骤；
SKIP_TESTS=0

#初始化参数：是否略过子模块的更新；默认值：1-忽略；
SKIP_SUBMODULES_UPDATE=1

#初始化参数：是否将仓库采用公开配置（即覆盖本地化配置）；
CONFIG_REPO_PUBLIC=0

#检查输入参数
for i in $*; do 
    case $i in 
    "--skipTests")
        #跳过测试；
        echo "收到参数 --skipTests 指示跳过测试环节。。。"

        SKIP_TESTS=1
        ;;
    "--update")
        #跳过更新子模块；
        echo "收到参数 --update 指示跳过子模块源码更新环节。。。"

        SKIP_SUBMODULES_UPDATE=0
        ;;
    "--public")
        #仓库采用公开配置；
        echo "收到参数 --public 指示仓库采用公开配置。。。"

        CONFIG_REPO_PUBLIC=1
        ;;

    esac
done

echo "---------------- 完成环境变量初始化 ----------------"


