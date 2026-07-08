pnpm dlx skills add https://github.com/zeno-common/soil-coding-skills.git --skill java-sdk-doc-generator
pnpm dlx skills add https://github.com/znlgis/opengis-skills --skill superpowers-zh
pnpm dlx skills add https://github.com/anthropics/skills --skill skill-creator

java-coding-guidelines
java-sdk-doc-generator

pnpm dlx skills add https://github.com/espennilsen/pi --skill readme-reviewer
pnpm dlx skills add https://github.com/openyida/openyida --skill skill-optimization-guide


# Submodule

## 在克隆（clone）仓库时直接拉取
`git clone --recurse-submodules <主仓库URL>`

## 拉取后手动初始化和更新子模块
`git submodule update --init --recursive`

## 在拉取（pull）代码时同步更新
`git pull --recurse-submodules`

## 仅更新已初始化的子模块：如果子模块已经初始化过，只需拉取主项目记录的最新提交，可以直接运行：
`git submodule update --recursive

## 进阶技巧：配置全局自动拉取
`git config --global submodule.recurse true`