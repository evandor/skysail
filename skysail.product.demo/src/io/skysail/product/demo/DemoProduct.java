//package io.skysail.product.demo;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import org.osgi.service.component.annotations.Component;
//
//import io.skysail.server.product.ProductDefinition;
//import lombok.extern.slf4j.Slf4j;
//
//@Component
//@Slf4j
//public class DemoProduct implements ProductDefinition {
//
//	@Override
//	public byte[] installationPublicKey() {
//		return read(ETC_SERVER_KEY_PUBLIC);
//	}
//
//	@Override
//	public byte[] installationPrivateKey() {
//		return read(ETC_SERVER_KEY_PRIVATE);
//	}
//
//	private byte[] read(String filename) {
//		try {
//			Path path = Paths.get(filename);
//			return Files.readAllBytes(path);
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//		}
//		return null;
//	}
//
//}
