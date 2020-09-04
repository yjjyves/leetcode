package com.yves.spring.example.config;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class GetResourceTest {

	public static void main(String[] args) throws IOException {
		Enumeration<URL> enu = GetResourceTest.class.getClassLoader().getResources("com/yves/example/CBean.class");
		while (enu.hasMoreElements()) {
			System.out.println(enu.nextElement());
		}

		ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(true);
		scan.findCandidateComponents("com/yves");
	}
}
