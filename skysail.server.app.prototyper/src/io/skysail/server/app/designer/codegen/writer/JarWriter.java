package io.skysail.server.app.designer.codegen.writer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;

import io.skysail.domain.core.EntityModel;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JarWriter {

    public static JarOutputStream startBundleJar(DesignerApplicationModel applicationModel) throws FileNotFoundException, IOException {

        String packageName = applicationModel.getPackageName();

        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");

        Attributes global = manifest.getMainAttributes();
        global.put(new Attributes.Name("Bnd-LastModified"), Long.toString(new Date().getTime()));
        global.put(new Attributes.Name("Bundle-Description"), "skysail application bundle created by the designer");
        global.put(new Attributes.Name("Bundle-License"), "http://www.opensource.org/licenses/apache2.0.php;description=\"Apache 2.0 Licensed\";link=LICENSE");
        global.put(new Attributes.Name("Bundle-ManifestVersion"), "2");
        global.put(new Attributes.Name("Bundle-Name"), applicationModel.getProjectName());
        global.put(new Attributes.Name("Bundle-SymbolicName"), applicationModel.getProjectName());
        global.put(new Attributes.Name("Bundle-Version"), "0.1.0");

         AttributesWriter importPackageWriter = new AttributesWriter("Import-Package")
            .addVersion("com.fasterxml.jackson.annotation","[2.5,3)")
            .addVersion("io.skysail.api.links","[6.0,7)")
            .addVersion("io.skysail.api.responses","[18.1,19)")
            .addVersion("io.skysail.domain","[0.2,1)")
            .addVersion("io.skysail.domain.core","[4.0,5)")
            .addVersion("io.skysail.domain.core.repos","[1.0,2)")
            .addVersion("io.skysail.domain.html","[0.1,1)")
            .addVersion("io.skysail.server",null)
            .addVersion("io.skysail.server.app","[11.0,12)")
            .addVersion("io.skysail.server.db","[8.1,9)")
            .addVersion("io.skysail.server.domain.jvm","[1.0,2)")
            .addVersion("io.skysail.server.domain.jvm","[1.0,1.1)")
            .addVersion("io.skysail.server.menus","[1.0,1.1)")
            .addVersion("io.skysail.server.queryfilter","[0.5,1)")
            .addVersion("io.skysail.server.queryfilter.pagination","[0.2,1)")
            .addVersion("io.skysail.server.restlet","[4.1,5)")
            .addVersion("io.skysail.server.restlet.resources","[10.0,11)")
            .addVersion("io.skysail.server.security.config","[1.0,2)")
            .addVersion("javassist.util.proxy", null)
            .addVersion("javax.persistence","[2.1,3)")
            .addVersion("org.apache.shiro","[1.2,2)")
            .addVersion("org.apache.shiro.subject","[1.2,2)")
            .addVersion("org.osgi.framework","[1.8,2)")
            .addVersion("org.osgi.service.event","[1.3,2)")
            .addVersion("org.restlet",null)
            .addVersion("org.restlet.resource",null)
            ;

         global.put(importPackageWriter.getAttributesName(),importPackageWriter.getAttributesValue());

//        global.put(new Attributes.Name("Import-Package"),
//                //  "de.twenty11.skysail.server.core.restlet;version=\"[22.0,23)\","
//        		  "io.skysail.server.security.config;version=\"[0.3,1)\","
//                + "io.skysail.api.links;version=\"[6.0,7)\","
//                + "io.skysail.api.responses;version=\"[18.0,19)\","
//                + "io.skysail.domain;version=\"[0.1,1)\","
//                + "io.skysail.domain.core;version=\"[3.0,4)\","
//                + "io.skysail.domain.core.repos;version=\"[1.0,2)\","
//                + "io.skysail.domain.html;version=\"[0.1,1)\","
//                + "io.skysail.server,"
//                + "io.skysail.server.domain.jvm;version=\"[0.2,1)\","
//                + "io.skysail.server.app;version=\"[9.0,10)\","
//                + "io.skysail.server.db;version=\"[8.0,9)\","
//                + "io.skysail.server.forms;version=\"[8.2,9)\","
//                + "io.skysail.server.menus;version=\"[0.1,0.2)\","
//                + "io.skysail.server.queryfilter;version=\"[0.5,1)\","
//                + "io.skysail.server.queryfilter.pagination;version=\"[0.2,1)\","
//                + "io.skysail.server.restlet;version=\"[3.1,4)\","
//                + "io.skysail.server.restlet.resources;version=\"[8.1,9)\","
//                + "javax.persistence;version=\"[2.1,3)\","
//                + "org.apache.shiro;version=\"[1.2,2)\","
//                + "org.apache.shiro.subject;version=\"[1.2,2)\","
//                + "org.osgi.service.event;version=\"[1.3,2)\","
//                + "org.restlet,org.restlet.resource,org.osgi.framework;version=\"[1.8,2)\","
//                + "javassist.util.proxy");

        global.put(new Attributes.Name("Include-Resource"), "templates=src;recursive:=true;filter:=*.st|*.stg");

        global.put(new Attributes.Name("Private-Package"), packageName + ";version=\"0.1.0\","
                + "translations");

        global.put(new Attributes.Name("Provide-Capability"),
                  "osgi.service;objectClass:List<String>=\"de.twenty11.skysail.server.app.ApplicationProvider,"
                + "io.skysail.server.menus.MenuItemProvider\","
                + "osgi.service;objectClass:List<String>=\"io.skysail.domain.core.repos.DbRepository\"");

        global.put(new Attributes.Name("Require-Capability"),
                  "osgi.service;filter:=\"(objectClass=io.skysail.domain.core.Repositories)\";effective:=active,"
                + "osgi.service;filter:=\"(objectClass=io.skysail.server.db.DbService)\";effective:=active,"
                + "osgi.ee;filter:=\"(&(osgi.ee=JavaSE)(version=1.8))\"");

        Collection<EntityModel<?>> rootEntities = applicationModel.getRootEntities();
        String repos = rootEntities.stream().map(e -> "OSGI-INF/"+packageName+"."+e.getSimpleName()+"Repository.xml").collect(Collectors.joining(","));
        String serviceComponent = repos + ",OSGI-INF/"+packageName+"."+applicationModel.getName()+"Application.xml";
        global.put(new Attributes.Name("Service-Component"), serviceComponent);

        global.put(new Attributes.Name("Tool"), "skysail");

        String projectName = applicationModel.getProjectName();
        return new JarOutputStream(new FileOutputStream("designerbundles/" + projectName + ".jar"),
                manifest);
    }

    public static void add(JarOutputStream bundleJar, String path) throws IOException {
        add(new File(path), path, "", bundleJar);
    }

    private static void add(File source, String rootPath, String jarPath, JarOutputStream target) throws IOException {
        BufferedInputStream in = null;
        jarPath = jarPath.replace("\\", "/");
        rootPath = FilenameUtils.normalize(new File(rootPath).getAbsolutePath());
        try {
            if (source.isDirectory()) {
                String name = source.getPath().replace("\\", "/");

                if (!jarPath.isEmpty()) {
                    if (!name.endsWith("/")) {
                        name += "/";
                    }
                    if (jarPath.length() > 0 && !jarPath.endsWith("/")) {
                        jarPath += "/";
                    }
                    log.info("creating new jar entry with path '{}'", jarPath);
                    JarEntry entry = new JarEntry(jarPath);
                    entry.setTime(source.lastModified());
                    target.putNextEntry(entry);
                    target.closeEntry();
                }
                for (File nestedFile : source.listFiles()) {
                    //jarPath = "." + nestedFile.toString().substring(rootPath.length());
                    String normalized = FilenameUtils.normalize(nestedFile.getAbsolutePath());
                    jarPath = normalized.substring(rootPath.length()+1);
                    add(nestedFile, rootPath, jarPath, target);
                }
                return;
            }

            log.info("creating new jar entry with path '{}'", jarPath);
            JarEntry entry = new JarEntry(jarPath);//source.getPath().replace("\\", "/"));
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            in = new BufferedInputStream(new FileInputStream(source));

            byte[] buffer = new byte[1024];
            while (true) {
                int count = in.read(buffer);
                if (count == -1)
                    break;
                target.write(buffer, 0, count);
            }
            target.closeEntry();
        } finally {
            if (in != null)
                in.close();
        }
    }

}
