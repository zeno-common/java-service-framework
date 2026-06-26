
# git submodule

注销并清理：git submodule deinit -f <路径> 和 git rm -f <路径>
重新添加：git submodule add <新URL> <新路径>
提交更改：git add .gitmodules 并 git commit

git submodule add https://github.com/zeno-common/java-service-framework-doc.git docs/jsf-skills 