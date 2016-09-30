package com.beecloud.auth;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.yamlbeans.YamlReader;

public class AuthUtil {
	private static Logger logger = LoggerFactory.getLogger(AuthUtil.class);
	private static final InputStream AuthYaml = AuthUtil.class.getClassLoader().getResourceAsStream("Auth.yml");
	public static Auth getAuth() throws Exception{
		YamlReader reader = new YamlReader(new InputStreamReader(AuthYaml,"UTF-8"));
		Auth auth = reader.read(Auth.class);
		logger.info(auth.toString());
        return auth;
    }
}
