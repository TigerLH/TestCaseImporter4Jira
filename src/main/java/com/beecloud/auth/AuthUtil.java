package com.beecloud.auth;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.esotericsoftware.yamlbeans.YamlReader;

public class AuthUtil {
	private static final String AuthYaml = AuthUtil.class.getClassLoader().getResource("Auth.yml").getFile();
	public static Auth getAuth() throws Exception{
		YamlReader reader = new YamlReader(new InputStreamReader(new FileInputStream(AuthYaml),"UTF-8"));
		Auth auth = reader.read(Auth.class);
        return auth;
    }
}
