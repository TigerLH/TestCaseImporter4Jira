package com.beecloud.auth;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.chainsaw.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.yamlbeans.YamlReader;

public class AuthUtil {
	private static Logger logger = LoggerFactory.getLogger(AuthUtil.class);
	public static Auth getAuth() throws Exception{
		InputStream AuthYaml = AuthUtil.class.getClassLoader().getResourceAsStream("Auth.yml");
		YamlReader reader = new YamlReader(new InputStreamReader(AuthYaml,"UTF-8"));
		Auth auth = reader.read(Auth.class);
		AuthYaml.close();//此处不关闭,会导致该方法不能重复调用
		logger.info(auth.toString());
        return auth;
    }
}
