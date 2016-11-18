#!/bin/bash



if [ $# -lt 1 ];then
echo "Error! Should enter the root directory of xcode project after the ipa-build command."
exit 2
fi

if [ ! -d $1 ];then
echo "Error! The first param must be a directory."
exit 2
fi

#工程绝对路径
cd $1
project_path=$(pwd)

#编译的configuration，默认为Release
build_config=Release

param_pattern=":nc:o:g:p:t:ws:"
OPTIND=2
while getopts $param_pattern optname
do
case "$optname" in
"n")
should_clean=y
;;
"c")
tmp_optind=$OPTIND
tmp_optname=$optname
tmp_optarg=$OPTARG
OPTIND=$OPTIND-1
code_sign=$optname
if getopts $param_pattern optname ;then
echo  "Error argument value for option $tmp_optname"
exit 2
fi
OPTIND=$tmp_optind

build_config=$tmp_optarg

;;
"g")
tmp_optind=$OPTIND
tmp_optname=$optname
tmp_optarg=$OPTARG

OPTIND=$OPTIND-1


if getopts $param_pattern optname ;then
echo  "Error argument value for option $tmp_optname"
exit 2
fi
OPTIND=$tmp_optind
buildSign=$tmp_optarg

;;
"p")
tmp_optind=$OPTIND
tmp_optname=$optname
tmp_optarg=$OPTARG

OPTIND=$OPTIND-1


if getopts $param_pattern optname ;then
echo  "Error argument value for option $tmp_optname"
exit 2
fi
OPTIND=$tmp_optind
profileFile=$tmp_optarg

;;
"o")
tmp_optind=$OPTIND
tmp_optname=$optname
tmp_optarg=$OPTARG

OPTIND=$OPTIND-1
if getopts $param_pattern optname ;then
echo  "Error argument value for option $tmp_optname"
exit 2
fi
OPTIND=$tmp_optind

cd $tmp_optarg
output_path=$(pwd)
if [ ! -d $output_path ];then
echo "Error!The value of option o must be an exist directory."
exit 2
fi

;;
"w")
workspace_name='*.xcworkspace'
ls $project_path/$workspace_name &>/dev/null
rtnValue=$?
if [ $rtnValue = 0 ];then
build_workspace=$(echo $(basename $project_path/$workspace_name))
else
echo  "Error!Current path is not a xcode workspace.Please check, or do not use -w option."
exit 2
fi

;;
"s")
tmp_optind=$OPTIND
tmp_optname=$optname
tmp_optarg=$OPTARG

OPTIND=$OPTIND-1
if getopts $param_pattern optname ;then
echo  "Error argument value for option $tmp_optname"
exit 2
fi
OPTIND=$tmp_optind

build_scheme=$tmp_optarg

;;
"t")
tmp_optind=$OPTIND
tmp_optname=$optname
tmp_optarg=$OPTARG

OPTIND=$OPTIND-1
if getopts $param_pattern optname ;then
echo  "Error argument value for option $tmp_optname"
exit 2
fi
OPTIND=$tmp_optind

build_target=$tmp_optarg

;;


"?")
echo "Error! Unknown option $OPTARG"
exit 2
;;
":")
echo "Error! No argument value for option $OPTARG"
exit 2
;;
*)
# Should not occur
echo "Error! Unknown error while processing options"
exit 2
;;
esac
done

echo "buildSign:${buildSign}" 
echo "profileFile:${profileFile}"
#build文件夹路径
build_path=${project_path}/build
#生成的app文件目录
appdirname=Release-iphoneos
if [ $build_config = Debug ];then
appdirname=Debug-iphoneos
fi
#编译后文件路径(仅当编译workspace时才会用到)
compiled_path=${build_path}/${appdirname}

#是否clean
if [ "$build_config" = "y" ];then
xcodebuild clean
fi


#组合编译命令
build_cmd='xcodebuild'

if [ "$build_workspace" != "" ];then
#编译workspace
if [ "$build_scheme" = "" ];then
echo "Error! Must provide a scheme by -s option together when using -w option to compile a workspace."
exit 2
fi

build_cmd=${build_cmd}' -workspace '${build_workspace}' -scheme '${build_scheme}'  -configuration '${build_config}'  CONFIGURATION_BUILD_DIR='${compiled_path}' ONLY_ACTIVE_ARCH=NO ' 

else
#编译project
echo "build cmd:${build_cmd}\n"
build_cmd=${build_cmd}' -configuration '${build_config}   'PROVISIONING_PROFILE'  ${profileFile}
echo "build cmd2:" $build_cmd

if [ "$build_target" != "" ];then
build_cmd=${build_cmd}' -target '${build_target}
fi

fi


#编译工程
cd $project_path
$build_cmd || exit

#进入build路径
cd $build_path

#创建ipa-build文件夹
if [ -d ./ipa-build ];then
rm -rf ipa-build
fi
mkdir ipa-build



#app文件名称
appname=$(basename ./${appdirname}/*.app)

#通过app文件名获得工程target名字
target_name=$(echo $appname | awk -F. '{print $1}')
#app文件中Info.plist文件路径
app_infoplist_path=${build_path}/${appdirname}/${appname}/Info.plist
#取版本号
bundleShortVersion=$(/usr/libexec/PlistBuddy -c "print CFBundleShortVersionString" ${app_infoplist_path})
#取build值
bundleVersion=$(/usr/libexec/PlistBuddy -c "print CFBundleVersion" ${app_infoplist_path})

#IPA名称
ipa_name="${target_name}_${bundleShortVersion}_${build_config}${bundleVersion}_$(date +"%Y%m%d")"
ipa_name="release"
echo $ipa_name

#xcrun打包
#xcrun -sdk iphoneos PackageApplication -v ./${appdirname}/*.app -o ${build_path}/ipa-build/${ipa_name}.ipa || exit

#if [ "$output_path" != "" ];then
#cp ${build_path}/ipa-build/${ipa_name}.ipa $output_path/${ipa_name}.ipa
#echo "Copy ipa file successfully to the path $output_path/${ipa_name}.ipa"
#fi
