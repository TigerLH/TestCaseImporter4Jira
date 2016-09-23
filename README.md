# TestCaseImporter4Jira
###前提:Jira自身支持的CSV导入不能兼容测试步骤
###原理:使用Jira Api导入
###使用到的三方库：jira-rest-java-client-core、com.esotericsoftware.yamlbeans、org.apache.poi、ch.qos.logback
###打包:mvn assembly:assembly
###输出:可执行文件target/com.beecloud.jira-0.0.1-SNAPSHOT-jar-with-dependencies.jar




##注意事项：
Jira的Url、用户名、密码可在resource/Auth.yml中配置,以实际使用环境为准
程序通过项目名称查找Excel中对应的sheet页进行数据读取并导入，所以必须使sheet名称与所选项目名称保存一致
