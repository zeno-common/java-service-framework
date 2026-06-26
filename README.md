
# git submodule

git submodule deinit -f docs/jsf-skills 
git rm -f docs/jsf-skills
重新添加：git submodule add <新URL> <新路径>
提交更改：git add .gitmodules 并 git commit

git submodule add --force https://github.com/zeno-common/java-service-framework-doc.git docs/jsf-skills 